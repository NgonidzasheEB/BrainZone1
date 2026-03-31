package com.brainzone.mindgames;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BatteryLowReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
            Toast.makeText(context, "Battery Low! Please charge your device to continue playing.", Toast.LENGTH_LONG).show();
            // In a real app, you might trigger a pause in the active activity.
        }
    }
}