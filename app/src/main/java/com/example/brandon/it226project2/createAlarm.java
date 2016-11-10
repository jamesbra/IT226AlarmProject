package com.example.brandon.it226project2;

import android.app.*;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.Calendar;

public class createAlarm extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);

        Button timerAlarmButton = (Button) findViewById(R.id.timer_alarm_button);
        Button specificAlarmButton = (Button) findViewById(R.id.specific_alarm_button);
        Button location = (Button) findViewById(R.id.location_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        //buildCurrentDate();
        location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent locationAlarmIntent = new Intent(createAlarm.this,LocationAlarm.class);
                startActivity(locationAlarmIntent);
            }
        });
        specificAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent specificAlarmIntent = new Intent(createAlarm.this,SpecificAlarm.class);
                startActivity(specificAlarmIntent);
                //showDatePickerDialog(v);
            }
        });
        timerAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final TimePicker time = (TimePicker) findViewById(R.id.timePicker);
                //int hour = time.getHour();
                //int minute = time.getMinute();
                Intent timerAlarmIntent = new Intent(createAlarm.this,TimerAlarm.class);
                startActivity(timerAlarmIntent);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent (createAlarm.this, AlarmSoundService.class);
                PendingIntent sender = PendingIntent.getBroadcast(createAlarm.this,0,intent,0);
                AlarmManager alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarm_manager.cancel(sender);
            }
        });

    }


    public void buildCurrentDate(){
        //Calendar c = Calendar.getInstance();
        //String date = "Alarm date:\n" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.YEAR);
        //TextView currentDateText = (TextView) findViewById(R.id.current_date_selected);
        //currentDateText.setText(date);
    }
}
