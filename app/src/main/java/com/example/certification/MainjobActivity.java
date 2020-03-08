package com.example.certification;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainjobActivity extends AppCompatActivity {

    private MainjobAdapter mAdapter;
    public GestureDetector gesture_detector;
    String temp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainjob);

        setTitle("진출분야");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.title_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MainjobAdapter();
        recyclerView.setAdapter(mAdapter);

        gesture_detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        if(!isNetworkConnected())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainjobActivity.this);
            builder.setTitle("메시지")
                    .setMessage("네트워크 연결 상태를 확인해 주세요.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
            return ;
        }

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());

                if(childView != null && gesture_detector.onTouchEvent((e))) {
                    int currentPos = rv.getChildAdapterPosition(childView);
                    Intent it = new Intent(MainjobActivity.this, DetailjobActivity.class);
                    it.putExtra("category", mAdapter.getRecycler_title(currentPos).getJOB_CATEGORY());
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

    private boolean isNetworkConnected() { // Checking the Network is Connected
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void ConnectDB() {

        ConnectDB connectDB = Request.getRetrofit().create(ConnectDB.class);
        Call<List<Recycler_job>> call = connectDB.category_data();

        call.enqueue(new Callback<List<Recycler_job>>() {
            @Override
            public void onResponse(Call<List<Recycler_job>> call, Response<List<Recycler_job>> response) {
                List<Recycler_job> result = response.body();

                if(result != null)
                    if (result.size() != 0)
                        for (int i = 0; i < result.size(); i++) {
                            if(!temp.equals(result.get(i).getJOB_CATEGORY()))
                                mAdapter.add(new Recycler_job(result.get(i).getJOB_NAME(), result.get(i).getJOB_CATEGORY(), result.get(i).getNUM()));
                            temp = result.get(i).getJOB_CATEGORY();
                        }
            }

            @Override
            public void onFailure(Call<List<Recycler_job>> call, Throwable t) {
                Log.d("ERROR MESSAGE", "CONNECT FAIL TO SERVER");
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

        List<Recycler_job> mlist = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.item);
            }

            public void setData(Recycler_job data) {
                title.setText(data.getJOB_CATEGORY());
            }
        }

        public void add(Recycler_job item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public MainjobActivity.MainjobAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_titlelist, viewGroup, false);
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

        public Recycler_job getRecycler_title(int pos) {
            return mlist.get(pos);
        }
    }
}
