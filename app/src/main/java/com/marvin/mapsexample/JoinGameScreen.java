package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.Player;
import com.marvin.mapsexample.HelperPackage.RestCallbackInterface;
import com.marvin.mapsexample.HelperPackage.RestServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by christianheldingsrensen on 20/09/14.
 */
public class JoinGameScreen extends RestServer {

    EditText inputField;
    Button findPartner;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.join_game_screen);

        TextView text = (TextView) findViewById(R.id.textView);
        findPartner = (Button) findViewById(R.id.findPartner);
        inputField = (EditText)findViewById(R.id.edittext);

        if(Game.player != null) {
            text.setText("Welcome, " + Game.player.getName());
        }

        findPartner.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                final String partnerName = inputField.getText().toString();

                requestPost("http://marvin.idyia.dk/game/join",
                    new HashMap<String, String>() {{
                        put("partnerName", partnerName);
                        put("playerId", Game.player.getId());
                    }},
                    new JoinGameCallback());
            }
        });
    }

    private class JoinGameCallback implements RestCallbackInterface {

        public void onEndRequest(JSONObject result)
        {
            try {

                String status = result.getString("status");

                if(status.equals("200"))
                {
                    JSONObject data = result.getJSONObject("data");
                    Boolean joined = data.getBoolean("joined");

                    if(joined)
                    {
                        String gameId = data.getString("gameId");
                        String partnerId = data.getString("partnerId");
                        String partnerName = data.getString("partnerName");

                        Game.id = gameId;
                        Game.partner = new Player(partnerId, partnerName);

                        Game.currentMission = "s1";

                        Intent i = new Intent(getApplicationContext(), GameStartScreen.class);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "No agents with such name found", Toast.LENGTH_SHORT).show();
                    }
                }
                else throw new Exception(status);

            } catch (JSONException e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "Could not create player; status 500", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "Could not create player; status "+e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
