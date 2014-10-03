package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by rasmus on 9/16/14.
 */
public class IntroScreen extends RestServer {

    Button newGame;
    Button joinGame;
    Button funcTestScreen;
    EditText inputField;
    static String username = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_screen);

        //prepare dem buttons
        newGame = (Button) findViewById(R.id.new_game);
        joinGame = (Button) findViewById(R.id.join_game);
        funcTestScreen = (Button) findViewById(R.id.func_test_screen);
        inputField = (EditText) findViewById(R.id.edittext);

        newGame.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                username = inputField.getText().toString();

                if(username.length() > 3)
                {
                    requestPost("http://marvin.idyia.dk/game/new",
                        new HashMap<String, String>() {{
                            put("username", username);
                        }},
                        new NewGameCallback());
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Username must be min. 4 characters", Toast.LENGTH_SHORT).show();
                }
            }
        });

        joinGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = inputField.getText().toString();

                if(username.length() > 3)
                {
                    requestPost("http://marvin.idyia.dk/game/new",
                            new HashMap<String, String>() {{
                                put("username", username);
                            }},
                            new JoinGameCallback());
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Username must be min. 4 characters", Toast.LENGTH_SHORT).show();
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

        public void onEndRequest(String result)
        {
            Intent i = new Intent(getApplicationContext(), NewGameScreen.class);
            startActivity(i);
        }
    }

    private class JoinGameCallback implements RestCallbackInterface {

        public void onEndRequest(String result)
        {
            Intent i = new Intent(getApplicationContext(), JoinGame.class);
            startActivity(i);
        }
    }
}
