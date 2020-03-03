package com.example.certification;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class CertificationActivity extends AppCompatActivity {

    private CertificationDetailAdapter mAdapter;

    String title;
    String num;

    Button bookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certification);

        Intent intent = getIntent();

        title = intent.getStringExtra("name");  // name of certification
        num = intent.getStringExtra("num");

        Log.d("", "num값 알아보기" + num);

        setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookmark = (Button) findViewById(R.id.bookmark);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.detail_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CertificationDetailAdapter();
        recyclerView.setAdapter(mAdapter);

        if (!isNetworkConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CertificationActivity.this);
            builder.setTitle("메시지")
                    .setMessage("네트워크 연결 상태를 확인해 주세요.")
                    .setCancelable(false)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
            return;
        }

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceManager.setString(getApplicationContext(), num, title);
                PreferenceManager.setString_key(getApplicationContext(), "key", num);
                Toast.makeText(getApplicationContext(), "북마크에 추가했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        ConnectDB();
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void ConnectDB() {
        ConnectDB connectDB = Request.getRetrofit().create(ConnectDB.class);
        Call<List<Recycler_certifidetail>> call = connectDB.certification_data();

        call.enqueue(new Callback<List<Recycler_certifidetail>>() {
            @Override

            public void onResponse(Call<List<Recycler_certifidetail>> call, Response<List<Recycler_certifidetail>> response) {
                List<Recycler_certifidetail> result = response.body();

                if(result != null)
                    if (result.size() != 0)
                        for (int i = 0; i < result.size(); i++)
                            if(title.contains(result.get(i).getNAME()))
                                mAdapter.add(new Recycler_certifidetail(result.get(i).getNAME(), result.get(i).getDESCRIPTION(), result.get(i).getCOMPANY(), result.get(i).getJOB(), result.get(i).getLINK(), result.get(i).getSUBJECT_NAME(), result.get(i).getRECEIPT_DATE(), result.get(i).getWRITTEN_DATE(), result.get(i).getPRACTICAL_DATE(), result.get(i).getANNOUNCEMENT_DATE()));

            }
            @Override
            public void onFailure(Call<List<Recycler_certifidetail>> call, Throwable t) {
                Log.d("ERROR MESSAGE", "CONNECT FAIL TO SERVER");
            }
        });
    }

    class CertificationDetailAdapter extends RecyclerView.Adapter<CertificationActivity.CertificationDetailAdapter.ViewHolder> {

        List<Recycler_certifidetail> mlist = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView description;
            TextView company;
            TextView job;
            TextView link;
            TextView subject_name;
            TextView receipt_date;
            TextView written_date;
            TextView practical_date;
            TextView announcement_date;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                description= (TextView) itemView.findViewById(R.id.description);
                company = (TextView) itemView.findViewById(R.id.company);
                job = (TextView) itemView.findViewById(R.id.job);
                link = (TextView) itemView.findViewById(R.id.link);
                subject_name = (TextView) itemView.findViewById(R.id.subject_name);
                receipt_date = (TextView) itemView.findViewById(R.id.receipt_date);
                written_date = (TextView) itemView.findViewById(R.id.written_date);
                practical_date = (TextView) itemView.findViewById(R.id.practical_date);
                announcement_date = (TextView) itemView.findViewById(R.id.announcement_date);
            }

            public void setData(Recycler_certifidetail data) {
                name.setText("이름 : " + data.getNAME());
                description.setText("자격증 내용 : " + data.getDESCRIPTION());
                company.setText("관련 회사 : " + data.getCOMPANY());
                job.setText("관련 직종 : " + data.getJOB());
                link.setText("관련 링크 : " + data.getLINK());
                subject_name.setText("시험 과목 : " + data.getSUBJECT_NAME());
                receipt_date.setText("접수 날짜 : " + data.getRECEIPT_DATE());
                written_date.setText("필기 시험 : " + data.getWRITTEN_DATE());
                practical_date.setText("실기 시험 : " + data.getPRACTICAL_DATE());
                announcement_date.setText("발표 날짜 : " + data.getANNOUNCEMENT_DATE());
            }
        }

        public void add(Recycler_certifidetail item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public CertificationActivity.CertificationDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.detail_list, viewGroup, false);
            return new CertificationActivity.CertificationDetailAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CertificationActivity.CertificationDetailAdapter.ViewHolder viewholder, int position) { // Define properties of Recycler View items.
            viewholder.setData(mlist.get(position));
        }

        @Override
        public int getItemCount() { // Count of Recycler View items.
            return mlist.size();
        }
    }
}
