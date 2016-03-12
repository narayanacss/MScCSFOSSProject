package com.example.geotollways.geoapplication;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonIntentService extends IntentService {
    public static final String TAG2A = "Signin JSON";
    Context applicationContext = SigninActivity.getContextOfApplication();

    public JsonIntentService() {
        super(JsonIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG2A, "JService start");
        String [] postData = new String[5];
        String responseString = intent.getStringExtra("responseString");
        try {
            JSONObject responseObj = new JSONObject(responseString);
            postData[0] = responseObj.getString("sessid");
            JSONObject userObject  = responseObj.getJSONObject("user");
            postData[1] = userObject.getString("uid");
            postData[2] = userObject.getString("login");
            postData[3] = userObject.getString("name");
            JSONObject walletObject  = userObject.getJSONObject("geoWallet_balance");
            postData[4] = walletObject.getString("bal");
            Log.d(TAG2A, postData[0]);
            Log.d(TAG2A, postData[1]);
            Log.d(TAG2A, postData[2]);
            Log.d(TAG2A, postData[3]);
            Log.d(TAG2A, postData[4]);
        } catch (JSONException e) {
            Log.d(TAG2A, e.getMessage());
        }
        MenuActivity.setPostData(postData);
        Log.d(TAG2A, "JService stop");
    }
}
