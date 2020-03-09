package com.example.certification;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    PagerAdapter adapter;
    ViewPager pager;

    MoveBookmarkFragment fragment1;
    DeleteBookmarkFragment fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);

        setTitle("북마크");

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
                    case R.id.tab1 :
                        pager.setCurrentItem(0);
                        break;
                    case R.id.tab2 :
                        pager.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
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
