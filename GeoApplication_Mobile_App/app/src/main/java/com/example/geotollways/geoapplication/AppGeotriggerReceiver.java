package com.example.geotollways.geoapplication;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.os.Bundle;
import android.location.Location;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.esri.android.geotrigger.GeotriggerBroadcastReceiver;

public class AppGeotriggerReceiver extends GeotriggerBroadcastReceiver {
    public static final String TAG4A = "geoTriggerReceiver";


    @Override
    protected void onPushMessage(Context context, Bundle notification) {
        Log.d(TAG4A, "geotrigger received");

        super.onPushMessage(context, notification);
        // The notification Bundle has these keys: 'text', 'url', 'sound', 'icon', 'data'
        //String msg = String.format("Push Message Received: %s", notification.get("data"));
        String msg = (String) notification.get("data");
        Log.i(TAG4A, msg);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.stamp);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.stamp));
        builder.setContentTitle("geoTollways");
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

        Intent intent2 = new Intent(context, AppGeotriggerReceiverIntentService.class);
        intent2.putExtra("msg", msg);
        context.startService(intent2);
    }

    @Override
    protected void onLocationUpdate(Context context, Location location, boolean isOnDemand) {
        // Called with the GeotriggerService, obtains a new location update from
        // Android's native location services. The isOnDemand parameter
        // determines if this location update was a result of calling
        // GeotriggerService.requestOnDemandUpdate()
        Toast.makeText(context, "Location update received!",
                Toast.LENGTH_SHORT).show();
    }

}
