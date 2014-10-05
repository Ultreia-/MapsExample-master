package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.Player;
import com.marvin.mapsexample.HelperPackage.RestServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rasmus on 9/16/14.
 */
public class IntroScreen extends RestServer {

    Button newGame;
    Button joinGame;
    Button funcTestScreen;
    EditText inputField;

    static String playerName;
    static String playerId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_screen);

        if(Game.player != null) inputField.setText(Game.player.getName());

        //prepare dem buttons
        newGame = (Button) findViewById(R.id.new_game);
        joinGame = (Button) findViewById(R.id.join_game);
        funcTestScreen = (Button) findViewById(R.id.func_test_screen);
        inputField = (EditText) findViewById(R.id.edittext);

        newGame.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                playerName = inputField.getText().toString();

                if(playerName.length() > 3)
                {
                    requestPost("http://marvin.idyia.dk/player/create",
                        new HashMap<String, String>() {{
                            put("playerName", playerName);
                        }},
                        new NewGameCallback());
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Player name must be min. 4 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });

        joinGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                playerName = inputField.getText().toString();

                if(playerName.length() > 3)
                {
                    requestPost("http://marvin.idyia.dk/player/create",
                            new HashMap<String, String>() {{
                                put("playerName", playerName);
                            }},
                            new JoinGameCallback());
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Player name must be min. 4 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });

        funcTestScreen.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FunctionScreen.class);
                startActivity(i);
            }
        });
    }

    private class NewGameCallback implements RestCallbackInterface {

        public void onEndRequest(JSONObject result)
        {
            JSONObject data = null;

            try {

                data = result.getJSONObject("data");
                playerId = data.getString("playerId");

                Game.player = new Player(playerId, playerName);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            requestPost("http://marvin.idyia.dk/game/create",
                new HashMap<String, String>() {{
                    put("playerId", playerId);
                }},
                new CreateGameCallback());
        }
    }

    private class CreateGameCallback implements RestCallbackInterface{
        public void onEndRequest(JSONObject result)
        {
            JSONObject data = null;

            try {

                data = result.getJSONObject("data");
                Game.id = data.getString("gameId");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(getApplicationContext(), WaitingForPartnerScreen.class);
            startActivity(i);
        }
    }

    private class JoinGameCallback implements RestCallbackInterface {

        public void onEndRequest(JSONObject result)
        {
            JSONObject data = null;

            try {

                data = result.getJSONObject("data");
                playerId = data.getString("playerId");

                Game.player = new Player(playerId, playerName);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(getApplicationContext(), JoinGame.class);
            startActivity(i);
        }
    }
}
