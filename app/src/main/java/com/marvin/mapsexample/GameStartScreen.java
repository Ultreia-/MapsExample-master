package com.marvin.mapsexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

        new Handler().postDelayed(new Runnable() {
            public void run() {

                AlertDialog.Builder ok = new AlertDialog.Builder(GameStartScreen.this);
                ok.setTitle("Help!!");
                ok.setMessage("Jim here. Can't say much, need help! I will send you my location, please hurry!");
                ok.setCancelable(false);
                ok.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface ok, int id) {
                        Intent i = new Intent(getApplicationContext(), MapsScreen.class);
                        Bundle b = new Bundle();
                        b.putString("id", "s1");
                        b.putDouble("lat", 56.154403);
                        b.putDouble("lng", 10.201871);
                        b.putString("title", "Save Jim");
                        b.putString("snippet", "Jim is being captured. Go and save him together");
                        i.putExtras(b);
                        startActivity(i);
                    }
                });
                ok.show();
            }
        }, 4000);
    }
}
