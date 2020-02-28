package com.example.certification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MaincertifiActivity extends AppCompatActivity {

    Button[] category = new Button[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maincertifi);

        setTitle("자격증");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String[] cate = {"민간자격증", "국가기술자격증", "국가전문자격증"};

        category[0] = (Button) findViewById(R.id.civil);
        category[1] = (Button) findViewById(R.id.country_tech);
        category[2] = (Button) findViewById(R.id.country_pro);

        for(int i = 0; i < category.length; i++) {
            final int count = i;
            category[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DetailcertifiActivity.class);
                    intent.putExtra("detailcertifi", cate[count]);
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
