package com.example.geotollways.geoapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class MenuHttpHandler extends AsyncTask<AsyncParamsGenerator, Void, Integer> {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

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
            Log.i("Menu http", response.body().string());
            return response.code();
        } catch (IOException e) {
            Log.d("Menu http", e.getMessage());
        }
        return null;
    }
}



