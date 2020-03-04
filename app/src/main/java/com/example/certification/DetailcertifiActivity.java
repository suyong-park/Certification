package com.example.certification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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

public class DetailcertifiActivity extends AppCompatActivity {

    private CertificationTitleAdapter mAdapter;
    public GestureDetector gesture_detector;

    ProgressDialog dialog;

    String category;
    Button bookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailcertifi);

        Intent intent = getIntent();
        category = intent.getStringExtra("detailcertifi");  // detailcertifi of certification

        setTitle(category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookmark = (Button) findViewById(R.id.bookmark);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.title_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CertificationTitleAdapter();
        recyclerView.setAdapter(mAdapter);

        gesture_detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        if(!isNetworkConnected())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(DetailcertifiActivity.this);
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
                    Intent it = new Intent(DetailcertifiActivity.this, CertificationActivity.class);
                    it.putExtra("name", mAdapter.getRecycler_title(currentPos).getTitle());
                    it.putExtra("num", mAdapter.getRecycler_title(currentPos).getNum());
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

        Handler handler = new Handler();

        dialog = new ProgressDialog(DetailcertifiActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("데이터를 불러오는 중입니다.");
        dialog.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ConnectDB();
                dialog.dismiss();
            }
        }, 700);
    }

    private boolean isNetworkConnected() { // Checking the Network is Connected
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
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

    public void ConnectDB() {


        ConnectDB connectDB = Request.getRetrofit().create(ConnectDB.class);
        Call<List<Recycler_certifi>> call = connectDB.title_data();

        call.enqueue(new Callback<List<Recycler_certifi>>() {
            @Override
            public void onResponse(Call<List<Recycler_certifi>> call, Response<List<Recycler_certifi>> response) {
                List<Recycler_certifi> result = response.body();

                if(result != null)
                    if (result.size() != 0)
                        for (int i = 0; i < result.size(); i++)
                            if(category.contains(result.get(i).getCategory()))
                                mAdapter.add(new Recycler_certifi(result.get(i).getTitle(), result.get(i).getCategory(), result.get(i).getNum()));
            }
            @Override
            public void onFailure(Call<List<Recycler_certifi>> call, Throwable t) {
                Log.d("ERROR MESSAGE", "CONNECT FAIL TO SERVER");
            }
        });
    }

    class CertificationTitleAdapter extends RecyclerView.Adapter<CertificationTitleAdapter.ViewHolder> {

        List<Recycler_certifi> mlist = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.item);
            }

            public void setData(Recycler_certifi data) {
                title.setText(data.getTitle());
            }
        }

        public void add(Recycler_certifi item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_titlelist, viewGroup, false);
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

        public Recycler_certifi getRecycler_title(int pos) {
            return mlist.get(pos);
        }
    }
}