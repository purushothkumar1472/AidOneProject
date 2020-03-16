package in.apexcoders.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class AidService extends Service implements SensorEventListener {
    SensorManager sensorManager;
    SmsManager smsManager;
    Sensor sensorAccelerometer;
    String CHANNEL_ID = "ForegroundServiceChannel";
    SharedPreferences sharedPreferences;
    int SHAKE_THRESHOLD = 700;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;



    public AidService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("FallDetection", "Started");
        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sharedPreferences = getSharedPreferences("PreferenceAidYou", 0);
        sharedPreferences.edit().putBoolean("ServiceOn", true).apply();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundApi();
        return super.onStartCommand(intent, flags, startId);
    }

    void startForegroundApi() {
        NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Foreground Service Channel AidYou", NotificationManager.IMPORTANCE_MIN);
        NotificationManager mgr = getSystemService(NotificationManager.class);
        mgr.createNotificationChannel(serviceChannel);

        Intent notificationIntent = new Intent(this, AidService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this, CHANNEL_ID).setContentTitle("Aiding You").setContentText("We are running for your safety").setTicker("Hello!").build();
        startForeground(10023, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
                if (speed > SHAKE_THRESHOLD) {
                    Toast.makeText(this, "Trigerred", Toast.LENGTH_SHORT).show();

                    smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(sharedPreferences.getString("EmergencyNum1", "9999999990"), null, "Help! I am in danger", null, null);
                    smsManager.sendTextMessage(sharedPreferences.getString("EmergencyNum2", "9999999990"), null, "Help! I am in danger", null, null);
                    smsManager.sendTextMessage(sharedPreferences.getString("EmergencyNum3", "9999999990"), null, "Help! I am in danger", null, null);
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
