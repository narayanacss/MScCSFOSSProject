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

import org.json.JSONException;
import org.json.JSONObject;

public class AppGeotriggerReceiverIntentService extends IntentService {
    public static final String TAG4B = "geoTriggerReceiverService";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    Context applicationContext = SigninActivity.getContextOfApplication();
    String uid = null, sid = null, timestamp = null, fenceID = null, username = null;

    public AppGeotriggerReceiverIntentService() {
        super(AppGeotriggerReceiverIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG4B, "AService start");

        String msg = intent.getStringExtra("msg");
        try {
            JSONObject msgObj = new JSONObject(msg);
            Log.i(TAG4B, msg);
            fenceID = msgObj.getString("fenceID");
            Log.i(TAG4B, fenceID);
        } catch (JSONException e) {
            Log.d(TAG4B, e.getMessage());
        }

        String [] postData = MenuActivity.getPostData();
        sid = postData[0];
        uid = postData[1];
        timestamp = postData[2];
        username = postData[3];
        Log.d(TAG4B, postData[0]);
        Log.d(TAG4B, postData[1]);
        Log.d(TAG4B, postData[2]);
        Log.d(TAG4B, postData[3]);

        String url = ""; //url to be included
        String json = createJson();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseString = response.body().string().toString();
            JSONObject responseObj = new JSONObject(responseString);
            String  resp = responseObj.getString("response");
            int responseCode = response.code();
            String resCode = String.valueOf(responseCode);
            Log.i(TAG4B, responseString);
            Log.i(TAG4B, resp);
            Log.i(TAG4B, resCode);

            if (responseCode==200 && resp.equals("success")){
                onTriggerSuccess(applicationContext);
            }else {
                onTriggerFailed(applicationContext);
            }
        } catch(Exception e) {
            e.printStackTrace();
            onTriggerFailed(applicationContext);
        }
        Log.d(TAG4B, "AService stop");
    }

    public String createJson() {
        Log.d(TAG4B, "JSON created.");
        return "{\"uid\": \"" + uid + "\", \"sid\": \"" + sid + "\", \"timestamp\": \"" + timestamp + "\", \"fenceID\": \"" + fenceID + "\", \"agent\": \"" + "okhttp/2.5.0" + "\", \"username\": \"" + username + "\"}";
    }

    public void onTriggerSuccess(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("")); //url to be included
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.success);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.stamp));
        builder.setContentTitle("geoTollways");
        builder.setContentText("geoTolling successful!");
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.success);
        builder.setSound(sound);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(2, builder.build());
    }

    public void onTriggerFailed(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("")); //url to be included
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.failure);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.stamp));
        builder.setContentTitle("geoTollways");
        builder.setContentText("geoTolling failed!");
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.failure);
        builder.setSound(sound);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(3, builder.build());
    }
}
