package com.example.certification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
// TODO : 앱 버전 플레이스토어에서 확인한 후 구 버전인 경우 업데이트 하도록 권고.

    NotificationManager manager;
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";

    EditText editText;
    DrawerLayout drawer;
    LinearLayout container;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // If the keyboard blinding other components, then components are go up the keyboard.

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        container = findViewById(R.id.container);
        editText = findViewById(R.id.editText);

        setSupportActionBar(toolbar);

        container.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               downKeyboard();
           }
        });

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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                downKeyboard();
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View navigation = navigationView.getHeaderView(0);
        TextView version = (TextView) navigation.findViewById(R.id.version);
        version.setText("version : " + getVersionInfo(getApplicationContext()));

        Switch push = (Switch) navigation.findViewById(R.id.switch_push);
        boolean state = PreferenceManager.getBoolean(getApplicationContext(),"switch");
        if(state)
            push.setChecked(true);
        else if(!state)
            push.setChecked(false);
        push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    PreferenceManager.setBoolean(getApplicationContext(), "switch", isChecked);
                    Snackbar.make(navigationView, "푸시알림 허용하셨습니다.", Snackbar.LENGTH_SHORT).show();
                    showNotice();
                }
                else {
                    PreferenceManager.setBoolean(getApplicationContext(), "switch", isChecked);
                    Snackbar.make(navigationView, "푸시알림 거부하셨습니다.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            Snackbar.make(drawer, "정말 그만볼꺼에요?", Snackbar.LENGTH_SHORT)
                    .setAction("넹", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .setDuration(1700)
                    .show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.certification) {
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
        else if(id == R.id.calendar_pop) {
            Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.web) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.naver.com"));
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

    public void showNotice() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(CHANNEL_ID) == null)
                manager.createNotificationChannel(new NotificationChannel(
                        CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                ));
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        }
        else
            builder = new NotificationCompat.Builder(getApplicationContext());

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle("Basic Push Message")
                .setContentText("푸시알림에 동의하셨습니다.")
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        Notification noti = builder.build();
        manager.notify(2, noti);
    }

    public void downKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public String getVersionInfo(Context context) {
        // Get App version
        String version = "Unknown";
        PackageInfo packageInfo;

        if (context == null) {
            return version;
        }
        try {
            packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getApplicationContext().getPackageName(), 0 );
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
