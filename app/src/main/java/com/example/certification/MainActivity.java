package com.example.certification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity  extends AppCompatActivity {

    Button mail, job, certification, bookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!PreferenceManager.getBoolean(getApplicationContext(), "never") && !PreferenceManager.getBoolean(getApplicationContext(), "close")) {
            Intent intent = new Intent(getApplicationContext(), TransparentActivity.class);
            startActivity(intent);
        }

        mail = (Button) findViewById(R.id.developer_mail);
        job = (Button) findViewById(R.id.job);
        certification = (Button) findViewById(R.id.certification);
        bookmark = (Button) findViewById(R.id.bookmark);

        job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainjobActivity.class);
                startActivity(intent);
            }
        });

        certification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MaincertifiActivity.class);
                startActivity(intent);
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/Text");
                String[] address = {"spdlqjfire@gmail.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT,"<자격증>개발자에게 보내는 조언 및 건의");
                email.putExtra(Intent.EXTRA_TEXT,"**불편사항 혹은 건의사항의 경우 앱 버전과 안드로이드 기기 종류를 함께 보내주시면 감사하겠습니다.\n앱 버전: \n기기명: ");
                startActivity(email);
            }
        });

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : Fragment로 설계 변경할 수 있는지 확인
                Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
                startActivity(intent);
            }
        });
    }
}
