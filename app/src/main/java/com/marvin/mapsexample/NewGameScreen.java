package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Tobias on 03-10-2014.
 */
public class NewGameScreen extends RestServer{

    Button cancelNewGameButton;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.new_game_screen);

        Toast.makeText(getBaseContext(), "Looking for other agents...", Toast.LENGTH_SHORT).show();

        cancelNewGameButton = (Button) findViewById(R.id.cancelGame);

        cancelNewGameButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

               requestPost("http://marvin.idyia.dk/game/cancel",
                    new HashMap<String, String>() {{
                        put("username", IntroScreen.username);
                    }},
                    new CancelGameCallback());
            }
        });
    }

    private class CancelGameCallback implements RestCallbackInterface {

        public void onEndRequest(String result)
        {
            Intent i = new Intent(getApplicationContext(), IntroScreen.class);
            startActivity(i);
        }
    }
}
