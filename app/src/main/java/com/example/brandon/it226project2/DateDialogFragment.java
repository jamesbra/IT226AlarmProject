package com.example.brandon.it226project2;

import android.widget.DatePicker;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.app.Dialog;
import java.util.Calendar;



public class DateDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    public Dialog onCreateDialog(Bundle savedInstanceState){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, year, month, day);

    }

    
    public void onDateSet(DatePicker view, int year, int month, int day){
        String date = "Alarm date:\n" + month + "-" + day + "-" + year;

    }

}
