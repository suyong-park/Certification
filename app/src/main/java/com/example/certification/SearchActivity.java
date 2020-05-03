package com.example.certification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchAdapter mAdapter;
    public GestureDetector gesture_detector;
    public static Activity searchActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        searchActivity = SearchActivity.this;

        Intent intent = getIntent();
        String[] title_names = intent.getStringArrayExtra("title_name");
        final String isCertification = intent.getStringExtra("category");
        String search_word = intent.getStringExtra("search_word");

        LinearLayout linearLayout = findViewById(R.id.linear);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(searchActivity, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new SearchAdapter();
        recyclerView.setAdapter(mAdapter);

        for(int i = 0; i < title_names.length; i++)
            mAdapter.add(new Recycler_onething(title_names[i]));

        Snackbar.make(linearLayout, search_word + " 와(과) 관련된 검색 결과입니다.", Snackbar.LENGTH_SHORT).show();
        gesture_detector = new GestureDetector(searchActivity, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());

                if(childView != null && gesture_detector.onTouchEvent((e))) {
                    int currentPos = rv.getChildAdapterPosition(childView);
                    Intent intent;
                    if(isCertification.equals("자격증")) // User search for certification
                        intent = new Intent(searchActivity, CertificationActivity.class);
                    else
                        intent = new Intent(searchActivity, JobActivity.class);
                    intent.putExtra("name", mAdapter.getRecycler_title(currentPos).getTitle());
                    intent.putExtra("search", true);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
        List<Recycler_onething> mlist = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
            }

            public void setData(Recycler_onething data) {
                title.setText(data.getTitle());
            }
        }

        public void add(Recycler_onething item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_only_title, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) { // Define properties of Recycler View items.
            viewholder.setData(mlist.get(position));
        }

        @Override
        public int getItemCount() { // Count of Recycler View items.
            return mlist.size();
        }

        public Recycler_onething getRecycler_title(int pos) {
            return mlist.get(pos);
        }
    }
}
