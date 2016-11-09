package com.example.brandon.it226project2;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class AlarmSoundService extends Service {
    MediaPlayer mplayer;
    public AlarmSoundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startID){

        mplayer = MediaPlayer.create(this, R.raw.yesss_1);
        mplayer.start();

        return START_NOT_STICKY;
    }
}
