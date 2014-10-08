package com.marvin.mapsexample.DialClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.RestCallbackInterface;
import com.marvin.mapsexample.HelperPackage.RestServer;
import com.marvin.mapsexample.MapsScreen;
import com.marvin.mapsexample.OSScreen;
import com.marvin.mapsexample.R;
import com.marvin.mapsexample.SignalRedirect.InitiatePaintView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DialTest extends RestServer implements DialModel.Listener, SensorEventListener {

    Sensor accelerometer;
    SensorManager sm;

    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];
    private int progress;
    private boolean sendNewScrambleData = true;

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

        if(linear_acceleration[0] > 6 && progress <= 99)
        {
            progress += 30;
        }

        if(progress > 99 && sendNewScrambleData)
        {
            Log.v("Accelerometer!", "Activated");

            sendNewScrambleData = false;

            requestPost("http://marvin.idyia.dk/game/accelerometer",
                new HashMap<String, String>() {{
                    put("scramble", Double.toString(Game.scramble));
                    put("amplitude", Double.toString(Game.currentRotation));
                }},
                new AccelerometerCallback());
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

    private class AccelerometerCallback implements RestCallbackInterface
    {
        public void onEndRequest(JSONObject result)
        {
            try
            {
                String status = result.getString("status");
                if (status.equals("200"))
                {
                        requestGet("http://marvin.idyia.dk/game/hasAccelerometerDataBeenChecked/",
                        new HasAccelerometerDataBeenCheckedCallback());
                    //progress = 0;

                } else throw new Exception(status);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "PlayerHasArrivedSCallback; JSON " + e, Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "PlayerHasArrivedSCallback; status " + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class HasAccelerometerDataBeenCheckedCallback implements RestCallbackInterface
    {
        public void onEndRequest(JSONObject result)
        {
            try
            {
                String status = result.getString("status");
                if (status.equals("200"))
                {
                    JSONObject data = result.getJSONObject("data");
                    String dataHasBeenChecked = data.getString("dataHasBeenChecked");

                    Log.v("dataHasBeenChecked:", dataHasBeenChecked);

                    if(dataHasBeenChecked.equals("0"))
                    {
                        progress = 0;
                        sendNewScrambleData = true;
                    }
                    else
                    {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                requestGet("http://marvin.idyia.dk/game/hasAccelerometerDataBeenChecked/",
                                        new HasAccelerometerDataBeenCheckedCallback());

                            }
                        }, 1000);
                    }

                } else throw new Exception(status);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "PlayerHasArrivedSCallback; JSON " + e, Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "PlayerHasArrivedSCallback; status " + e, Toast.LENGTH_LONG).show();
            }
        }
    }
}