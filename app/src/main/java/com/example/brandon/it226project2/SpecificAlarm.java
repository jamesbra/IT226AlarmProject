package com.example.brandon.it226project2;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.*;


//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;


import java.util.Calendar;

public class SpecificAlarm extends AppCompatActivity {
    private boolean reoccurring = false;
    private boolean timePicked = false;
    private boolean datePicked = false;
    private boolean alarmDateIsToday;
    private int daySelected;
    private int monthSelected;
    private int yearSelected;
    private int hourSelected;
    private int minuteSelected;
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=0;
    private String amPM = "";
    private AlarmManager alarm_manager;
    private PendingIntent pending_intent;
    EditText userAlarmText;
    private Context context;
    private LocationRequest locationRequest;
    Criteria criteria;
    LocationListener locationListener;
    Looper looper;

    TextView latitudeText;
    TextView longitudeText;

    TextView timeSelected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_alarm);
         userAlarmText = (EditText) findViewById(R.id.alarm_text);
        final Intent my_intent = new Intent(this, Alarm_Broadcast_Receiver.class);
        Button pickDateButton = (Button) findViewById(R.id.pick_date_button);
        Button pickTimeButton = (Button) findViewById(R.id.pick_time_button);
        Button confirmButton = (Button) findViewById(R.id.confirm_button);
        Button repeatButton = (Button) findViewById(R.id.repeat_button);
        latitudeText = (TextView) findViewById(R.id.user_latitude);
        longitudeText= (TextView) findViewById(R.id.user_longitude);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        looper = null;
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {



            } else {



                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);


            }
        }
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

         LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //Log.d("Location Changes", location.toString());
                latitudeText.setText(String.valueOf(location.getLatitude()));
                longitudeText.setText(String.valueOf(location.getLongitude()));
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

        final TextView repeatingTextView = (TextView) findViewById(R.id.repeat_text);
        final Calendar c = Calendar.getInstance();
        daySelected = c.get(Calendar.DAY_OF_MONTH);
        monthSelected = c.get(Calendar.MONTH);
        yearSelected = c.get(Calendar.YEAR);
        hourSelected = c.get(Calendar.HOUR_OF_DAY);
        minuteSelected = c.get(Calendar.MINUTE);
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
                //showDatePickerDialog(v);
            }
        });

        pickTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
                //showTimePickerDialog(v);
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Calendar current = Calendar.getInstance();
                if (alarmDateIsToday){
                    if (hourSelected <= current.get(Calendar.HOUR_OF_DAY) && minuteSelected <= current.get(Calendar.MINUTE)){
                        Toast.makeText(SpecificAlarm.this,"Time is in the past! Please select a time in the future.",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if (timePicked && datePicked) {
                    c.set(Calendar.MONTH,monthSelected);
                    c.set(Calendar.DAY_OF_MONTH,daySelected);
                    c.set(Calendar.YEAR,yearSelected);
                    c.set(Calendar.HOUR_OF_DAY,hourSelected);
                    c.set(Calendar.MINUTE,minuteSelected);
                    c.set(Calendar.SECOND,0);
                    c.set(Calendar.MILLISECOND,0);
                    pending_intent = PendingIntent.getBroadcast(SpecificAlarm.this,0,my_intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (reoccurring){
                        alarm_manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 60*1000,pending_intent);
                    }
                    else{
                        alarm_manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pending_intent);
                    }

                    String alarmText = userAlarmText.getText().toString();
                    doNotify();

                    finish();
                }
                else{
                    Toast.makeText(SpecificAlarm.this,"Please pick date/time",Toast.LENGTH_LONG).show();
                }

            }
        });
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                reoccurring = !reoccurring;
                String output;
                if (reoccurring){
                    output = "Alarm will repeat every day";
                }
                else{
                    output = "Alarm will not repeat every day";
                }
                repeatingTextView.setText(output);
            }
        });


    }

    @Override
    protected Dialog onCreateDialog(int id){
        if (id == 0){
            return new DatePickerDialog(this,datePickerListener, yearSelected,monthSelected,daySelected);
        }
        else
            return new TimePickerDialog(this,0,timePickerListener,hourSelected,minuteSelected, DateFormat.is24HourFormat(this));
    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar current = Calendar.getInstance();
            if (year == current.get(Calendar.YEAR) && month == current.get(Calendar.MONTH) && day == current.get(Calendar.DAY_OF_MONTH) ){
                alarmDateIsToday = true;
            }

            if (year < current.get(Calendar.YEAR) && month < current.get(Calendar.MONTH) && day < current.get(Calendar.DAY_OF_MONTH)) {
                Toast.makeText(SpecificAlarm.this, "Date in is the past! Please select new date.", Toast.LENGTH_LONG).show();
            }
            else{
                yearSelected = year;
                monthSelected = month;
                daySelected = day;
                TextView dateSelected = (TextView) findViewById(R.id.date_selected);
                String output = monthSelected + "-" + daySelected + "-" + yearSelected;
                datePicked = true;
                dateSelected.setText(output);
            }


        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute){
            hourSelected = hour;
            minuteSelected = minute;
            timeSelected = (TextView) findViewById(R.id.time_selected);
            String output;
            if (hourSelected > 12){
                amPM = "PM";
                if (minuteSelected < 10){
                    output = (hourSelected - 12) + ":0" + minuteSelected + " " + amPM ;
                }
                else{
                    output = (hourSelected - 12) + ":" + minuteSelected + " " + amPM ;
                }

            }
            else{
                amPM = "AM";
                if (minuteSelected < 10){
                    output = hourSelected + ":0" + minuteSelected + " " + amPM ;
                }
                else {
                    output = hourSelected + ":" + minuteSelected + " " + amPM;
                }
            }
            timePicked = true;
            timeSelected.setText(output);
        }
    };
    private void doNotify()
    {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent intent = PendingIntent.getActivity(this,100,new Intent(this, createAlarm.class),0);

        String format =timeSelected.getText() + " Latitude:" + latitudeText.getText().toString().substring(0,8) + " Longitude:" + longitudeText.getText().toString().substring(0,10);
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setSmallIcon(R.drawable.ic_speaker_dark);
        nb.setSound(sound);
        nb.setContentTitle(userAlarmText.getText().toString());
        nb.setContentText(format);
        nb.setContentIntent(intent);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(100, nb.build());
    }

}
