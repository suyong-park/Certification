package com.example.certification;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobActivity extends AppCompatActivity {

    public static Activity JobActivity;
    private JobDetailAdapter mAdapter;

    String name, certification;
    String title[];
    boolean from_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job);

        JobActivity = JobActivity.this;

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        from_search = intent.getBooleanExtra("search", false);

        setTitle(name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.job_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new JobDetailAdapter();
        recyclerView.setAdapter(mAdapter);

        Broadcast.isNetworkWorking(JobActivity.this);

        ConnectDB();
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
                                mAdapter.add(new Recycler_job(result.get(i).getName(), result.get(i).getCategory(), result.get(i).getDescription(), result.get(i).getLink(), result.get(i).getCertification_name()));
                                certification = result.get(i).getCertification_name();
                                title = certification.split(",");
                            }
                        }
            }
            @Override
            public void onFailure(Call<List<Recycler_job>> call, Throwable t) {
                Log.d("ERROR MESSAGE", "CONNECT FAIL TO SERVER");
                Broadcast.AlertBuild(JobActivity, "에러", "서버 연결에 실패했습니다.")
                        .setPositiveButton("확인", null)
                        .setNegativeButton("취소", null)
                        .show();
            }
        });
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
                if(from_search) {
                    finish();
                    break;
                }
                MainjobActivity mainjob = (MainjobActivity) MainjobActivity.MainjobActivity;
                DetailjobActivity detailjob = (DetailjobActivity) DetailjobActivity.DetailjobActivity;
                finish();
                detailjob.finish();
                mainjob.finish();
                break;
            case R.id.to_bookmark :
                Intent intent = new Intent(JobActivity, BookmarkActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.to_certification :
                isTouchCertification();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void isTouchCertification() {
        final int[] selectedIndex = {0};
        final boolean[] flag = {true};

        for(int i = 0; i < title.length; i++)
            title[i] = title[i].trim();

        if(title.length == 1) {
            Intent intent = new Intent(JobActivity, CertificationActivity.class);
            intent.putExtra("name", title[0]);
            intent.putExtra("job", true);
            intent.putExtra("search", from_search);
            startActivity(intent);
            return ;
        }

        Broadcast.AlertBuild(JobActivity.this, "어디로 이동하시겠습니까?", null)
                .setSingleChoiceItems(title, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flag[0] = false;
                        selectedIndex[0] = which;
                    }
                })
                .setPositiveButton("이동", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (flag[0]) {
                            LinearLayout job = findViewById(R.id.linear_job);

                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            if (Build.VERSION.SDK_INT >= 26)
                                vibrator.vibrate(VibrationEffect.createOneShot(150, 80));
                            else
                                vibrator.vibrate(150);
                            Snackbar.make(job, "항목을 선택하세요.", Snackbar.LENGTH_SHORT).show();
                            isTouchCertification();
                        } else {
                            Intent intent = new Intent(JobActivity, CertificationActivity.class);
                            intent.putExtra("name", title[selectedIndex[0]]);
                            intent.putExtra("job", true);
                            intent.putExtra("search", from_search);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
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
}
