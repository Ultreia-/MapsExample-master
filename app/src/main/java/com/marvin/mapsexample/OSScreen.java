package com.marvin.mapsexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.RestServer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by nicklasjust on 25/09/14.
 */
public class OSScreen extends RestServer{

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.os_screen);

        if (Game.currentMisson.equals("s1"))
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                new AlertDialog.Builder(OSScreen.this)
                    .setTitle("Internal Memo")
                    .setMessage("If you read this, you have been chosen to carry out a task of great importance ... ")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(OSScreen.this, MapsScreen.class);
                        Bundle b = new Bundle();

                        Game.currentMisson = "s2";

                        b.putDouble("lat", 56.172917);
                        b.putDouble("lng", 10.186582);
                        b.putString("title", "Upload data");
                        b.putString("snippet", "Jim is being captured. Go and save him together");
                        i.putExtras(b);

                        startActivity(i);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_email)
                    .show();
                }
            }, 3000);

        }
        else if (Game.currentMisson.equals("s2"))
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                new AlertDialog.Builder(OSScreen.this)
                    .setTitle("Message from Robert")
                    .setMessage("Hello agents ... Execute Virus plx ... ")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent i = new Intent(OSScreen.this, MapsScreen.class);
                            Bundle b = new Bundle();

                            Game.currentMisson = "s3";

                            b.putDouble("lat", 56.172917);
                            b.putDouble("lng", 10.186582);
                            b.putString("title", "Execute Virus");
                            b.putString("snippet", "Go and Execute Virus");
                            i.putExtras(b);

                            startActivity(i);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_email)
                    .show();
                }
            }, 3000);
        }
        else if (Game.currentMisson.equals("s3"))
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                new AlertDialog.Builder(OSScreen.this)
                    .setTitle("Message from Robert")
                    .setMessage("Well done agents")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {

                            Game.currentMisson = "1CS-2SR";
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_email)
                    .show();
                }
            }, 3000);
        }
        else if (Game.currentMisson.equals("1CS-2SR"))
        {
            //Toast.makeText(getBaseContext(), "Well done agent", Toast.LENGTH_SHORT).show();
        }

        TextView text = (TextView) findViewById(R.id.textView);
        text.setText("Agent: " + Game.player.getName());

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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), IntroScreen.class);
        startActivity(i);
    }
}
