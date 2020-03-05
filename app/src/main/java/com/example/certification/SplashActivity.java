package com.example.certification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setBoolean(getApplicationContext(), "close", false);
        PreferenceManager.clear(getApplicationContext()); // for test!!!!

        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
        finish();
    }
}