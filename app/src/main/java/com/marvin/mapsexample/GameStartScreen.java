package com.marvin.mapsexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.RestServer;



public class GameStartScreen extends RestServer {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_start_screen);

        if(Game.id != null
        && Game.player != null
        && Game.partner != null)
        {
            Toast.makeText(getBaseContext(), Game.player.getName() + " & " + Game.partner.getName(), Toast.LENGTH_SHORT).show();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Look at this dialog!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(i);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
