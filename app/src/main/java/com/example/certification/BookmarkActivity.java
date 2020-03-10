package com.example.certification;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    // TODO : 폴더 생성했을 때 생성되는 리스트

    PagerAdapter adapter;
    ViewPager pager;

    MoveBookmarkFragment fragment1;
    DeleteBookmarkFragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);

        setTitle("북마크");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);

        adapter = new PagerAdapter(getSupportFragmentManager());

        fragment1 = new MoveBookmarkFragment();
        fragment2 = new DeleteBookmarkFragment();
        adapter.addItem(fragment1);
        adapter.addItem(fragment2);
        pager.setAdapter(adapter);

        final BottomNavigationView bottom = findViewById(R.id.bottom_navigation);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        pager.setCurrentItem(0);
                        break;
                    case R.id.tab2:
                        pager.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // Connecting ViewPager and BottomNavigationView
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) { // Operate if you slide another tab.
                switch (position) {
                    case 0:
                        bottom.getMenu().findItem(R.id.tab1).setChecked(true);
                        break;
                    case 1:
                        bottom.getMenu().findItem(R.id.tab2).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // If touch the back key on tool bar, then finish present activity.
                return true;
            case R.id.folder_add:
                onRecursive();
                return true;
            case R.id.folder_delete:
                Toast.makeText(getApplicationContext(), "폴더 삭제", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onRecursive() {
        final EditText folder_name = new EditText(getApplicationContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);
        builder.setTitle("폴더 생성")
                .setMessage("폴더 이름을 입력하세요")
                .setView(folder_name)
                .setPositiveButton("생성", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            if(folder_name.getText().toString().trim().equals("")) {
                                AlertDialog.Builder sub = new AlertDialog.Builder(BookmarkActivity.this);
                                sub.setTitle("에러")
                                        .setMessage("폴더 이름을 입력하세요.")
                                        .setCancelable(false)
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onRecursive();
                                                return ;
                                            }
                                        }).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "폴더 추가" + folder_name.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                            }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return ;
                    }
                }).show();
    }

    class PagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<Fragment> items = new ArrayList<Fragment>();
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}
