package com.marvin.mapsexample.DialClasses;

import android.app.Activity;
import android.os.Bundle;

import com.marvin.mapsexample.SignalRedirect.PaintView;

/**
 * Created by Rasmus on 10/3/2014.
 */
public class InitiateSineView extends Activity{

    SineView sineView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sineView = new SineView(this);
        setContentView(sineView);
    }
}
