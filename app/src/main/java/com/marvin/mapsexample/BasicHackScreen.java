package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.RestServer;

/**
 * Created by christianheldingsrensen on 05/10/14.
 */
public class BasicHackScreen extends RestServer {

    TextView terminal;
    String termText = "$\n"
            + "ls\n"
            + "config\n"
            + "cache\n"
            + "mnt\n"
            + "vendor\n"
            + "ueventd.rc\n"
            + "ueventd.goldfish.rc\n"
            + "system\n"
            + "default.prop\n"
            + "data\n"
            + "root\n"
            + ">..";
    //String termTextOut = "$ ls config cache mnt vendor ueventd.rc ueventd.goldfish.rc system default.prop data root >..";
    //String virusCode = "open sesame";
    Button commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hack_screen);

        commit = (Button) findViewById(R.id.commit);

        terminal = (TextView) findViewById(R.id.terminal);
        terminal.setText(termText);


        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(terminal.getText().toString().equals("$\n"
                        + "ls\n"
                        + "config\n"
                        + "cache\n"
                        + "mnt\n"
                        + "vendor\n"
                        + "ueventd.rc\n"
                        + "ueventd.goldfish.rc\n"
                        + "system\n"
                        + "default.prop\n"
                        + "data\n"
                        + "root\n"
                        + ">.." +"open sesame")){
                    Intent i = new Intent(getApplicationContext(), OSScreen.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getBaseContext(), "No deal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
