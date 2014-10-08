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
import com.marvin.mapsexample.HelperPackage.RestServer;

/**
 * Created by christianheldingsrensen on 05/10/14.
 */
public class BasicHackScreen extends RestServer {

    private EditText commandInput;
    private Button buttonRun;
    private String correntCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hack_screen);

        commandInput = (EditText) findViewById(R.id.commandInput);
        buttonRun = (Button) findViewById(R.id.runCommand);

        correntCommand = (Game.playerOne) ? Game.playerOneVirusCommand : Game.playerTwoVirusCommand;

        buttonRun.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(commandInput.getText().toString().equals(correntCommand))
                {
                    Intent i = new Intent(getApplicationContext(), OSScreen.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Unknown command", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
