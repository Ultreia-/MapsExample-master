package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Nicklas on 24-09-2014.
 */
public class LoadingScreen extends FragmentActivity{

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
                            setText();
                            progress += 1;
                            Thread.sleep(1500);
                            Intent i = new Intent(getApplicationContext(), OSScreen.class);
                            startActivity(i);
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void setText() {
        downloading.post(new Runnable() {
                          public void run() {
                              downloading.setText("Downloading Compass OS v1.1.21 " + progress + "%");
                          }
                      });
    }
}
