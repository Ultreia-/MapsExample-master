package com.marvin.mapsexample;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.marvin.mapsexample.ARView.ARView;

public class MapsActivity extends FragmentActivity implements LocationListener {

    GoogleMap googleMap;
    LocationManager locationManager;

    public double latitude;
    public double longitude;
    public double ARLatitude;
    public double ARLongitude;
    public double hqLat;
    public double hqLong;
    public float distance;

    public Intent i;

    public Location hqLocation;
    public double distToMarker = 10;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if(status != ConnectionResult.SUCCESS){
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

            addTestMarkerToMap();

            if(location != null) {
                onLocationChanged(location);
            }

            i = this.getIntent();
            if(i != null) {
                String id = i.getExtras().getString("id");
                if(id.equals("new marker")) {
                    Bundle extras = i.getExtras();
                    double lat = extras.getDouble("lat");
                    double lng = extras.getDouble("lng");
                    String title = extras.getString("title");
                    String snippet = extras.getString("snippet");
                    addMarkerToMap(lat, lng, title, snippet);
                }
            }
            locationManager.requestLocationUpdates(provider, 1500, 0, this);
        }


        addTestMarkerToMap();
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

        if (location != null) {
            ARLatitude = latitude;
            ARLongitude = longitude;

            if (hqLocation != null) {
                System.out.println("Hey igen!");
                distance = location.distanceTo(hqLocation);

                if (distance < distToMarker) {
                    Intent intent = new Intent(getApplicationContext(), ARView.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", "locations for AR");
                    bundle.putDouble("lat", ARLatitude);
                    bundle.putDouble("lng", ARLongitude);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    System.out.println(distance);
                } else {
                    System.out.print(distance);
                }
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
        if(locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public void onResume() {
        super.onResume();
        if(locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
        }
    }
}
