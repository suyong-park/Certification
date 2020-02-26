package com.example.certification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button country_pro; // 국가전문자격
    Button country_tech; // 국가기술자격
    Button civil; // 민간자격

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        country_pro = (Button) findViewById(R.id.country_pro);
        country_tech = (Button) findViewById(R.id.country_tech);
        civil = (Button) findViewById(R.id.civil);

        country_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CountryProActivity.class);
                startActivity(intent);
            }
        });

        country_tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CountryTechActivity.class);
                startActivity(intent);
            }
        });

        civil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CivilActivity.class);
                startActivity(intent);
            }
        });
    }
}
