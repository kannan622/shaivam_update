package org.shaivam.broadcastReceivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import org.shaivam.R;
import org.shaivam.utils.AppConfig;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        try {

            String requestcode = arg1.getStringExtra("requestcode");
            String message = "";
            if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_6_Am_hours))) {
                message = arg0.getResources().getString(R.string.ushat);
            } else if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_8_Am_hours))) {
                message = arg0.getResources().getString(R.string.kaala);
            } else if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_12_Pm_hours))) {
                message = arg0.getResources().getString(R.string.uchi);
            } else if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_6_Pm_hours))) {
                message = arg0.getResources().getString(R.string.saya);
            } else if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_8_Pm_hours))) {
                message = arg0.getResources().getString(R.string.iranda);
            } else if (requestcode.equalsIgnoreCase(String.valueOf(AppConfig.Six_Pooja_9_Pm_hours))) {
                message = arg0.getResources().getString(R.string.ardha);
            } else
                message = "Connect to Internet to play song!!";
            AppConfig.createNotificationPooja(arg0, requestcode, "Shaivam.org Alarm!", message);
            Toast.makeText(arg0, AppConfig.getTextString(arg0, AppConfig.alarm_received), Toast.LENGTH_SHORT).show();

            MediaPlayer objPlayer;
            objPlayer = MediaPlayer.create(arg0, R.raw.bell);
            objPlayer.start();

            PendingIntent pendingIntent = PendingIntent.getBroadcast(arg0.getApplicationContext(),
                0, arg1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) arg0.getApplicationContext().getSystemService
                (Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
