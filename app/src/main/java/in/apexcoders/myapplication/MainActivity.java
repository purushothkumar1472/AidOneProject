package in.apexcoders.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button serviceButton;
    SharedPreferences pref;
    Boolean isOn;
    Button editContactDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("PreferenceAidYou", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isFirstTime", true)) {
            getContactDetails();
        }
        setContentView(R.layout.activity_main);
    }

    private void getContactDetails() {
        Intent intent = new Intent(this, ContactFetchActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        0);
            }
        }
        serviceButton = findViewById(R.id.serviceButton);
        pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        isOn = pref.getBoolean("Service", false);
        if (isOn) {
            serviceButton.setText("Stop Service");
        } else {
            serviceButton.setText("Start Service");
        }

        serviceButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.serviceButton) {
            Intent intent = new Intent(MainActivity.this, AidService.class);

            if (isOn) {
                //stop the service
                stopService(intent);
                isOn = false;
                serviceButton.setText("Start Service");
            } else {
                //start the service
                startService(intent);
                isOn = true;
                serviceButton.setText("Stop Service");

            }
        }
    }

}
