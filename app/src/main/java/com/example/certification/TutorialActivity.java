package com.example.certification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private LinearLayout container;
    private TextView present_page;
    private int[] layouts;
    private Button skip, next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.tutorial);

        viewPager = findViewById(R.id.viewpager);
        container = findViewById(R.id.layout_dots);
        skip = findViewById(R.id.skip);
        next = findViewById(R.id.next);

        layouts = new int[]{R.layout.tutorial_page_0, R.layout.tutorial_page_1, R.layout.tutorial_page_2, R.layout.tutorial_page_3, R.layout.tutorial_page_4};

        addBottomPageNumber(0);
        changeStatusBarColor();
        pagerAdapter = new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveMainPage();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if(current < layouts.length)
                    viewPager.setCurrentItem(current);
                else
                    moveMainPage();
            }
        });
    }

    private void addBottomPageNumber(int currentPage) { ;
        container.removeAllViews();
        present_page = new TextView(this);
        present_page.setText("" + (currentPage  + 1)+ "/" + "" + layouts.length);
        present_page.setTextSize(20);
        present_page.setPadding(0, 25,0, 0);
        present_page.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font_bookmark.ttf")); // Add font
        container.addView(present_page);
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void moveMainPage() {
        startActivity(new Intent(TutorialActivity.this, MainActivity.class));
        finish();
    }

    private void changeStatusBarColor() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomPageNumber(position);

            if(position == layouts.length - 1) {
                next.setText(getString(R.string.start));
                skip.setVisibility(View.VISIBLE);
            }
            else {
                next.setText(getString(R.string.next));
                skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {
        private LayoutInflater layoutInflater;

        public PagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
