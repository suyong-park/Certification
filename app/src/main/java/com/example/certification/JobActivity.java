package com.example.certification;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

public class JobActivity extends AppCompatActivity {

    public static Activity JobActivity;
    Handler handler = new Handler();
    private JobDetailAdapter mAdapter;
    ProgressDialog dialog;
    String name, num, certification, certification_num;
    Button move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job);

        JobActivity = JobActivity.this;

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        num = intent.getStringExtra("num");

        setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        move = (Button) findViewById(R.id.move);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.job_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new JobDetailAdapter();
        recyclerView.setAdapter(mAdapter);

        if(!isNetworkConnected())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(JobActivity.this);
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

        Asynctask asyncTask = new Asynctask();
        asyncTask.execute();

        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CertificationActivity.class);
                intent.putExtra("name", certification);
                intent.putExtra("num", certification_num);
                intent.putExtra("job", true);
                startActivity(intent);
            }
        });
    }

    public void ConnectDB() {
        ConnectDB connectDB = Broadcast.getRetrofit().create(ConnectDB.class);
        Call<List<Recycler_jobdetail>> call = connectDB.job_data();

        call.enqueue(new Callback<List<Recycler_jobdetail>>() {
            @Override
            public void onResponse(Call<List<Recycler_jobdetail>> call, Response<List<Recycler_jobdetail>> response) {
                List<Recycler_jobdetail> result = response.body();

                if(result != null)
                    if (result.size() != 0)
                        for (int i = 0; i < result.size(); i++)
                            if(name.equals(result.get(i).getJOB_NAME())) {
                                mAdapter.add(new Recycler_jobdetail(result.get(i).getJOB_NAME(), result.get(i).getJOB_CATEGORY(), result.get(i).getDESCRIPTION(), result.get(i).getLINK(), result.get(i).getNUM(), result.get(i).getNAME()));
                                certification = result.get(i).getNAME();
                                certification_num = result.get(i).getNUM();
                            }
            }
            @Override
            public void onFailure(Call<List<Recycler_jobdetail>> call, Throwable t) {
                Log.d("ERROR MESSAGE", "CONNECT FAIL TO SERVER");
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_to_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // If touch the back key on tool bar, then finish present activity.
                return true;
            case R.id.icon_home :
                MainjobActivity mainjob = (MainjobActivity) MainjobActivity.MainjobActivity;
                DetailjobActivity detailjob = (DetailjobActivity) DetailjobActivity.DetailjobActivity;
                finish();
                detailjob.finish();
                mainjob.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class JobDetailAdapter extends RecyclerView.Adapter<JobActivity.JobDetailAdapter.ViewHolder> {

        List<Recycler_jobdetail> mlist = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView job_name;
            TextView description;
            TextView link;
            TextView name;

            public ViewHolder(View itemView) {
                super(itemView);
                job_name = (TextView) itemView.findViewById(R.id.job_name);
                description = (TextView) itemView.findViewById(R.id.description);
                link = (TextView) itemView.findViewById(R.id.link);
                name = (TextView) itemView.findViewById(R.id.name);
            }

            public void setData(Recycler_jobdetail data) {
                job_name.setText("직업 이름 : " + data.getJOB_NAME());
                description.setText("직업 설명 : " + data.getDESCRIPTION());
                link.setText("관련 링크 : " + data.getLINK());
                name.setText("관련 자격증 : " + data.getNAME());
            }
        }

        public void add(Recycler_jobdetail item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public JobActivity.JobDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_job_detail, viewGroup, false);
            return new JobActivity.JobDetailAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull JobActivity.JobDetailAdapter.ViewHolder viewholder, int position) { // Define properties of Recycler View items.
            viewholder.setData(mlist.get(position));
        }

        @Override
        public int getItemCount() { // Count of Recycler View items.
            return mlist.size();
        }
    }

    class Asynctask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(JobActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("데이터를 불러오는 중입니다.");
            dialog.show();
            dialog.setCancelable(false);
        }

        protected Void doInBackground(Void ... values) {

            try {
                ConnectDB();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, 700);
        }
    }
}
