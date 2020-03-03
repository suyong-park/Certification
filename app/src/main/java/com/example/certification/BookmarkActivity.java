package com.example.certification;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookmarkActivity extends AppCompatActivity {

    private static final float FONT_SIZE = 10;
    LinearLayout container;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);

        String text, value;
        int max;

        container = (LinearLayout) findViewById(R.id.container);
        delete = (Button) findViewById(R.id.delete);

        try {
            value = PreferenceManager.getString_max(getApplicationContext(), "value"); // max value
            max = Integer.parseInt(value);

            for(int i = 1; i <= max; i++) {
                String temp = String.valueOf(i);
                text = PreferenceManager.getString(getApplicationContext(), temp);
                if(!text.equals(""))
                {
                    TextView(text);
                }
            }
        }
        catch(NumberFormatException e) {
            TextView("북마크가 없어용");
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 자동 새로고침 기능 추가해야함.
                PreferenceManager.clear(getApplicationContext());
                Toast.makeText(getApplicationContext(), "북마크가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
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
