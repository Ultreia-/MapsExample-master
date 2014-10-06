package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.marvin.mapsexample.CameraPackage.CameraLandingPage;
import com.marvin.mapsexample.ARView.ARView;
import com.marvin.mapsexample.DialClasses.DialTest;
import com.marvin.mapsexample.SignalRedirect.InitiatePaintView;
import com.marvin.mapsexample.SignalRedirect.PaintView;

/**
 * Created by christianheldingsrensen on 24/09/14.
 */
public class FunctionScreen extends FragmentActivity {

    Button loadTest;
    Button mapTest;
    Button camTest;
    Button restTest;
    Button dialTest;
    Button uploadTest;
    Button sigTest;
    Button accelTest;
    Button hackTest;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.func_screen);

        loadTest = (Button) findViewById(R.id.loading_test);
        mapTest = (Button) findViewById(R.id.map_test);
        camTest = (Button) findViewById(R.id.cam_test);
        restTest = (Button) findViewById(R.id.rest_test);
        dialTest = (Button) findViewById(R.id.dial_test);
        uploadTest = (Button) findViewById(R.id.upload_test);
        sigTest = (Button) findViewById(R.id.sig_test);
        accelTest = (Button) findViewById(R.id.accelerometer_test);
        hackTest = (Button) findViewById(R.id.hacking_test);


        loadTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoadingScreen.class);
                startActivity(i);
            }
        });

        mapTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                i.putExtra("id", "from function screen");
                startActivity(i);
            }
        });

        camTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ARView.class);
                startActivity(i);
            }
        });

        restTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), restTestScreen.class);
                startActivity(i);
            }
        });

        dialTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DialTest.class);
                startActivity(i);
            }
        });

        uploadTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), UploadingScreen.class);
                startActivity(i);
            }
        });

        sigTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), InitiatePaintView.class);
                startActivity(i);
            }
        });

        accelTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AccelerometerScreen.class);
                startActivity(i);
            }
        });

        hackTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), BasicHackScreen.class);
                startActivity(i);
            }
        });

    }
}