package com.example.geotollways.geoapplication;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WalletActivity extends AppCompatActivity {
    public static final String TAG5 = "WalletActivity";
    String [] postData = MenuActivity.getPostData();
    TextView tv, tv2;
    String bal = null;
    public static ProgressDialog testPD3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv = (TextView)findViewById(R.id.textView);
        tv2 = (TextView)findViewById(R.id.textView3);
        bal = postData[4];
        Log.d(TAG5, postData[4]);
        tv.setText(bal);

        ImageView img = (ImageView) findViewById(R.id.imageView13);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG5, "Back button clicked.");
                WalletActivity.this.finish();
            }
        });

        ImageView img2 = (ImageView) findViewById(R.id.imageView15);
        img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG5, "Refresh button clicked.");
                testPD3 = ProgressDialog.show(WalletActivity.this, "", "Processing...");
                Intent intent = new Intent(WalletActivity.this, WalletIntentService.class);
                startService(intent);
                bal = postData[4];
                tv.setText(bal);
                tv2.setText("Current balance:");
            }
        });
    }

}
