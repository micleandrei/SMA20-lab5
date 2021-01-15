package com.example.broadcastreceivers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView chargingStatusTextView;
    private PowerConnectionReceiver batteryStatusReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        receiveBatteryInfo();

    }

    public void receiveBatteryInfo(){
        batteryStatusReceiver = new PowerConnectionReceiver();
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, iFilter);

        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
        boolean charging = status == BatteryManager.BATTERY_STATUS_CHARGING;
        boolean full = status == BatteryManager.BATTERY_STATUS_FULL;

        chargingStatusTextView = (TextView)findViewById(R.id.chargingStatus);
        String statusText = "";
        if(charging){
            if(usbCharge){
                statusText = "Phone is charging via USB";
            }
            else if(acCharge){
                statusText = "Phone is charging via AC";
            }
        }
        else if(full){
            statusText = "Phone has full battery";
        }
        else{
            statusText = "Phone is not charging";
        }
        chargingStatusTextView.setText(statusText);

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "id_channel";
            String description = "description_channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("id_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(batteryStatusReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(batteryStatusReceiver);
    }
}