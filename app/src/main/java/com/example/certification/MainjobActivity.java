package com.example.certification;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainjobActivity extends AppCompatActivity {

    Button[] button = new Button[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainjob);

        setTitle("진출분야");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String[] cate = {"데이터", "교육", "개발", "법조계"};

        button[0] = (Button) findViewById(R.id.data);
        button[1] = (Button) findViewById(R.id.edu);
        button[2] = (Button) findViewById(R.id.develop);
        button[3] = (Button) findViewById(R.id.law);

        for(int i = 0; i < button.length; i++) {
            final int count = i;
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DetailjobActivity.class);
                    intent.putExtra("title", cate[count]);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // If touch the back key on tool bar, then finish present activity.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
