package com.marvin.mapsexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.RestServer;

import java.util.Timer;
import java.util.TimerTask;

public class GameStartScreen extends RestServer {

    private TimerTask timerTask;
    private Timer timer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_start_screen);

        if (Game.id != null
                && Game.player != null
                && Game.partner != null) {
            Toast.makeText(getBaseContext(), Game.player.getName() + " & " + Game.partner.getName(), Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            public void run() {

                    AlertDialog.Builder ok = new AlertDialog.Builder(GameStartScreen.this);
                    ok.setTitle("Help!!");
                    ok.setMessage("Message from Jim!");
                    ok.setCancelable(false);
                    ok.setPositiveButton("Ok", new  DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface ok, int id) {
                            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                            i.putExtra("id", "from function screen");
                            startActivity(i);
                        }
                    });
                    ok.show();
            }
        }, 4000);
        /*
        timerTask = new TimerTask() {

            @Override
            public void run() {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(GameStartScreen.this.getBaseContext());
                builder.setMessage("Message From Jim")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                                i.putExtra("id", "from function screen");
                                startActivity(i);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 5000);
        */
    }
}
