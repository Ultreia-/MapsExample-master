package com.marvin.mapsexample;

import android.os.Bundle;
import android.widget.TextView;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.RestServer;

/**
 * Created by Tobias on 05-10-2014.
 */
public class GameStartScreen extends RestServer {

    TextView partnerName;
    TextView playerName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_start_screen);

        playerName = (TextView) findViewById(R.id.playerName);
        partnerName = (TextView) findViewById(R.id.partnerName);

        playerName.setText("Welcome, " + Game.player.getName());
        partnerName.setText("Welcome, " + Game.partner.getName());
    }
}
