package com.marvin.mapsexample;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marvin.mapsexample.ARView.ARView;
import com.marvin.mapsexample.HelperPackage.Game;
import com.marvin.mapsexample.HelperPackage.Player;
import com.marvin.mapsexample.HelperPackage.RestCallbackInterface;
import com.marvin.mapsexample.HelperPackage.RestServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends RestServer implements LocationListener {

    GoogleMap googleMap;
    LocationManager locationManager;

    public double latitude;
    public double longitude;
    public double ARLatitude;
    public double ARLongitude;
    public double hqLat;
    public double hqLong;
    public float distance;

    private TimerTask timerTask;
    private Timer timer;

    private boolean stopRestPing = false;
    public Intent i;

    public Location hqLocation;
    public double distToMarker = 30;

    private String currentIdMarkerId;
    Bundle extras;
    double lat;
    double lng;
    String title;
    String snippet;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        } else {
            TextView tvLocation = (TextView) findViewById(R.id.tv_location);
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
            googleMap = fm.getMap();
            googleMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            tvLocation.setText("Welcome, " + IntroScreen.playerName);

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            //addTestMarkerToMap();

            if (location != null) {
                onLocationChanged(location);
            }

            i = this.getIntent();
            if (i != null) {
                currentIdMarkerId = i.getExtras().getString("id");
                if (currentIdMarkerId.equals("s2")) {
                    extras = i.getExtras();
                    lat = extras.getDouble("lat");
                    lng = extras.getDouble("lng");
                    title = extras.getString("title");
                    snippet = extras.getString("snippet");
                    addMarkerToMap(lat, lng, title, snippet);
                }
                if (currentIdMarkerId.equals("s1")) {
                    extras = i.getExtras();
                    lat = extras.getDouble("lat");
                    lng = extras.getDouble("lng");
                    title = extras.getString("title");
                    snippet = extras.getString("snippet");
                    addMarkerToMap(lat, lng, title, snippet);
                }
            }
            locationManager.requestLocationUpdates(provider, 1500, 0, this);
        }
        //addTestMarkerToMap();
    }

    private void addTestMarkerToMap() {
        LatLng pos = new LatLng(56.172675, 10.186526);
        hqLocation = new Location("Test");
        hqLocation.setLatitude(56.172675);
        hqLocation.setLongitude(10.186526);

        googleMap.addMarker(new MarkerOptions()
                        .title("MalCorp")
                        .snippet("MalCorp HQ")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .position(pos)
        );
    }

    public void addMarkerToMap(double lat, double lng, String title, String snippet) {
        LatLng pos = new LatLng(lat, lng);

        MarkerOptions marker = new MarkerOptions()
                .title(title)
                .snippet(snippet)
                .position(pos);
        googleMap.addMarker(marker);

    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Location markerLocation = new Location(currentIdMarkerId);
        markerLocation.setLatitude(lat);
        markerLocation.setLongitude(lng);

        if (location != null) {

            distance = location.distanceTo(markerLocation);

            if (distance < distToMarker) {

                //Toast.makeText(getBaseContext(), "location change in range", Toast.LENGTH_SHORT).show();

                if (currentIdMarkerId.equals("s1")) {

                    requestPost("http://marvin.idyia.dk/player/hasArrivedAtS",
                            new HashMap<String, String>() {{
                                put("sId", currentIdMarkerId);
                                put("playerOne", String.valueOf(Game.playerOne));
                                put("gameId", Game.id);
                            }},
                            new PlayerHasArrivedSCallback());
                }
                /*Intent intent = new Intent(getApplicationContext(), ARView.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "locations for AR");
                bundle.putDouble("lat", ARLatitude);
                bundle.putDouble("lng", ARLongitude);
                intent.putExtras(bundle);
                startActivity(intent);
                System.out.println(distance);*/
            } else {
                //Toast.makeText(getBaseContext(), "location change "+Float.toString(distance), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void onPause() {
        super.onPause();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public void onResume() {
        super.onResume();
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
        }
    }

    private class PlayerHasArrivedSCallback implements RestCallbackInterface {
        public void onEndRequest(JSONObject result) {
            try {

                String status = result.getString("status");
                if (status.equals("200")) {

                    JSONObject data = result.getJSONObject("data");
                    final String sId = data.getString("sId");

                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                            }

                            if(!stopRestPing) {
                                requestGet("http://marvin.idyia.dk/game/havebotharriveds/" + sId,
                                        new HaveBothArrivedSCallback());
                            }
                        }
                    };

                    timer = new Timer();
                    timer.scheduleAtFixedRate(timerTask, 0, 2000);
                } else throw new Exception(status);

            } catch (JSONException e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "PlayerHasArrivedSCallback; JSON "+e, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "PlayerHasArrivedSCallback; status " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class HaveBothArrivedSCallback implements RestCallbackInterface {
        public void onEndRequest(JSONObject result) {
            try {

                String status = result.getString("status");

                if (status.equals("200")) {

                    JSONObject data = result.getJSONObject("data");
                    //String playerAArrived = data.getString("playerAArrived");
                    String playerAArrived = "1";
                    String playerBArrived = data.getString("playerBArrived");

                    if(playerAArrived.equals("1") && playerBArrived.equals("1"))
                    {
                        stopRestPing = true;
                        timerTask.cancel();
                        timer.cancel();

                        Intent i = new Intent(getApplicationContext(), LoadingScreen.class);
                        startActivity(i);
                    }
                    else if((playerAArrived.equals("1") && Game.playerOne && playerBArrived.equals("0")) || (playerBArrived.equals("0") && playerBArrived.equals("1")))
                    {
                        Toast.makeText(getBaseContext(), "You have arrived, wait for your partner.", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "HaveBothArrivedSCallback; JSON " + e, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //e.printStackTrace();
                Toast.makeText(getBaseContext(), "HaveBothArrivedSCallback; status " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
