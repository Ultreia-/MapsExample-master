package com.marvin.mapsexample;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

public class AccelerometerScreen extends FragmentActivity implements SensorEventListener{

    Sensor accelerometer;
    SensorManager sm;
    TextView accelerationX;
    TextView accelerationY;
    TextView accelerationZ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_screen);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        accelerationX = (TextView) findViewById(R.id.acceleration_x);
        accelerationY = (TextView) findViewById(R.id.acceleration_y);
        accelerationZ = (TextView) findViewById(R.id.acceleration_z);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        accelerationX.setText("Acceleration på X akse: " + sensorEvent.values[0]);
        accelerationY.setText("Acceleration på Y akse: " + sensorEvent.values[1]);
        accelerationZ.setText("Acceleration på Z akse: " + sensorEvent.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void onResume() {
        super.onResume();
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }
}