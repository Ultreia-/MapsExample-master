package com.marvin.mapsexample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

/**
 * Created by nicklasjust on 25/09/14.
 */
public class OSScreen extends FragmentActivity{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.os_screen);

        TextView text = (TextView) findViewById(R.id.textView);
        text.setText("Agent: " + IntroScreen.username);


    }
}
