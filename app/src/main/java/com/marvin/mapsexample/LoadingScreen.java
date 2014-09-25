package com.marvin.mapsexample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ProgressBar;

/**
 * Created by Nicklas on 24-09-2014.
 */
public class LoadingScreen extends FragmentActivity{

    private ProgressBar progressbar;
    private int progress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        new Thread(new Runnable() {

            public void run() {
                progressbar = (ProgressBar) findViewById(R.id.progressBar);

                try {
                    while (progress < 100) {
                        if(progress < 25) {
                            progress += 7;
                            progressbar.setProgress(progress);
                            Thread.sleep(200);
                        }

                        if(progress >= 25 && progress < 50) {
                            progress += 5;
                            progressbar.setProgress(progress);
                            Thread.sleep(150);
                        }

                        if (progress >= 50 && progress < 80) {
                            progress += 4;
                            progressbar.setProgress(progress);
                            Thread.sleep(100);
                        }

                        if(progress >= 80) {
                            progress += 3;
                            progressbar.setProgress(progress);
                            Thread.sleep(75);
                        }
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
