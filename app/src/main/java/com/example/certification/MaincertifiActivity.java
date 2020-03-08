package com.example.certification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaincertifiActivity extends AppCompatActivity {

    private MaincertifiAdapter mAdapter;
    public GestureDetector gesture_detector;
    String certifi_title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maincertifi);

        setTitle("자격증");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.title_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new MaincertifiAdapter();
        recyclerView.setAdapter(mAdapter);

        gesture_detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        if(!isNetworkConnected())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MaincertifiActivity.this);
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
                    Intent it = new Intent(MaincertifiActivity.this, DetailcertifiActivity.class);
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
        Call<List<Recycler_certifi>> call = connectDB.title_data();

        call.enqueue(new Callback<List<Recycler_certifi>>() {
            @Override
            public void onResponse(Call<List<Recycler_certifi>> call, Response<List<Recycler_certifi>> response) {
                List<Recycler_certifi> result = response.body();

                if(result != null)
                    if (result.size() != 0)
                        for (int i = 0; i < result.size(); i++) {
                            String temp = certifi_title;
                            if(result.get(i).getCategory().contains("기술"))
                                certifi_title = "국가기술자격증";
                            else if(result.get(i).getCategory().contains("민간"))
                                certifi_title = "민간자격증";
                            else if(result.get(i).getCategory().contains("전문"))
                                certifi_title = "국가전문자격증";
                            if(!temp.equals(certifi_title))
                                mAdapter.add(new Recycler_certifi(result.get(i).getTitle(), certifi_title, result.get(i).getNum()));
                        }
            }

            @Override
            public void onFailure(Call<List<Recycler_certifi>> call, Throwable t) {
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

    class MaincertifiAdapter extends RecyclerView.Adapter<MaincertifiActivity.MaincertifiAdapter.ViewHolder> {

        List<Recycler_certifi> mlist = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.item);
            }

            public void setData(Recycler_certifi data) {
                title.setText(data.getCategory());
            }
        }

        public void add(Recycler_certifi item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public MaincertifiActivity.MaincertifiAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_titlelist, viewGroup, false);
            return new MaincertifiActivity.MaincertifiAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MaincertifiActivity.MaincertifiAdapter.ViewHolder viewholder, int position) { // Define properties of Recycler View items.
            viewholder.setData(mlist.get(position));
        }

        @Override
        public int getItemCount() { // Count of Recycler View items.
            return mlist.size();
        }

        public Recycler_certifi getRecycler_title(int pos) {
            return mlist.get(pos);
        }
    }
}
