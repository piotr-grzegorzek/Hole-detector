package com.example.holedetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class GyroscopesActivity extends AppCompatActivity {

    // UI elements
    private TextView vx, vy, vz, GyroData;

    // Sensor-related variables
    private SensorManager sensorManager;
    private List<Sensor> sensorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscopes);

        // Initialize UI elements
        vx = findViewById(R.id.vx);
        vy = findViewById(R.id.vy);
        vz = findViewById(R.id.vz);
        GyroData = findViewById(R.id.gyroData);
        Button startButton = findViewById(R.id.button);
        Button nextActivityButton = findViewById(R.id.button2);

        // Initialize the SensorManager and get a list of available sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        // Display Gyro info
        displayGyroInfo();

        // Set click listeners for buttons
        startButton.setOnClickListener(v -> startGyro());

        nextActivityButton.setOnClickListener(v -> {
            stopGyro();
            startActivity(new Intent(GyroscopesActivity.this, GravityActivity.class));
        });
    }

    // Start the Gyro sensor
    private void startGyro() {
        if (sensorList.size() > 0) {
            Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManager.registerListener(gyroListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            showToast("Error: No Gyros found.");
        }
    }

    // Stop the Gyro sensor
    private void stopGyro() {
        sensorManager.unregisterListener(gyroListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopGyro();
    }

    // SensorEventListener for Gyro data
    private final SensorEventListener gyroListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                handleGyro(sensorEvent);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Handle accuracy changes if needed
        }

        private void handleGyro(SensorEvent event) {
            float[] values = event.values;
            vx.setText(String.valueOf(values[0]));
            vy.setText(String.valueOf(values[1]));
            vz.setText(String.valueOf(values[2]));
        }
    };

    // Display a Toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // Display Gyro information
    private void displayGyroInfo() {
        if (sensorList.size() > 0) {
            Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            if (gyroSensor != null) {
                String info = "Gyro Name: " + gyroSensor.getName() + "\n"
                        + "Vendor: " + gyroSensor.getVendor() + "\n"
                        + "Version: " + gyroSensor.getVersion() + "\n"
                        + "Type: " + gyroSensor.getType() + "\n"
                        + "Resolution: " + gyroSensor.getResolution() + "\n"
                        + "Power: " + gyroSensor.getPower() + " mA\n"
                        + "Maximum Range: " + gyroSensor.getMaximumRange() + "\n";
                GyroData.setText(info);
            } else {
                showToast("Error: No Gyro sensor found.");
            }
        } else {
            showToast("Error: No sensors found.");
        }
    }
}