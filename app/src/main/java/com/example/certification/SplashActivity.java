package com.example.certification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        PreferenceManager.removeKey(getApplicationContext(), "checkIsFirst"); // for test !!!
        PreferenceManager.clear(getApplicationContext()); // for test !!!

        ImageView splash = (ImageView) findViewById(R.id.gif_splash);
        Glide.with(this).load(R.drawable.splash).into(splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(it);
                finish();
            }
        }, 1700);
    }
}