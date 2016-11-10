package com.example.brandon.it226project2;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LocationAlarm extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    TextView latitudeText;
    TextView longitudeText;

    private FusedLocationProviderApi locationProvider = LocationServices.FusedLocationApi;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Double myLatitude;
    private Double myLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_alarm);

        latitudeText = (TextView) findViewById(R.id.tvLatitude);
        longitudeText = (TextView) findViewById(R.id.tvLongitude);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(60 * 1000);
        locationRequest.setFastestInterval(15 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        requestLocationUpdates();
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        myLatitude = location.getLatitude();
        myLongitude = location.getLongitude();
        latitudeText.setText("Latitude is : " + String.valueOf(myLatitude));
        longitudeText.setText("Longitude is : " + String.valueOf(myLongitude));
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()){
            requestLocationUpdates();
        }
    }
}

