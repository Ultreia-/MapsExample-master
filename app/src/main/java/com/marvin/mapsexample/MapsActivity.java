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

    public double latitude;
    public double longitude;
    public double hqLat;
    public double hqLong;
    public float distance;
    public Location hqLocation;


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
            tvLocation.setText("Welcome, " + IntroScreen.username);

            latitude = location.getLatitude();
            longitude = location.getLongitude();

            LatLng latLng = new LatLng(latitude, longitude);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));


            if(location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 1500, 0, this);
        }
        Intent i = getIntent();
        if(i != null) {
            String id = i.getExtras().getString("id");
            if(id.equals("marker for S2")) {
                double lat = i.getExtras().getDouble("lat");
                double lng = i.getExtras().getDouble("lng");
                String title = i.getExtras().getString("id");
                String snippet = i.getExtras().getString("snippet");

                addMarkerToMap(lat, lng, title, snippet);
            }
        }

        addTestMarkerToMap();

    }

    private void addTestMarkerToMap() {
        LatLng pos = new LatLng(56.172809, 10.186461);
        hqLocation = new Location("Test");
        hqLocation.setLatitude(56.172809);
        hqLocation.setLongitude(10.186461);

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

        if(hqLocation != null) {
            distance = location.distanceTo(hqLocation);

            if (distance < 10) {
                System.out.println(distance);
                Intent intent = new Intent(getApplicationContext(), ARView.class);
                Bundle b = new Bundle();
                b.putString("id", "close enough");
                b.putDouble("lat", latitude);
                b.putDouble("lng", longitude);
                intent.putExtras(b);
                startActivity(intent);
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
}
