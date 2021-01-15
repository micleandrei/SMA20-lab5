package com.example.broadcastreceivers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;

public class PowerConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
        String chargingStatus;
        if(isCharging){
            chargingStatus = "Charging";
        }
        else{
            chargingStatus = "Not charging";
        }

        Intent newIntent = new Intent(context, MainActivity.class);
        newIntent.putExtra("status", isCharging);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, newIntent,0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "id_channel")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Charging status changed!")
                .setContentText(chargingStatus)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, mBuilder.build());




    }
}