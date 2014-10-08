package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.Player;
import com.marvin.mapsexample.HelperPackage.RestCallbackInterface;
import com.marvin.mapsexample.HelperPackage.RestServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Nicklas on 24-09-2014.
 */
public class LoadingScreen extends RestServer{

    private ProgressBar progressbar;
    private TextView downloading;
    private int progress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        new Thread(new Runnable() {

            public void run() {
                progressbar = (ProgressBar) findViewById(R.id.progressBar);
                downloading = (TextView) findViewById(R.id.downloading);

                try {
                    while (progress < 100) {
                        if(progress < 25) {
                            progress += 7;
                            progressbar.setProgress(progress);
                            setText();
                            Thread.sleep(600);
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
                            Thread.sleep(200);
                        }

                        if(progress >= 80) {
                            progress += 3;
                            progressbar.setProgress(progress);
                            setText();
                            Thread.sleep(400);
                        }

                        if(progress >= 90) {
                            progress += 1;
                            progressbar.setProgress(progress);
                            setText();
                            Thread.sleep(600);
                        }

                        if(progress == 99) {
                            setText();
                            progress += 1;
                            Thread.sleep(500);

                            requestPost("http://marvin.idyia.dk/player/hasCompletedM",
                                new HashMap<String, String>() {{
                                    put("mId", Game.currentMission);
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
                Toast.makeText(getBaseContext(), "PlayerHasCompletedSCallback; JSON " + e, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "PlayerHasCompletedSCallback; status " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setText() {
        downloading.post(new Runnable() {
              public void run() {
                  downloading.setText("Downloading Compass OS v1.1.21 " + progress + "%");
              }
          });
    }
}
