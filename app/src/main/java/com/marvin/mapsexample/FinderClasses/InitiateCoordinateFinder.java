package com.marvin.mapsexample.FinderClasses;

import android.app.Activity;
import android.os.Bundle;

import com.marvin.mapsexample.R;

/**
 * Created by Rasmus on 10/6/2014.
 */
public class InitiateCoordinateFinder extends Activity{


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new CoordinateFinder(this));
    }
}
