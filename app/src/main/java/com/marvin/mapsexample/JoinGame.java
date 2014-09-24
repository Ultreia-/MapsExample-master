package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by christianheldingsrensen on 20/09/14.
 */
public class JoinGame extends FragmentActivity {

    EditText inputField;
    Button findPartner;
    static String partnerName = "";
    Button findGame;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.join_screen);

        TextView text = (TextView) findViewById(R.id.textView);
        findPartner = (Button) findViewById(R.id.find_partner);
        findGame = (Button) findViewById(R.id.find_game);
        inputField = (EditText)findViewById(R.id.edittext);

        text.setText("Welcome, " + IntroScreen.username);

        findPartner.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                partnerName = inputField.getText().toString();
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });

        findGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(i);
            }
        });
    }
}
