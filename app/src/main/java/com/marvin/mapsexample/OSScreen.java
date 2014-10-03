package com.marvin.mapsexample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by nicklasjust on 25/09/14.
 */
public class OSScreen extends FragmentActivity{

    public static final String firstTime = "firstTime";
    public static final String myPreferences = "MyPrefs";

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.os_screen);

        pref = getSharedPreferences(myPreferences, Context.MODE_PRIVATE);
        editor = pref.edit();

        if(!pref.contains(firstTime)) {
            editor.putBoolean(firstTime, true);
            editor.commit();
            new AlertDialog.Builder(this)
                    .setTitle("Halp meeeee!!!")
                    .setMessage("Hola bromigos! I could really use some help from you, so if you want to, suck ma penis!")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putBoolean(firstTime, false);
                            editor.commit();
                            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                            Bundle b = new Bundle();
                            b.putString("id", "marker for S2");
                            b.putDouble("lat", 56.15674);
                            b.putDouble("lng", 10.20112);
                            b.putString("title", "S2");
                            b.putString("snippet", "Kæmpe meget mission");
                            i.putExtras(b);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putBoolean(firstTime, false);
                            editor.commit();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

        TextView text = (TextView) findViewById(R.id.textView);
        text.setText("Agent: " + IntroScreen.username);

        TabHost th = (TabHost) findViewById(R.id.tabHost);
        th.setup();

        TabHost.TabSpec specs = th.newTabSpec("tag1");
        specs.setContent(R.id.tab1);
        specs.setIndicator("Mission Log");
        th.addTab(specs);

        specs = th.newTabSpec("tag2");
        specs.setContent(R.id.tab2);
        specs.setIndicator("Notes");
        th.addTab(specs);

        specs = th.newTabSpec("tag3");
        specs.setContent(R.id.tab3);
        specs.setIndicator("Progress");
        th.addTab(specs);

    }
}
