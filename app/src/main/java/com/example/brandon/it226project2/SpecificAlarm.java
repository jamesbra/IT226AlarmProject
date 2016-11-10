package com.example.brandon.it226project2;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.*;

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
    private String amPM = "";
    private AlarmManager alarm_manager;
    private PendingIntent pending_intent;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_alarm);

        final Intent my_intent = new Intent(this, Alarm_Broadcast_Receiver.class);
        Button pickDateButton = (Button) findViewById(R.id.pick_date_button);
        Button pickTimeButton = (Button) findViewById(R.id.pick_time_button);
        Button confirmButton = (Button) findViewById(R.id.confirm_button);
        Button repeatButton = (Button) findViewById(R.id.repeat_button);

        final EditText userAlarmText = (EditText) findViewById(R.id.alarm_text);
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
                    Toast.makeText(SpecificAlarm.this,"Alarm Added",Toast.LENGTH_LONG).show();
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
            return new TimePickerDialog(this,timePickerListener,hourSelected,minuteSelected, DateFormat.is24HourFormat(this));
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
            TextView timeSelected = (TextView) findViewById(R.id.time_selected);
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
//    public void showDatePickerDialog(View v){
//        DialogFragment myFragment = new DateDialogFragment();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        myFragment.show(ft,"DatePicker");
//
//        //myFragment.show(getSupportFragmentManager(), "datePicker");
//    }
//
//    public void showTimePickerDialog(View v){
//        DialogFragment myFragment = new TimeDialogFragment();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        myFragment.show(ft,"TimePicker");
//
//    }
}
