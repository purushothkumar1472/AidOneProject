package in.apexcoders.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button serviceButton;
    SharedPreferences pref;
    Boolean isOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
