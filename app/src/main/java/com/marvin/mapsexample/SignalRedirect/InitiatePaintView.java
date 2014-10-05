package com.marvin.mapsexample.SignalRedirect;

import android.app.Activity;
import android.os.Bundle;


/**
 * Created by christianheldingsrensen on 03/10/14.
 */
public class InitiatePaintView extends Activity {

    PaintView paintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paintView = new PaintView(this);
        setContentView(paintView);
    }
}
