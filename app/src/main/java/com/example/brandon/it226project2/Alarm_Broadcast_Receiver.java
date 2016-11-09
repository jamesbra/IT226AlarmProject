package com.example.brandon.it226project2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Alarm_Broadcast_Receiver extends BroadcastReceiver {
    public Alarm_Broadcast_Receiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service_intent = new Intent(context, AlarmSoundService.class);
        context.startService(service_intent);
    }
}
