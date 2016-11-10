package com.example.brandon.it226project2;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerAlarm extends AppCompatActivity {
    Uri alert;
    Ringtone r;
    int numAlarms;
    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lat = 0;
        lon = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_alarm);
        numAlarms = 0;
        final EditText mDay = (EditText) findViewById(R.id.setDay);
        final EditText mMin = (EditText) findViewById(R.id.setMin);
        final Button mMake = (Button) findViewById(R.id.startTimer);
        final EditText mMessage = (EditText) findViewById(R.id.customMessage);
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

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        5);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                lat = location.getLatitude();
                lon = location.getLongitude();
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
            public void onClick(View v) {
                makeAlarm(v, mDay, mMin, mMessage);
            }
        });
    }

    public void makeAlarm(final View v, EditText day, EditText min, EditText message) {
        final TextView mText = (TextView) findViewById(R.id.mText);
        long dayInput = Long.parseLong(day.getText().toString()) * 86400000;
        long minInput = Long.parseLong(min.getText().toString()) * 60000;
        final String title = message.getText().toString();
        final int notificationID = numAlarms;
        numAlarms++;
        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setSmallIcon(R.drawable.notification_icon);

        Intent intent = new Intent(getApplicationContext(), TimerAlarm.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        new CountDownTimer(dayInput + minInput, 1000) {
            public void onTick(long millisUntilFinished) {
                String format = new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished));
                mText.setText("Time Remaining: " + format);
                mBuilder.setContentTitle(title);
                mBuilder.setContentText("Time left: " + format + "   Location set at: " + lat + " " + lon);
                mNotificationManager.notify(notificationID, mBuilder.build());
            }

            public void onFinish() {
                mBuilder.setContentTitle(title);
                mBuilder.setContentText("Your timer has finished!");
                mNotificationManager.notify(notificationID, mBuilder.build());
                mText.setText("Done!");
                startNoise(v);
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