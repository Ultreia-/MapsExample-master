package com.marvin.mapsexample.DialClasses;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.R;
import com.marvin.mapsexample.SignalRedirect.InitiatePaintView;

public class DialTest extends Activity implements DialModel.Listener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dial_screen);

        DialView leftDial = (DialView) findViewById(R.id.left_dial);
        leftDial.getModel().addListener(this);

        SeekBar scrambleControl = (SeekBar) findViewById(R.id.scramble_bar);
        scrambleControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 1;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                double progressDouble = (double) progressChanged;

                Game.scramble = (progressDouble/100);
                Log.v("Scramble", Double.toString(Game.scramble));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                /*Toast.makeText(DialTest.this, "seek bar progress:" + progressChanged,
                        Toast.LENGTH_SHORT).show();
                        */
            }
        });

    }

    @Override
    public void onDialPositionChanged(DialModel sender, int nicksChanged) {
        TextView text = (TextView) findViewById(R.id.text);
        text.setText(sender.getCurrentNick() + "");
    }

}