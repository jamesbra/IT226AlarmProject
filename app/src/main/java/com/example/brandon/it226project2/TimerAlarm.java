package com.example.brandon.it226project2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
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

public class TimerAlarm extends AppCompatActivity
{
    Uri alert;
    Ringtone r;
    int numAlarms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_alarm);
        numAlarms = 0;
        final EditText mDay = (EditText) findViewById(R.id.setDay);
        final EditText mMin = (EditText) findViewById(R.id.setMin);
        final Button mMake = (Button) findViewById(R.id.startTimer);
        final EditText mMessage = (EditText) findViewById(R.id.customMessage);

        r = RingtoneManager.getRingtone(getApplicationContext(), alert);
        alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mMake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                makeAlarm(v, mDay, mMin, mMessage);
            }
        });
    }

    public void makeAlarm(final View v, EditText day, EditText min, EditText message)
    {
        final TextView mText = (TextView) findViewById(R.id.mText);
        long dayInput = Long.parseLong(day.getText().toString()) * 86400000;
        long minInput = Long.parseLong(min.getText().toString()) * 60000;
        final String title = message.getText().toString();
        final int notificationID = numAlarms;
        numAlarms++;
        final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setSmallIcon(R.drawable.notification_icon);

        Intent intent =  new Intent(getApplicationContext(), TimerAlarm.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);

        new CountDownTimer(dayInput + minInput, 1000)
        {
            public void onTick(long millisUntilFinished)
            {
                String format = new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished));
                mText.setText("Time Remaining: " + format);
                mBuilder.setContentTitle(title);
                mBuilder.setContentText("Time left: " + format);
                mNotificationManager.notify(notificationID, mBuilder.build());
            }

            public void onFinish()
            {
                mBuilder.setContentTitle(title);
                mBuilder.setContentText("Your timer has finished!");
                mNotificationManager.notify(notificationID, mBuilder.build());
                mText.setText("Done!");
                startNoise(v);
            }
        }.start();
    }

    public void startNoise(View v)
    {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
