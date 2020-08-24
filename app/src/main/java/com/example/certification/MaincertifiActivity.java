package com.example.certification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class MaincertifiActivity extends AppCompatActivity {

    public static Activity MaincertifiActivity;
    MaterialCardView[] card = new MaterialCardView[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maincertifi);

        MaincertifiActivity = MaincertifiActivity.this;

        setTitle("자격증");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        card[0] = findViewById(R.id.tech_title);
        card[1] = findViewById(R.id.pro_title);
        card[2] = findViewById(R.id.civil_title);

        for(int i = 0; i < 3; i++) {
            final int temp = i;
            card[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MaincertifiActivity, DetailcertifiActivity.class);
                    switch (temp) {
                        case 0 :
                            intent.putExtra("category", "국가기술자격증");
                            startActivity(intent);
                            break;
                        case 1 :
                            intent.putExtra("category", "국가전문자격증");
                            startActivity(intent);
                            break;
                        case 2 :
                            intent.putExtra("category", "민간자격증");
                            startActivity(intent);
                            break;
                    }
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
