package com.marvin.mapsexample.DialClasses;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.R;
import com.marvin.mapsexample.SignalRedirect.InitiatePaintView;

public class DialTest extends Activity implements DialModel.Listener, SensorEventListener {

    Sensor accelerometer;
    SensorManager sm;

    public float[] gravity = new float[3];
    public float[] linear_acceleration = new float[3];
    public int progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dial_screen);

        DialView leftDial = (DialView) findViewById(R.id.left_dial);
        leftDial.getModel().addListener(this);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        SeekBar scrambleControl = (SeekBar) findViewById(R.id.scramble_bar);
        scrambleControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 1;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                double progressDouble = (double) progressChanged;

                Game.scramble = (progressDouble/100.0);
                Log.v("Scramble", Double.toString(Game.scramble));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onDialPositionChanged(DialModel sender, int nicksChanged) {
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(sender.getCurrentNick() + "");
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        // alpha is calculated as t / (t + dT)
        // with t, the low-pass filter's time-constant
        // and dT, the event delivery rate

        final float alpha = 0.8f;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
        //gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
        //gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];

        linear_acceleration[0] = sensorEvent.values[0] - gravity[0];
        //linear_acceleration[1] = sensorEvent.values[1] - gravity[1];
        //linear_acceleration[2] = sensorEvent.values[2] - gravity[2];

        if(linear_acceleration[0] > 6 && progress < 99) {
            progress += 3;
        } else if(linear_acceleration[0] > 4 && progress == 99) {
            progress += 1;
            /*
            Her skal Tobias lave et eller andet til databasen vil jeg gå ud fra!
            Game.scramble og den anden jeg ikke kan huske
            Hvis det ikke er det rigtige så husk lige at sætte progress til 0.
             */
        }
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