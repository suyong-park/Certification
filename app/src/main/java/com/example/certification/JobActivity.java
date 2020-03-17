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
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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

    String name, certification, certification_num;
    String title[], number[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job);

        JobActivity = JobActivity.this;

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.job_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new JobDetailAdapter();
        recyclerView.setAdapter(mAdapter);

        isNetworkWorking();

        Asynctask asyncTask = new Asynctask();
        asyncTask.execute();
    }

    public void ConnectDB() {
        ConnectDB connectDB = Broadcast.getRetrofit().create(ConnectDB.class);
        Call<List<Recycler_job>> call = connectDB.job_data();

        call.enqueue(new Callback<List<Recycler_job>>() {
            @Override
            public void onResponse(Call<List<Recycler_job>> call, Response<List<Recycler_job>> response) {
                List<Recycler_job> result = response.body();

                if(result != null)
                    if (result.size() != 0)
                        for (int i = 0; i < result.size(); i++) {
                            if(name.equals(result.get(i).getName())) {
                                mAdapter.add(new Recycler_job(result.get(i).getName(), result.get(i).getCategory(), result.get(i).getDescription(), result.get(i).getLink(), result.get(i).getNum(), result.get(i).getCertification_name()));
                                certification = result.get(i).getCertification_name();
                                certification_num = result.get(i).getNum();
                                title = certification.split(",");
                                number = certification_num.split(",");
                            }
                        }
            }
            @Override
            public void onFailure(Call<List<Recycler_job>> call, Throwable t) {
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

    private void isNetworkWorking() {
        if (!isNetworkConnected()) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(JobActivity.this);
            builder.setTitle("메시지")
                    .setMessage("네트워크 연결 상태를 확인해 주세요.")
                    .setCancelable(false)
                    .setPositiveButton("설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_job, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // If touch the back key on tool bar, then finish present activity.
                return true;
            case R.id.to_home :
                MainjobActivity mainjob = (MainjobActivity) MainjobActivity.MainjobActivity;
                DetailjobActivity detailjob = (DetailjobActivity) DetailjobActivity.DetailjobActivity;
                finish();
                detailjob.finish();
                mainjob.finish();
                break;
            case R.id.to_bookmark :
                Intent intent = new Intent(getApplicationContext(), BookmarkActivity.class);
                startActivity(intent);
                break;
            case R.id.to_certification :
                isTouchCertification();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void isTouchCertification() {
        final int[] selectedIndex = {0};

        for(int i = 0; i < title.length; i++)
            title[i] = title[i].trim();

        if(title.length == 1) {
            Intent intent = new Intent(getApplicationContext(), CertificationActivity.class);
            intent.putExtra("name", title[0]);
            intent.putExtra("num", number[0]);
            intent.putExtra("job", true);
            startActivity(intent);
            return ;
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(JobActivity.this);
        builder.setTitle("어디로 이동할까요?")
                .setSingleChoiceItems(title, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex[0] = which;
                    }
                })
                .setPositiveButton("이동", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), CertificationActivity.class);
                        intent.putExtra("name", title[selectedIndex[0]]);
                        intent.putExtra("num", number[selectedIndex[0]]);
                        intent.putExtra("job", true);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return ;
                    }
                })
                .show();
    }

    class JobDetailAdapter extends RecyclerView.Adapter<JobActivity.JobDetailAdapter.ViewHolder> {

        List<Recycler_job> mlist = new ArrayList<>();

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

            public void setData(Recycler_job data) {
                job_name.setText(data.getName());
                description.setText(data.getDescription());
                link.setText(data.getLink());
                name.setText(data.getCertification_name());
            }
        }

        public void add(Recycler_job item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public JobActivity.JobDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_job, viewGroup, false);
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
