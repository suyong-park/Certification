package com.example.certification;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BookmarkActivity extends AppCompatActivity {

    private static final float FONT_SIZE = 10;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);

        container = (LinearLayout) findViewById(R.id.container);

        String key = PreferenceManager.getString_key(getApplicationContext(), "key");
        String text = PreferenceManager.getString(getApplicationContext(), key);
        TextView(text);
    }

    public void TextView(String data) {
        TextView text = new TextView(getApplicationContext());
        text.setText(data);
        text.setTextSize(FONT_SIZE);

        LinearLayout.LayoutParams linear_par = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linear_par.gravity = Gravity.CENTER;
        text.setLayoutParams(linear_par);
        container.addView(text);
    }
}
