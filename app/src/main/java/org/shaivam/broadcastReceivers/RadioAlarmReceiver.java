package org.shaivam.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import org.shaivam.R;
import org.shaivam.utils.AppConfig;

public class RadioAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        try {

            String program = arg1.getStringExtra("program");

            AppConfig.createRadioNotification(arg0, "Radio Program Reminder!", program);
            Toast.makeText(arg0, AppConfig.getTextString(arg0, AppConfig.alarm_received), Toast.LENGTH_SHORT).show();

            MediaPlayer objPlayer;
            objPlayer = MediaPlayer.create(arg0, R.raw.bell);
            objPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
