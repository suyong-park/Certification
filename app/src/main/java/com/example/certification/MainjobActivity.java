package com.example.certification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainjobActivity extends AppCompatActivity {

    public static Activity MainjobActivity;
    private MainjobAdapter mAdapter;
    public GestureDetector gesture_detector;
    String temp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainjob);

        MainjobActivity = MainjobActivity.this;

        setTitle("진출분야");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.category_job);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MainjobAdapter();
        recyclerView.setAdapter(mAdapter);

        gesture_detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        Broadcast.isNetworkWorking(MainjobActivity.this);

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());

                if(childView != null && gesture_detector.onTouchEvent((e))) {
                    int currentPos = rv.getChildAdapterPosition(childView);
                    Intent it = new Intent(MainjobActivity.this, DetailjobActivity.class);
                    it.putExtra("category", mAdapter.getRecycler_title(currentPos).getCategory());
                    startActivity(it);
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

        ConnectDB();
    }

    public void ConnectDB() {

        ConnectDB connectDB = Broadcast.getRetrofit().create(ConnectDB.class);
        Call<List<Recycler_category>> call = connectDB.job_category_data();

        call.enqueue(new Callback<List<Recycler_category>>() {
            @Override
            public void onResponse(Call<List<Recycler_category>> call, Response<List<Recycler_category>> response) {
                List<Recycler_category> result = response.body();

                if(result != null)
                    if (result.size() != 0)
                        for (int i = 0; i < result.size(); i++) {
                            if(!temp.equals(result.get(i).getCategory()))
                                mAdapter.add(new Recycler_category(result.get(i).getTitle(), result.get(i).getCategory()));
                            temp = result.get(i).getCategory();
                        }
            }

            @Override
            public void onFailure(Call<List<Recycler_category>> call, Throwable t) {
                Log.d("ERROR MESSAGE", "CONNECT FAIL TO SERVER");
                Broadcast.AlertBuild(MainjobActivity, "에러", "서버 연결에 실패했습니다.")
                        .setPositiveButton("확인", null)
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // If touch the back key on tool bar, then finish present activity.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class MainjobAdapter extends RecyclerView.Adapter<MainjobActivity.MainjobAdapter.ViewHolder> {

        List<Recycler_category> mlist = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
            }

            public void setData(Recycler_category data) {
                title.setText(data.getCategory());
            }
        }

        public void add(Recycler_category item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public MainjobActivity.MainjobAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_category, viewGroup, false);
            return new MainjobActivity.MainjobAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MainjobActivity.MainjobAdapter.ViewHolder viewholder, int position) { // Define properties of Recycler View items.
            viewholder.setData(mlist.get(position));
        }

        @Override
        public int getItemCount() { // Count of Recycler View items.
            return mlist.size();
        }

        public Recycler_category getRecycler_title(int pos) {
            return mlist.get(pos);
        }
    }
}
