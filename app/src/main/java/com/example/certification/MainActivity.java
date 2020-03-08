package com.example.certification;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    EditText editText;

    DrawerLayout drawer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // If the keyboard blinding other components, then components are go up the keyboard.

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        editText = findViewById(R.id.editText);

        setSupportActionBar(toolbar);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH: // Search
                        Toast.makeText(getApplicationContext(), "검색창 눌렀넹", Toast.LENGTH_SHORT).show();
                        break;
                    default: // Blocking Press Enter
                        return false;
                }
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.develop) {
            Intent intent = new Intent(getApplicationContext(), TransparentActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.certification) {
            Intent intent = new Intent(getApplicationContext(), MaincertifiActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.job) {
            Intent intent = new Intent(getApplicationContext(), MainjobActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.bookmark) {
            Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.share) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "안녕 !! 이건 공유하기");
            intent.putExtra(Intent.EXTRA_TEXT, "이런 앱이 있당!!");
            Intent chooser = Intent.createChooser(intent, "공유");
            startActivity(chooser);
        }
        else if(id == R.id.send) {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("plain/Text");
            String[] address = {"spdlqjfire@gmail.com"};
            email.putExtra(Intent.EXTRA_EMAIL, address);
            email.putExtra(Intent.EXTRA_SUBJECT,"<자격증>개발자에게 보내는 조언 및 건의");
            email.putExtra(Intent.EXTRA_TEXT,"**불편사항 혹은 건의사항의 경우 앱 버전과 안드로이드 기기 종류를 함께 보내주시면 감사하겠습니다.\n앱 버전: \n기기명: ");
            startActivity(email);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
