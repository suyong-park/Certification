package com.example.certification;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TransparentActivity extends AppCompatActivity {

    Button never, close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent);

        never = (Button) findViewById(R.id.never);
        close = (Button) findViewById(R.id.close);

        never.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setBoolean(getApplicationContext(), "never", true);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setBoolean(getApplicationContext(), "close", true);
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }
}
