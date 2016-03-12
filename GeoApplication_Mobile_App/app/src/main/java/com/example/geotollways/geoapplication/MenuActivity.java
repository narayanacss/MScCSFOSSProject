package com.example.geotollways.geoapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.File;

import com.esri.android.geotrigger.GeotriggerService;

public class MenuActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    public static final String TAG3 = "MenuActivity";
    Switch geoSwitch;
    String user, pass;
    CoordinatorLayout cLayout;
    public static Context contextOfApplication;
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
    Button button, button2;
    public static ProgressDialog testPD, testPD2;

    private static String[] postData = new String[5];

    public static String[] getPostData() {
        return postData;
    }

    public static void setPostData(String[] postData) {
        MenuActivity.postData = postData;
    }

    public static final String TAG4 = "geoTrigger";
    // Client ID from https://developers.arcgis.com/en/applications
    private static final String AGO_CLIENT_ID = "bDelcHJFwWZ5ijDj";
    // The project number from https://cloud.google.com/console
    private static final String GCM_SENDER_ID = ""; //To be included
    // A list of initial tags to apply to the device.
    // Triggers created on the server for this application, with at least one of these same tags,
    // will be active for the device.
    private static final String[] TAGS = new String[] {""}; //To be included

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        geoSwitch = (Switch) findViewById(R.id.switch1);
        geoSwitch.setOnCheckedChangeListener(this);
        cLayout = (CoordinatorLayout)findViewById(R.id.coordLay);

        ImageView img4 = (ImageView) findViewById(R.id.imageView10);
        img4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG3, "gW image clicked.");
                Intent intent = new Intent(MenuActivity.this, WalletActivity.class);
                MenuActivity.this.startActivity(intent);
            }
        });

        ImageView img = (ImageView) findViewById(R.id.imageView7);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG3, "gT image clicked.");
                Uri uri = Uri.parse(""); //To be included
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        ImageView img2 = (ImageView) findViewById(R.id.imageView8);
        img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG3, "Maps image clicked.");
                Uri uri2 = Uri.parse("http://www.nhtis.org/map.htm#");
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
                startActivity(intent2);
            }
        });

        ImageView img3 = (ImageView) findViewById(R.id.imageView9);
        img3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG3, "Logout image clicked");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MenuActivity.this);
                alertDialog.setTitle("Confirm Logout");
                alertDialog.setMessage("Are you sure you want to logout?");
                alertDialog.setIcon(R.drawable.logout);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ProgressDialog.show(MenuActivity.this, "", "Please wait");
                        logout();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });

        button = (Button) findViewById(R.id.btnTest);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG3, "Success Test button clicked.");
                button.setEnabled(false);
                testPD = ProgressDialog.show(MenuActivity.this, "", "Processing...");
                Intent intent = new Intent(MenuActivity.this, SuccessTestIntentService.class);
                startService(intent);
                button.setEnabled(true);
            }
        });


        button2 = (Button) findViewById(R.id.btnTest2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG3, "Failure Test button clicked.");
                button2.setEnabled(false);
                testPD2 = ProgressDialog.show(MenuActivity.this, "", "Processing...");
                Intent intent = new Intent(MenuActivity.this, FailureTestIntentService.class);
                startService(intent);
                button2.setEnabled(true);
            }
        });

    }

    public void logout() {
        Intent intent3 = getIntent();
        user = intent3.getStringExtra("username");
        pass = intent3.getStringExtra("password");

        String url = ""; //url to be included
        String json = createJson();
        AsyncParamsGenerator params = new AsyncParamsGenerator(json, url);
        MenuHttpHandler mhh = new MenuHttpHandler();
        try {
            int responseCode = mhh.execute(params).get();
            String res = String.valueOf(responseCode);
            Log.i(TAG3, res);
            if (responseCode!=200){
                onLogoutSuccess();
            }else {
            onLogoutFailed();
            }
        } catch(Exception e) {
            e.printStackTrace();
            onLogoutFailed();
            return;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            GeotriggerHelper.startGeotriggerService(MenuActivity.this, AGO_CLIENT_ID, GCM_SENDER_ID, TAGS,
                    GeotriggerService.TRACKING_PROFILE_FINE);
            Log.d(TAG4, "geotrigger service started");
        } else {
            // Turn off tracking, but leave the service around for further instructions
            GeotriggerService.setTrackingProfile(this, GeotriggerService.TRACKING_PROFILE_OFF);
            Snackbar.make(cLayout, "geo-tracking switched off.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            Log.d(TAG4, "geotrigger service terminated");
        }
    }

    public String createJson() {
        Log.d(TAG3, "JSON created.");
        return "{\"username\": \"" + user + "\", \"password\": \"" + pass + "\"}";
    }

    public void onLogoutSuccess() {
        GeotriggerService.setTrackingProfile(this, GeotriggerService.TRACKING_PROFILE_OFF);
        Intent intent = new Intent(MenuActivity.this, SigninActivity.class);
        MenuActivity.this.startActivity(intent);
    }

    public void onLogoutFailed() {
        Snackbar.make(cLayout, "Logout failed! Try again.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("lib")){
                    deleteDir(new File(appDir, s));
                    Log.d(TAG3, "App data deleted.");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

}
