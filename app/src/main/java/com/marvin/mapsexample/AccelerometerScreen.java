package com.marvin.mapsexample;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class AccelerometerScreen extends FragmentActivity implements SensorEventListener{

    Sensor accelerometer;
    SensorManager sm;
    TextView accelerationProgress;

    public static final float BYTES_IN_MB = 1024.0f * 1024.0f;
    public static final float BYTES_PER_PX = 4.0f;

    ImageView image;


    public float[] gravity = new float[3];
    public float[] linear_acceleration = new float[3];
    public int progress;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accelerometer_screen);

        image = (ImageView) findViewById(R.id.accelerometer_image);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

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

        if(linear_acceleration[0] > 4 && progress < 99) {
            progress += 3;
            setText();
        } else if(linear_acceleration[0] > 4 && progress == 99) {
            setText();
            progress += 1;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (progress >= 20 && progress < 45) {
                        setFlicker();
                        Thread.sleep(400);
                        removeFlicker();
                    }
                    if (progress >= 45 && progress < 65) {
                        setFlicker();
                        Thread.sleep(250);
                        removeFlicker();
                    }
                    if (progress >= 65 && progress < 80) {
                        setFlicker();
                        Thread.sleep(175);
                        removeFlicker();
                    }
                    if (progress >= 80 && progress < 100) {
                        setFlicker();
                        Thread.sleep(100);
                        removeFlicker();
                    }
                    if(progress == 100) {
                        removeFlicker();
                        setText();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
                if(progress == 100) {
                    accelerationProgress.setText("You have cuttededed the power mah maaaan! Good job");
                }else {
                    accelerationProgress.setText("Shake the phone till 100% You are at: " + progress + "%");
                }
            }
        });
    }

    public void setFlicker() {
        image.post(new Runnable() {
            @Override
            public void run() {
                if(readBitMapInfo() > megabytesFree()) {
                    subSampleImage(32);
                } else {
                    image.setImageResource(R.drawable.flicker);
                }
            }
        });
    }

    public void removeFlicker() {
        image.post(new Runnable() {
            @Override
            public void run() {
                image.setImageResource(0);
            }
        });
    }

    private float readBitMapInfo() {
        final Resources res = this.getResources();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, R.drawable.flicker, options);
        final float imageHeight = options.outHeight;
        final float imageWidth = options.outWidth;
        final String imageMimeType = options.outMimeType;

        Log.d("ScaleBeforeLoad", "w, h, type: " + imageWidth + ", " + imageHeight + ", " + imageMimeType);
        Log.d("ScaleBeforeLoad", "estimated memory required in MB: " + imageWidth * imageHeight * BYTES_PER_PX / BYTES_IN_MB);

        return imageWidth * imageHeight * BYTES_PER_PX / BYTES_IN_MB;
    }

    public void subSampleImage(int powerOf2) {
        if(powerOf2 < 1 || powerOf2 > 32) {
            Log.e("ScaleBeforeLoad", "trying to apply upscale or excessive downscale" + powerOf2);
            return;
        }

        final Resources res = this.getResources();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = powerOf2;
        final Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.flicker, options);
        image.setImageBitmap(bmp);

    }

    public static float megabytesFree() {
        final Runtime rt = Runtime.getRuntime();
        final float bytesUsed = rt.totalMemory();
        final float mbUsed = bytesUsed / BYTES_IN_MB;
        final float mbFree = megabytesAvailable() - mbUsed;
        return mbFree;
    }

    public static float megabytesAvailable() {
        final Runtime rt = Runtime.getRuntime();
        final float bytesAvailable = rt.maxMemory();
        return bytesAvailable / BYTES_IN_MB;
    }
}