package com.marvin.mapsexample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.Player;
import com.marvin.mapsexample.HelperPackage.RestServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Tobias on 03-10-2014.
 */
public class WaitForPartnerScreen extends RestServer {

    Button cancelNewGameButton;
    private TimerTask timerTask;
    private Timer timer;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.wait_for_partner_screen);

        cancelNewGameButton = (Button) findViewById(R.id.cancelGame);

        final Toast toast = Toast.makeText(getBaseContext(), "Looking for a partner...", Toast.LENGTH_SHORT);

        cancelNewGameButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

               requestPost("http://marvin.idyia.dk/game/cancel",
                    new HashMap<String, String>() {{
                        put("gameId", Game.id);
                    }},
                    new CancelGameCallback());
            }
        });

        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }

                toast.show();

                requestPost("http://marvin.idyia.dk/game/hasPartnerJoined",
                    new HashMap<String, String>() {{
                        put("gameId", Game.id);
                    }},
                    new FindPartnerCallback());
            }
        };

        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 4000);
    }

    @Override
    public void onBackPressed() {
        requestPost("http://marvin.idyia.dk/game/cancel",
                new HashMap<String, String>() {{
                    put("gameId", Game.id);
                }},
                new CancelGameCallback());
    }

    private class CancelGameCallback implements RestCallbackInterface {

        public void onEndRequest(JSONObject result)
        {
            timerTask.cancel();
            timer.cancel();

            Game.id = null;
            Intent i = new Intent(getApplicationContext(), IntroScreen.class);
            startActivity(i);
        }
    }

    private class FindPartnerCallback implements RestCallbackInterface {

        public void onEndRequest(JSONObject result)
        {
            try {

                String status = result.getString("status");

                if(status.equals("200"))
                {
                    JSONObject data = result.getJSONObject("data");
                    Boolean partnerHasJoined = data.getBoolean("partnerHasJoined");

                    if(partnerHasJoined)
                    {
                        String partnerId = data.getString("partnerId");
                        String partnerName = data.getString("partnerName");
                        Game.partner = new Player(partnerId, partnerName);

                        timerTask.cancel();
                        timer.cancel();

                        Intent i = new Intent(getApplicationContext(), GameStartScreen.class);
                        startActivity(i);
                    }
                }
                else throw new Exception(status);

            } catch (JSONException e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "Could not create player; status 500", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "Could not create player; status "+e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
