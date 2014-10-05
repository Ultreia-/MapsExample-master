package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.RestServer;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Tobias on 03-10-2014.
 */
public class WaitForPartnerScreen extends RestServer {

    Button cancelNewGameButton;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.wait_for_partner_screen);

        Toast.makeText(getBaseContext(), "Looking for your partner...", Toast.LENGTH_SHORT).show();

        cancelNewGameButton = (Button) findViewById(R.id.cancelGame);

        cancelNewGameButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

               requestPost("http://marvin.idyia.dk/game/cancel",
                    new HashMap<String, String>() {{
                        put("gameId", Game.id);
                    }},
                    new CancelGameCallback());
            }
        });
    }

    @Override
    public void onBackPressed() {
        requestPost("http://marvin.idyia.dk/game/cancel",
                new HashMap<String, String>() {{
                    put("gameId", Game.id);
                }},
                new CancelGameCallback());
    }

    private class CancelGameCallback implements RestCallbackInterface {

        public void onEndRequest(JSONObject result)
        {
            Game.id = null;
            Intent i = new Intent(getApplicationContext(), IntroScreen.class);
            startActivity(i);
        }
    }
}
