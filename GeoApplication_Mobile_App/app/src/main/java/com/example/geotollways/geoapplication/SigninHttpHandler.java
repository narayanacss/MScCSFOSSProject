package com.example.geotollways.geoapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class SigninHttpHandler extends AsyncTask<AsyncParamsGenerator, Void, Integer> {
    public static final String TAG2 = "Signin http post";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    Context applicationContext = SigninActivity.getContextOfApplication();

    protected Integer doInBackground(AsyncParamsGenerator... params){
        String json = params[0].json;
        String url = params[0].url;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Intent intent = new Intent(applicationContext, JsonIntentService.class);
            String responseString = response.body().string();
            intent.putExtra("responseString", responseString.toString());
            applicationContext.startService(intent);
            Log.i(TAG2, responseString);
            return response.code();
        } catch (IOException e) {
            Log.e(TAG2, e.getMessage());
        }
        return null;
    }
}


