package com.example.certification;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
// TODO : 앱 버전 플레이스토어에서 확인한 후 구 버전인 경우 업데이트 하도록 권고.

    private long onBackPressedTime = 0;
    private boolean isFabOpen = false;
    private Animation fab_open, fab_close;
    private FloatingActionButton[] fab = new FloatingActionButton[5];

    MainActivity mainActivity;
    TextInputLayout text_layout;
    TextInputEditText editText;
    DrawerLayout drawer;
    BottomAppBar bottomAppBar;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = MainActivity.this;

        boolean isFirst = PreferenceManager.getBoolean(mainActivity, "checkIsFirst");
        if(!isFirst) {
            PreferenceManager.setBoolean(mainActivity, "checkIsFirst", true);

            Intent intent = new Intent(mainActivity, TutorialActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // If the keyboard blinding other components, then components are go up the keyboard.

        Broadcast.HashMap();
        View view = findViewById(R.id.drawer_layout);

        text_layout = findViewById(R.id.text_layout);
        drawer = findViewById(R.id.drawer_layout);
        editText = findViewById(R.id.editText);
        bottomAppBar = findViewById(R.id.bottom_app_bar);

        for(int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    fab[0] = findViewById(R.id.fab_main);
                case 1:
                    fab[1] = findViewById(R.id.fab_calendar);
                case 2:
                    fab[2] = findViewById(R.id.fab_bookmark);
                case 3:
                    fab[3] = findViewById(R.id.fab_company);
                case 4:
                    fab[4] = findViewById(R.id.fab_certification);
            }
        }

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        for(int i = 0; i < 5; i++) {
            final int count = i;
            fab[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isOpenFab();
                    Intent intent;
                    switch (count) {
                        case 1:
                            intent = new Intent(mainActivity, CalendarActivity.class);
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(mainActivity, BookmarkActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(mainActivity, MainjobActivity.class);
                            startActivity(intent);
                            break;
                        case 4:
                            intent = new Intent(mainActivity, MaincertifiActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
            });
        }

        String[] title = {"검색항목 선택", "자격증", "직업"};
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.addAll(title);
        spinner.setAdapter(adapter);

        bottomAppBar.setTitle(R.string.app_name);
        setSupportActionBar(bottomAppBar);

        final EditText edit = text_layout.getEditText();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isFabOpen) {
                    fab[0].setImageResource(R.drawable.icon_unfold_more);
                    for (int i = 1; i < 5; i++) {
                        fab[i].startAnimation(fab_close);
                        fab[i].setClickable(false);
                    }
                    isFabOpen = false;
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN) { // Keyboard down if user touch the screen.
                    downKeyboard();
                    edit.clearFocus();
                    return true;
                }
                return false;
            }
        });

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")) {
                    text_layout.setError("특수문자는 검색할 수 없습니다.");
                    return ;
                }
                else
                    text_layout.setError(null); // null used to delete message.
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH: // Search
                        downKeyboard();
                        if(spinner.getSelectedItem().toString().equals("검색항목 선택")) {
                            Snackbar.make(drawer, "검색 항목을 선택하세요.", Snackbar.LENGTH_SHORT).show();
                            return false;
                        }
                        if(editText.getText().length() == 0) {
                            Snackbar.make(drawer, "검색 내용을 입력하세요.", Snackbar.LENGTH_SHORT).show();
                            return false;
                        }
                        if(editText.getText().length() < 10) {
                            Broadcast.isNetworkWorking(mainActivity);
                            searchResult();
                        }
                        break;
                    default: // Blocking Press Enter
                        return false;
                }
                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(mainActivity, drawer, bottomAppBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) { // Open Shortcut menu.
                super.onDrawerOpened(drawerView);
                if(isFabOpen) {
                    fab[0].setImageResource(R.drawable.icon_unfold_more);
                    for(int i = 1; i < 5; i++) {
                        fab[i].startAnimation(fab_close);
                        fab[i].setClickable(false);
                    }
                    isFabOpen = false;
                }
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View navigation = navigationView.getHeaderView(0);
        TextView version = (TextView) navigation.findViewById(R.id.version);
        version.setText("version : " + getVersionInfo(mainActivity));
    }

    public void isOpenFab() {
        if(isFabOpen) {
            fab[0].setImageResource(R.drawable.icon_unfold_more);
            for(int i = 1; i < 5; i++) {
                fab[i].startAnimation(fab_close);
                fab[i].setClickable(false);
            }
            isFabOpen = false;
        }
        else {
            fab[0].setImageResource(R.drawable.icon_unfold_less);
            for(int i = 1; i < 5; i++) {
                fab[i].startAnimation(fab_open);
                fab[i].setClickable(true);
            }
            isFabOpen = true;
        }
    }

    public void searchResult() {

        final String search_word = editText.getText().toString().trim(); // To searching word
        final String search_category = spinner.getSelectedItem().toString(); // Category of Search_word

        ConnectDB connectDB = Broadcast.getRetrofit().create(ConnectDB.class);
        Call<List<Recycler_onething>> call = connectDB.search(Broadcast.Filtering(search_category, search_word));

        call.enqueue(new Callback<List<Recycler_onething>>() {
            @Override
            public void onResponse(Call<List<Recycler_onething>> call, Response<List<Recycler_onething>> response) {
                List<Recycler_onething> result = response.body();
                String[] title_name = new String[result.size()];

                if(result != null) {
                    if (result.size() != 0) {
                        for (int i = 0; i < result.size(); i++)
                            title_name[i] = result.get(i).getTitle();
                        Intent intent = new Intent(mainActivity, SearchActivity.class);
                        intent.putExtra("title_name", title_name);
                        intent.putExtra("category", search_category);
                        intent.putExtra("search_word", search_word);
                        startActivity(intent);
                    }
                    else {
                        Broadcast.AlertBuild(mainActivity, "메시지", search_word + " 와(과) 관련된 검색 결과가 없습니다.")
                        .setPositiveButton("확인", null)
                        .show();
                        return ;
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Recycler_onething>> call, Throwable t) {
                Log.d("ERROR MESSAGE", "CONNECT FAIL TO SERVER");
                Broadcast.AlertBuild(mainActivity, "에러", "서버 연결에 실패했습니다.")
                        .setPositiveButton("확인", null)
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            if(onBackPressedTime + 2000 < System.currentTimeMillis()) {
                onBackPressedTime = System.currentTimeMillis();
                Snackbar.make(drawer, "정말 나가시겠습니까?", Snackbar.LENGTH_SHORT)
                        .setAction("네", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        })
                        .show();
                return ;
            }
            if(onBackPressedTime + 1500 >= System.currentTimeMillis())
                finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent, title;
        switch (item.getItemId()) {
            case R.id.certification :
                intent = new Intent(mainActivity, MaincertifiActivity.class);
                startActivity(intent);
                break;
            case R.id.job :
                intent = new Intent(mainActivity, MainjobActivity.class);
                startActivity(intent);
                break;
            case R.id.bookmark :
                intent = new Intent(mainActivity, BookmarkActivity.class);
                startActivity(intent);
                break;
            case R.id.calendar_pop :
                intent = new Intent(mainActivity, CalendarActivity.class);
                startActivity(intent);
                break;
            case R.id.push :
                intent = new Intent(mainActivity, PushActivity.class);
                startActivity(intent);
                break;
            case R.id.web :
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.naver.com"));
                title = Intent.createChooser(intent, "웹사이트로 보기");
                startActivity(title);
                return true;
            case R.id.share :
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "안녕 !! 이건 공유하기");
                intent.putExtra(Intent.EXTRA_TEXT, "이런 앱이 있당!!");
                title = Intent.createChooser(intent, "공유하기");
                startActivity(title);
                return true;
            case R.id.send :
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/Text");
                String[] address = {"spdlqjfire@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, address);
                intent.putExtra(Intent.EXTRA_SUBJECT,"<자격증>개발자에게 보내는 조언 및 건의");
                intent.putExtra(Intent.EXTRA_TEXT,"**불편사항 혹은 건의사항의 경우 앱 버전과 안드로이드 기기 종류를 함께 보내주시면 감사하겠습니다.\n앱 버전: \n기기명: ");
                title = Intent.createChooser(intent, "개발자에게 보내는 글");
                startActivity(title);
                return true;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void downKeyboard() {

        InputMethodManager inputManager = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = mainActivity.getCurrentFocus();

        if (focusedView != null)
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public String getVersionInfo(Context context) {
        // Get App version
        String version = "Unknown";
        PackageInfo packageInfo;

        if (context == null)
            return version;

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
