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

        if (Game.currentMission.equals("s1"))
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                new AlertDialog.Builder(OSScreen.this)
                    .setTitle("Internal Memo")
                    .setMessage("If you are reading this, something went wrong. This OS contains one of two parts of a vital package you must deliver. Follow the coordinates.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(OSScreen.this, MapsScreen.class);
                        Bundle b = new Bundle();

                        Game.currentMission = "s2";

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
        else if (Game.currentMission.equals("s2"))
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                new AlertDialog.Builder(OSScreen.this)
                    .setTitle("Message from Robert")
                    .setMessage("Hello. I don't know who you are, but apparently Jim trusts you. I don't want to ask this of you, but we don't have a choice. Go to the location provided on your map. I will contact you again when you arrive.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent i = new Intent(OSScreen.this, MapsScreen.class);
                            Bundle b = new Bundle();

                            Game.currentMission = "s3";

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
        else if (Game.currentMission.equals("s3"))
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    new AlertDialog.Builder(OSScreen.this)
                        .setTitle("Message from Robert")
                        .setMessage("Well done agents")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent i = new Intent(OSScreen.this, MapsScreen.class);
                                Bundle b = new Bundle();

                                Game.currentMission = "1cs-2sr";

                                b.putDouble("lat", 56.173105);
                                b.putDouble("lng", 10.184879);
                                b.putString("title", "RFID scanner");
                                b.putString("snippet", "Go and scramble this RFID scanner to protect the privacy of the people");

                                b.putDouble("lat-2", 56.173462);
                                b.putDouble("lng-2", 10.187035);
                                b.putString("title-2", "RFID scanner");
                                b.putString("snippet-2", "Go and scramble this RFID scanner to protect the privacy of the people");

                                i.putExtras(b);

                                startActivity(i);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_email)
                        .show();
                }
            }, 3000);
        }
        else if (Game.currentMission.equals("1cs-2sr"))
        {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                new AlertDialog.Builder(OSScreen.this)
                    .setTitle("Message from Robert")
                    .setMessage("Well done! That was just in the nick of time! We could use people like you in The White Compass. Would you care to join us in our course to liberate the world? I already have two missions, you can chose from.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Intent i = new Intent(OSScreen.this, MapsScreen.class);
                            Bundle b = new Bundle();

                            b.putDouble("lat", 56.172917);
                            b.putDouble("lng", 10.186582);
                            b.putString("title", "RFID scanner");
                            b.putString("snippet", "Go and scramble this RFID scanner to protect the privacy of the people");
                            i.putExtras(b);

                            i.putExtras(b);

                            startActivity(i);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_email)
                    .show();
                }
            }, 3000);
        }
        else if (Game.currentMission.equals("2sr")) {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    new AlertDialog.Builder(OSScreen.this)
                            .setTitle("Message from Robert")
                            .setMessage("Well done agents, you scrambled the RFID scanner")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_email)
                            .show();
                }
            }, 3000);
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
