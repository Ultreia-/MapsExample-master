package com.marvin.mapsexample;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by rasmus on 9/16/14.
 */
public class IntroScreen extends FragmentActivity {

    Button newGame;
    Button joinGame;
    EditText inputField;
    static String username = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //prepare dem buttons
        newGame = (Button) findViewById(R.id.new_game);
        joinGame = (Button) findViewById(R.id.join_game);
        inputField = (EditText)findViewById(R.id.edittext);

        newGame.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                username = inputField.getText().toString();
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });

        joinGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username= inputField.getText().toString();
                Intent i = new Intent(getApplicationContext(), JoinGame.class);
                startActivity(i);
            }
        });

    }
}
