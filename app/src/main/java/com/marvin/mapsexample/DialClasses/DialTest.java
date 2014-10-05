package com.marvin.mapsexample.DialClasses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.marvin.mapsexample.R;
import com.marvin.mapsexample.SignalRedirect.InitiatePaintView;

public class DialTest extends Activity implements DialModel.Listener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dial_screen);

        DialView leftDial = (DialView) findViewById(R.id.left_dial);
        leftDial.getModel().addListener(this);

    }

    @Override
    public void onDialPositionChanged(DialModel sender, int nicksChanged) {
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(sender.getCurrentNick() + "");
    }

}