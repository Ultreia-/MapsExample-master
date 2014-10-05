package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.Player;
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
    static String partnerName = "";
    Button findGame;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.join_game_screen);

        TextView text = (TextView) findViewById(R.id.textView);
        findPartner = (Button) findViewById(R.id.findPartner);
        inputField = (EditText)findViewById(R.id.edittext);

        text.setText("Welcome, " + Game.player.getName());

        findPartner.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                partnerName = inputField.getText().toString();

                requestPost("http://marvin.idyia.dk/game/join",
                        new HashMap<String, String>() {{
                            put("partnerName", partnerName);
                        }},
                        new JoinGameCallback());
            }
        });
    }

    private class JoinGameCallback implements RestCallbackInterface {

        public void onEndRequest(JSONObject result)
        {
            JSONObject status;
            JSONObject data;
            String gameId;
            try {

                if(result != null) {
                    status = result.getJSONObject("status");

                    if (status.equals("200")) {
                        data = result.getJSONObject("data");
                        gameId = data.getString("gameId");

                        Game.id = gameId;

                        Intent i = new Intent(getApplicationContext(), FunctionScreen.class);
                        startActivity(i);
                    } else {
                        Toast.makeText(getBaseContext(), "There is no player with such name. Try again.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(), "Somethings not right.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
