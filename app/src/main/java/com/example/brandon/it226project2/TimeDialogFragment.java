package com.example.brandon.it226project2;

import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Dialog;
import java.util.Calendar;



public class TimeDialogFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));

    }

    public void onTimeSet(TimePicker view, int hour, int minute){
        String date = "Alarm Time:\n" + hour + ":" + minute;

    }

}
