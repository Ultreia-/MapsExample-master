package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by christianheldingsrensen on 24/09/14.
 */
public class FunctionScreen extends FragmentActivity {

    Button loadTest;
    Button mapTest;
    Button camTest;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.func_screen);

        TextView text = (TextView) findViewById(R.id.textView);
        loadTest = (Button) findViewById(R.id.loading_test);
        mapTest = (Button) findViewById(R.id.map_test);
        camTest = (Button) findViewById(R.id.cam_test);

        text.setText("Welcome, " + IntroScreen.username);

        loadTest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoadingScreen.class);
                startActivity(i);
            }
        });

        mapTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });

        camTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CameraLandingPage.class);
                startActivity(i);
            }
        });
    }
}