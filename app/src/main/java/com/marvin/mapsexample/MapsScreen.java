package com.marvin.mapsexample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marvin.mapsexample.DialClasses.DialTest;
import com.marvin.mapsexample.FinderClasses.CoordinateFinder;
import com.marvin.mapsexample.FinderClasses.InitiateCoordinateFinder;
import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.RestCallbackInterface;
import com.marvin.mapsexample.HelperPackage.RestServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MapsScreen extends RestServer implements GooglePlayServicesClient.OnConnectionFailedListener, GooglePlayServicesClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener {

    private LocationClient locationClient = null;
    private GoogleMap googleMap = null;

    private LocationRequest request = LocationRequest.create()
            .setInterval(1000)
            .setFastestInterval(16)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    private Intent intent;
    private Bundle extras;

    private double markerLat;
    private double markerLng;
    private String markerTitle;
    private String markerSnippet;

    private boolean arrivedAtMarker = false;
    private boolean mapFollowPlayer = true;
    private boolean waitingForResponse = false;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        waitingForResponse = false;

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS)
        {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }
        else
        {
            if (googleMap == null)
            {
                SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
                googleMap = fm.getMap();
                googleMap.setMyLocationEnabled(true);

                if (googleMap != null) {
                    googleMap.setMyLocationEnabled(true);
                }
            }

            intent = this.getIntent();

            if (intent != null)
            {
                if (Game.currentMission.equals("s1")
                ||  Game.currentMission.equals("s2")
                ||  Game.currentMission.equals("s3")
                ||  Game.currentMission.equals("2sr"))
                {
                    extras = intent.getExtras();

                    if(extras != null)
                    {
                        markerLat = extras.getDouble("lat");
                        markerLng = extras.getDouble("lng");
                        markerTitle = extras.getString("title");
                        markerSnippet = extras.getString("snippet");
                    }
                    addMarkerToMap(markerLat, markerLng, markerTitle, markerSnippet);
                }
            }
        }
    }

    public void addMarkerToMap(double lat, double lng, String title, String snippet)
    {
        LatLng pos = new LatLng(lat, lng);

        MarkerOptions marker = new MarkerOptions()
            .title(title)
            .snippet(snippet)
            .position(pos);

        googleMap.addMarker(marker);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        LatLng latLng = new LatLng(latitude, longitude);

        if(mapFollowPlayer)
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            mapFollowPlayer = false;
        }

        Location markerLocation = new Location(Game.currentMission);
        markerLocation.setLatitude(markerLat);
        markerLocation.setLongitude(markerLng);

        float distanceToMarker = location.distanceTo(markerLocation);

        if (distanceToMarker < 50)
        {
            if(!waitingForResponse)
            {
                waitingForResponse = true;

                if (Game.currentMission.equals("s1")
                ||  Game.currentMission.equals("s2")
                ||  Game.currentMission.equals("s3")
                ||  Game.currentMission.equals("2sr"))
                {
                    requestPost("http://marvin.idyia.dk/player/hasArrivedAt",
                        new HashMap<String, String>() {{
                            put("mId", Game.currentMission);
                            put("playerOne", String.valueOf(Game.playerOne));
                            put("gameId", Game.id);
                        }},
                        new PlayerHasArrivedSCallback());
                }
                else
                {
                    waitingForResponse = false;
                }
            }
        }
        else
        {
            Toast.makeText(getBaseContext(), "Distance: " + Float.toString(distanceToMarker), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){ }

    @Override
    public void onConnected(Bundle connectionHint)
    {
        locationClient.requestLocationUpdates(request, this);
    }

    @Override
    public void onDisconnected() { }

    @Override
    public void onResume() {
        super.onResume();
        setupLocationClientIfRequested();
        locationClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        locationClient.disconnect();
    }

    private void setupLocationClientIfRequested()
    {
        if(locationClient == null)
        {
            locationClient = new LocationClient(this, this, this);
        }
    }

    private class PlayerHasArrivedSCallback implements RestCallbackInterface
    {
        public void onEndRequest(JSONObject result)
        {
            try
            {
                String status = result.getString("status");

                if (status.equals("200"))
                {
                    if (Game.currentMission.equals("s1")
                    ||  Game.currentMission.equals("s2")
                    ||  Game.currentMission.equals("s3"))
                    {
                        JSONObject data = result.getJSONObject("data");
                        final String mId = data.getString("mId");

                        requestGet("http://marvin.idyia.dk/game/haveBothArrivedS/" + mId,
                                new HaveBothArrivedSCallback());
                    }
                    else if(Game.currentMission.equals("2sr"))
                    {
                        requestGet("http://marvin.idyia.dk/game/haveBothArrivedSR/",
                                new HaveBothArrivedSCallback());
                    }

                } else throw new Exception(status);
            }
            catch (JSONException e)
            {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "PlayerHasArrivedSCallback; JSON " + e, Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "PlayerHasArrivedSCallback; status " + e, Toast.LENGTH_LONG).show();
            }
        }
    }

    private class HaveBothArrivedSCallback implements RestCallbackInterface
    {
        public void onEndRequest(JSONObject result)
        {
            try
            {


                String status = result.getString("status");

                if (status.equals("200")) {

                    JSONObject data = result.getJSONObject("data");
                    String playerAArrived = data.getString("playerAArrived");
                    String playerBArrived = data.getString("playerBArrived");
                    final String mId = data.getString("mId");

                    if(playerAArrived.equals("1") && playerBArrived.equals("1"))
                    {
                        if (Game.currentMission.equals("s1"))
                        {
                            Intent i = new Intent(getApplicationContext(), LoadingScreen.class);
                            startActivity(i);
                        }
                        else if (Game.currentMission.equals("s2"))
                        {
                            Intent i = new Intent(getApplicationContext(), UploadingScreen.class);
                            startActivity(i);
                        }
                        else if (Game.currentMission.equals("s3"))
                        {
                            new AlertDialog.Builder(MapsScreen.this)
                                .setTitle("Message from Robert")
                                .setMessage("Hello again agent ... You have arrived at the terminal ... To execute your part of the virus, you have to run following command in the terminal ... ")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        Intent i = new Intent(getApplicationContext(), BasicHackScreen.class);
                                        startActivity(i);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_email)
                                .show();
                        }
                        else if (Game.currentMission.equals("2sr"))
                        {
                            new AlertDialog.Builder(MapsScreen.this)
                                .setTitle("You have arrived at the RFID scanner")
                                .setMessage("Press ok to interact")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        Intent i;

                                        if(Game.playerOne)
                                        {
                                            i = new Intent(getApplicationContext(), DialTest.class);
                                        }
                                        else
                                        {
                                            i = new Intent(getApplicationContext(), InitiateCoordinateFinder.class);
                                        }

                                        startActivity(i);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                        }
                    }
                    else
                    {
                        if(!arrivedAtMarker)
                        {
                            arrivedAtMarker = true;
                            Toast.makeText(getBaseContext(), "You have arrived, wait for your partner.", Toast.LENGTH_LONG).show();
                        }

                        if (Game.currentMission.equals("s1")
                        ||  Game.currentMission.equals("s2")
                        ||  Game.currentMission.equals("s3"))
                        {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    requestGet("http://marvin.idyia.dk/game/haveBothArrivedS/" + mId,
                                            new HaveBothArrivedSCallback());
                                }
                            }, 1000);

                        }
                        else if(Game.currentMission.equals("2sr"))
                        {
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    requestGet("http://marvin.idyia.dk/game/haveBothArrivedSR/",
                                      new HaveBothArrivedSCallback());
                                }
                            }, 1000);
                        }
                    }
                }


            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "HaveBothArrivedSCallback; JSON " + e, Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "HaveBothArrivedSCallback; status " + e, Toast.LENGTH_LONG).show();
            }
        }
    }

}