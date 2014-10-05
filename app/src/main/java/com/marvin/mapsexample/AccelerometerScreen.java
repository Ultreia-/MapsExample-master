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
    TextView accelerationProgress;

    public float[] gravity = new float[3];
    public float[] linear_acceleration = new float[3];
    public int progress;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_screen);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        accelerationX = (TextView) findViewById(R.id.acceleration_x);
        accelerationY = (TextView) findViewById(R.id.acceleration_y);
        accelerationZ = (TextView) findViewById(R.id.acceleration_z);
        accelerationProgress = (TextView) findViewById(R.id.acceleration_progress);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = 0.8f;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];

        linear_acceleration[0] = sensorEvent.values[0] - gravity[0];
        linear_acceleration[1] = sensorEvent.values[1] - gravity[1];
        linear_acceleration[2] = sensorEvent.values[2] - gravity[2];

        if(linear_acceleration[0] > 6 && progress < 100) {
            progress += 5;
            setText();
        }

        accelerationX.setText("Acceleration på X akse: " + linear_acceleration[0]);
        accelerationY.setText("Acceleration på Y akse: " + linear_acceleration[1]);
        accelerationZ.setText("Acceleration på Z akse: " + linear_acceleration[2]);
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

    public void setText() {
        accelerationProgress.post(new Runnable() {
            public void run() {
                accelerationProgress.setText("Shake the phone till 100% You are at: " + progress + "%");
            }
        });
    }
}