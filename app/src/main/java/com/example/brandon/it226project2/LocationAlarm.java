package com.example.brandon.it226project2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Math.abs;


public class LocationAlarm extends AppCompatActivity
{
    Uri alert;
    Ringtone r;
    int numAlarms;
    double oldLat;
    double oldLon;
    double newLat;
    double newLon;
    private Button mMake;
    protected void onCreate(Bundle savedInstanceState) {
        oldLat = 0;
        oldLon = 0;
        newLat = 0;
        newLon = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_alarm);
        numAlarms = 0;

        final EditText mMin = (EditText) findViewById(R.id.min_move);
        mMake = (Button) findViewById(R.id.start_time);

        r = RingtoneManager.getRingtone(getApplicationContext(), alert);
        alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        Looper looper = null;

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            }
            else
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        5);

            }
        }
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                newLat = location.getLatitude();
                newLon = location.getLongitude();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                //Log.d("Status Changed", String.valueOf(status));
            }

            public void onProviderEnabled(String provider) {
                //Log.d("Provider Enabled", provider);
            }

            public void onProviderDisabled(String provider) {
                //Log.d("Provider Disabled", provider);
            }
        };
        locationManager.requestSingleUpdate(criteria, locationListener, looper);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        mMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String help = mMin.getText().toString();
                makeAlarm(v, help, oldLat, oldLon);
            }
        });
    }

    public void makeAlarm(final View v, String min, double lat, double lon) {
        final TextView mText = (TextView) findViewById(R.id.newText);
        long minInput = Long.parseLong(min) * 6000;
        final int notificationID = numAlarms;
        final String minHelp = min;
        numAlarms++;
        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setSmallIcon(R.drawable.notification_icon);
        Intent intent = new Intent(getApplicationContext(), LocationAlarm.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        oldLat = newLat;
        oldLon = newLon;
        new CountDownTimer(minInput, 1000) {
            public void onTick(long millisUntilFinished) {
                String format = new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished));
                mText.setText("Time Remaining: " + format);
                mBuilder.setContentTitle("Keep moving!");
                mBuilder.setContentText("Time left: " + format);
                mNotificationManager.notify(notificationID, mBuilder.build());
            }

            public void onFinish() {
                if(abs(oldLon - newLon) < 1 || abs(oldLat - newLat) < 1)
                {
                    mBuilder.setContentTitle("Get up and walk!");
                    mBuilder.setContentText("Get moving");
                    mNotificationManager.notify(notificationID, mBuilder.build());
                    mText.setText("Move!");
                    startNoise(v);
                }
                else
                {
                    oldLon = newLon;
                    oldLat = newLat;
                    makeAlarm(v, minHelp, newLat, newLon);
                    cancel();
                }

            }
        }.start();


    }

    public void updateLocation()
    {

    }

    public void startNoise(View v) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

