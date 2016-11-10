package com.example.brandon.it226project2;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SpecificAlarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_alarm);
        Button pickDateButton = (Button) findViewById(R.id.pick_date_button);
        Button pickTimeButton = (Button) findViewById(R.id.pick_time_button);
        pickDateButton.setOnClickListener(
                new View.OnClickListener()
                {
                @Override
                public void onClick(View v)
                {
                    showDatePickerDialog(v);
                }
                }
        );

        pickTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
    }

    public void showDatePickerDialog(View v){
        DialogFragment myFragment = new DateDialogFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        myFragment.show(ft,"DatePicker");
        //myFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v){
        DialogFragment myFragment = new TimeDialogFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        myFragment.show(ft,"TimePicker");

    }
}
