package com.kreatidea.transmobile.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.kreatidea.transmobile.util.Constants;
import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.home.HomeActivity;
import com.kreatidea.transmobile.login.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    private int loadingTime = 2000;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getUserPreferences();
        isUserLoggedIn();
    }

    private void isUserLoggedIn() {
        if (userId.equals("")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                }
            }, loadingTime);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
                    finish();
                }
            }, loadingTime);
        }
    }

    private void getUserPreferences() {
        SharedPreferences preferences = getSharedPreferences(Constants.LOGIN_PREF, MODE_PRIVATE);
        userId = preferences.getString("user_id", "");
    }
}
