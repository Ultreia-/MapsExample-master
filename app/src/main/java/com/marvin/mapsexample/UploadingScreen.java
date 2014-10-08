package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.RestCallbackInterface;
import com.marvin.mapsexample.HelperPackage.RestServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by nicklasjust on 03/10/14.
 */
public class UploadingScreen extends RestServer{
    private ProgressBar progressbar;
    private TextView uploading;
    private int progress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploading_screen);

        new Thread(new Runnable() {

            public void run() {
                progressbar = (ProgressBar) findViewById(R.id.progressBar);
                uploading = (TextView) findViewById(R.id.uploading);

                try {
                    while (progress < 100) {
                        if(progress < 25) {
                            progress += 7;
                            progressbar.setProgress(progress);
                            setText();
                            Thread.sleep(200);
                        }

                        if(progress >= 25 && progress < 50) {
                            progress += 5;
                            progressbar.setProgress(progress);
                            setText();
                            Thread.sleep(150);
                        }

                        if (progress >= 50 && progress < 80) {
                            progress += 4;
                            progressbar.setProgress(progress);
                            setText();
                            Thread.sleep(100);
                        }

                        if(progress >= 80) {
                            progress += 3;
                            progressbar.setProgress(progress);
                            setText();
                            Thread.sleep(75);
                        }

                        if(progress >= 90) {
                            progress += 1;
                            progressbar.setProgress(progress);
                            setText();
                            Thread.sleep(50);
                        }

                        if(progress == 99) {
                            progress += 1;
                            setText();
                            Thread.sleep(1500);

                            requestPost("http://marvin.idyia.dk/player/hasCompletedS",
                                new HashMap<String, String>() {{
                                    put("sId", Game.currentMission);
                                    put("playerOne", String.valueOf(Game.playerOne));
                                    put("gameId", Game.id);
                                }},
                                new PlayerHasCompletedSCallback());
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private class PlayerHasCompletedSCallback implements RestCallbackInterface {

        public void onEndRequest(JSONObject result)
        {
            try {

                String status = result.getString("status");

                if(status.equals("200"))
                {
                    Intent i = new Intent(getApplicationContext(), OSScreen.class);
                    startActivity(i);

                } else throw new Exception(status);

            } catch (JSONException e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "Could not create player; status 500", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "Could not create player; status "+e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setText() {
        uploading.post(new Runnable() {
            public void run() {
                if(progress == 100) {
                    uploading.setText("Uploading package done!");
                } else {
                    uploading.setText("Uploading package " + progress + "%");
                }
            }
        });
    }
}
