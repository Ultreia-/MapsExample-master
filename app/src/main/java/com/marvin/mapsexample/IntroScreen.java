package com.marvin.mapsexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    EditText playerNameInputField;

    static String playerName;
    static String playerId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_screen);

        //prepare dem buttons
        newGame = (Button) findViewById(R.id.new_game);
        joinGame = (Button) findViewById(R.id.join_game);
        funcTestScreen = (Button) findViewById(R.id.func_test_screen);
        playerNameInputField = (EditText) findViewById(R.id.edittext);

        if(Game.player != null) playerNameInputField.setText(Game.player.getName());

        newGame.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

            playerName = playerNameInputField.getText().toString();

            if(playerName.length() > 3)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(playerNameInputField.getWindowToken(), 0);

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

            playerName = playerNameInputField.getText().toString();

            if(playerName.length() > 3)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(playerNameInputField.getWindowToken(), 0);

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
            try {

                String status = result.getString("status");

                if(status.equals("200"))
                {
                    JSONObject data = result.getJSONObject("data");
                    playerId = data.getString("playerId");

                    Game.player = new Player(playerId, playerName);
                    Game.playerOne = true;

                    requestPost("http://marvin.idyia.dk/game/create",
                        new HashMap<String, String>() {{
                            put("playerId", playerId);
                        }},
                        new CreateGameCallback());
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

    private class CreateGameCallback implements RestCallbackInterface{
        public void onEndRequest(JSONObject result)
        {
            try
            {
                String status = result.getString("status");

                if(status.equals("200"))
                {
                    JSONObject data = result.getJSONObject("data");
                    Game.id = data.getString("gameId");

                    Intent i = new Intent(getApplicationContext(), WaitForPartnerScreen.class);
                    startActivity(i);
                }
                else throw new Exception(status);

            } catch (JSONException e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "Could not create game; status 500", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "Could not create game; status "+e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class JoinGameCallback implements RestCallbackInterface {

        public void onEndRequest(JSONObject result)
        {
            try {
                String status = result.getString("status");

                if(status.equals("200"))
                {
                    JSONObject data = result.getJSONObject("data");
                    playerId = data.getString("playerId");

                    Game.player = new Player(playerId, playerName);

                    Intent i = new Intent(getApplicationContext(), JoinGameScreen.class);
                    startActivity(i);

                }else throw new Exception(status);

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
