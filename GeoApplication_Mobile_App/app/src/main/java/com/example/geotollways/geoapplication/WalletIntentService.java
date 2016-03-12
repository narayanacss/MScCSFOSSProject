package com.example.geotollways.geoapplication;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

public class WalletIntentService extends IntentService {
    public static final String TAG5A = "WalletService";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    String uid = null, sid = null, timestamp = null;

    public WalletIntentService() {
        super(SuccessTestIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG5A, "WService start");

        String [] postData = MenuActivity.getPostData();
        sid = postData[0];
        uid = postData[1];
        timestamp = postData[2];
        Log.d(TAG5A, postData[0]);
        Log.d(TAG5A, postData[1]);
        Log.d(TAG5A, postData[2]);

        String url = ""; //server url to be included
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
            Log.i(TAG5A, responseString);
            Log.i(TAG5A, resp);
            Log.i(TAG5A, resCode);

            if (responseCode==200 && resp.equals("failure")){
                if (WalletActivity.testPD3.isShowing()) {
                    WalletActivity.testPD3.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Unable to retrieve the balance. Please try later.",
                        Toast.LENGTH_SHORT).show();
            }else {
                if (WalletActivity.testPD3.isShowing()) {
                    WalletActivity.testPD3.dismiss();
                }
                postData[4] = resp;
                MenuActivity.setPostData(postData);
            }
        } catch(Exception e) {
            e.printStackTrace();
            if (WalletActivity.testPD3.isShowing()) {
                WalletActivity.testPD3.dismiss();
            }
            Toast.makeText(getApplicationContext(), "Unable to retrieve the balance. Please try later.",
                    Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG5A, "WService stop");
    }

    public String createJson() {
        Log.d(TAG5A, "JSON created.");
        return "{\"uid\": \"" + uid + "\", \"sid\": \"" + sid + "\", \"timestamp\": \"" + timestamp + "\"}";
    }
}
