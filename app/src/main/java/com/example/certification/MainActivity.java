package com.example.certification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button[] category = new Button[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] cate = {"민간자격증", "국가기술자격증", "국가전문자격증"};

        category[0] = (Button) findViewById(R.id.civil);
        category[1] = (Button) findViewById(R.id.country_tech);
        category[2] = (Button) findViewById(R.id.country_pro);

        for(int i = 0; i < category.length; i++) {
            final int count = i;
            category[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                    intent.putExtra("category", cate[count]);
                    startActivity(intent);
                }
            });
        }
    }
}
