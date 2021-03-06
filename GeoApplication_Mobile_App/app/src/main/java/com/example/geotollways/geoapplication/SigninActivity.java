package com.example.geotollways.geoapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;


public class SigninActivity extends AppCompatActivity {
    public static final String TAG1 = "LoginActivity";
    Button button;
    ProgressBar pb;
    EditText username, password;
    String user, pass;
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contextOfApplication = getApplicationContext();
        pb= (ProgressBar) findViewById(R.id.progressBar);
        button = (Button) findViewById(R.id.btnSignIn2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(View.VISIBLE);
                login();
            }
        });
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    public void login() {
        Log.d(TAG1, "Login");
        username = (EditText) findViewById(R.id.etUserName);
        password = (EditText) findViewById(R.id.etPass);
        user = username.getText().toString();
        pass = password.getText().toString();
        if (!validate()) {
            onValidateFailed();
            pb.setVisibility(View.INVISIBLE);
            return;
        }
        button.setEnabled(false);
        String url = ""; // url to be included
        String json = createJson();
        AsyncParamsGenerator params = new AsyncParamsGenerator(json, url);
        SigninHttpHandler shh = new SigninHttpHandler();
        try {
            int responseCode = shh.execute(params).get();
            String res = String.valueOf(responseCode);
            Log.i(TAG1, res);
            if (responseCode!=200){
                pb.setVisibility(View.INVISIBLE);
                onLoginFailed();
                return;
            }
            onLoginSuccess();
        }  catch(Exception e) {
            e.printStackTrace();
            pb.setVisibility(View.INVISIBLE);
            onHttpPostFailed();
            return;
        }
    }

    public boolean validate() {
        boolean valid = true;
        if (user.isEmpty()) {
            username.setError("Enter geo-username...");
            valid = false;
        } else {
            username.setError(null);
        }

        if (pass.isEmpty()) {
            password.setError("Enter geo-password...");
            valid = false;
        } else {
            password.setError(null);
        }
        Log.d(TAG1, "Login validation.");
        return valid;
    }

    public void onValidateFailed() {
        Toast.makeText(getBaseContext(), "Login failed!", Toast.LENGTH_LONG).show();
        button.setEnabled(true);
    }

    public String createJson() {
        Log.d(TAG1, "JSON created.");
        return "{\"username\": \"" + user + "\", \"password\": \"" + pass + "\"}";
    }

    public void onLoginSuccess() {
        Intent intent = new Intent(SigninActivity.this, MenuActivity.class);
        intent.putExtra("username", user);
        intent.putExtra("password", pass);
        SigninActivity.this.startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed! Incorrect username/password.", Toast.LENGTH_LONG).show();
        button.setEnabled(true);
    }

    public void onHttpPostFailed() {
        Toast.makeText(getBaseContext(), "Login failed! Try again.", Toast.LENGTH_LONG).show();
        button.setEnabled(true);
    }
}
