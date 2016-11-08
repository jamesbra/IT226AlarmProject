package com.example.brandon.it226project2;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.DatePicker;
import java.util.Calendar;
import android.support.v4.app.FragmentManager;

public class createAlarm extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_alarm);
        Button confirm = (Button) findViewById(R.id.confirm_button);
        Button date = (Button) findViewById(R.id.date_button);
        date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                showDatePickerDialog(v);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                final TimePicker time = (TimePicker) findViewById(R.id.timePicker) ;
                int hour = time.getHour();
                int minute = time.getMinute();

            }
        });

    }

    public void showDatePickerDialog(View v){
        DialogFragment myFragment = new DateDialogFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        myFragment.show(ft,"DatePicker");
        //myFragment.show(getSupportFragmentManager(), "datePicker");
    }

}
