package com.example.geotollways.geoapplication;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

public class FailureTestIntentService extends IntentService {
    public static final String TAG4C = "TestService";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    String uid = null, sid = null, timestamp = "", fenceID = "", username = null; //Values to be included

    public FailureTestIntentService() {
        super(SuccessTestIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG4C, "FTService start");
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(getApplicationContext());
        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("")); //url to be included
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent2, 0);
        builder2.setContentIntent(pendingIntent);
        builder2.setSmallIcon(R.drawable.stamp);
        builder2.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.stamp));
        builder2.setContentTitle("geoTollways");
        builder2.setContentText("Crossing dummy toll fence.");
        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.alert);
        builder2.setSound(sound);
        NotificationManager notificationManager2 = (NotificationManager)getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        notificationManager2.notify(7, builder2.build());

        String [] postData = MenuActivity.getPostData();
        sid = postData[0];
        uid = postData[1];
        username = postData[3];
        Log.d(TAG4C, postData[0]);
        Log.d(TAG4C, postData[1]);
        Log.d(TAG4C, postData[3]);

        String url = ""; //url to be included
        String json = createJson();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            JSONObject responseObj = new JSONObject(responseString.toString());
            String  resp = responseObj.getString("response");
            int responseCode = response.code();
            String resCode = String.valueOf(responseCode);
            Log.i(TAG4C, responseString);
            Log.i(TAG4C, resp);
            Log.i(TAG4C, resCode);

            if (responseCode==200 && resp.equals("success")){
                onTriggerSuccess(getApplicationContext());
            }else {
                onTriggerFailed(getApplicationContext());
            }
        } catch(Exception e) {
            e.printStackTrace();
            onTriggerFailed(getApplicationContext());
        }

        if (MenuActivity.testPD2.isShowing()) {
            MenuActivity.testPD2.dismiss();
        }
        Log.d(TAG4C, "FTService stop");
    }

    public String createJson() {
        Log.d(TAG4C, "JSON created.");
        return "{\"uid\": \"" + uid + "\", \"sid\": \"" + sid + "\", \"timestamp\": \"" + timestamp + "\", \"fenceID\": \"" + fenceID + "\", \"agent\": \"" + "test" + "\", \"username\": \"" + username + "\"}";
    }

    public void onTriggerSuccess(Context context){
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("")); //url to be included
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder2.setContentIntent(pendingIntent);
        builder2.setSmallIcon(R.drawable.success);
        builder2.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.stamp));
        builder2.setContentTitle("geoTollways");
        builder2.setContentText("geoTolling successful!");
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.success);
        builder2.setSound(sound);
        NotificationManager notificationManager2 = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager2.notify(8, builder2.build());
    }

    public void onTriggerFailed(Context context){
        NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("")); //url to be included
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder2.setContentIntent(pendingIntent);
        builder2.setSmallIcon(R.drawable.failure);
        builder2.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.stamp));
        builder2.setContentTitle("geoTollways");
        builder2.setContentText("geoTolling failed!");
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.failure);
        builder2.setSound(sound);
        NotificationManager notificationManager2 = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager2.notify(9, builder2.build());
    }
}
