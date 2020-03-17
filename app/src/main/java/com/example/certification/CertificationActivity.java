package com.example.certification;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CertificationActivity extends AppCompatActivity {

    private CertificationDetailAdapter mAdapter;
    ProgressDialog dialog;
    LinearLayout certification_layout;

    boolean from_job, from_bookmark;
    String title, num;
    int max = 1;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certification);

        Intent intent = getIntent();

        title = intent.getStringExtra("name");  // name of certification
        num = intent.getStringExtra("num");
        from_job = intent.getBooleanExtra("job", false);
        from_bookmark = intent.getBooleanExtra("bookmark", false);

        setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isNetworkWorking();

        certification_layout = (LinearLayout) findViewById(R.id.certification_layout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.certification_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CertificationDetailAdapter();
        recyclerView.setAdapter(mAdapter);

        Asynctask asynctask = new Asynctask();
        asynctask.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_certification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        JobActivity job = (JobActivity) JobActivity.JobActivity;
        MainjobActivity mainjob = (MainjobActivity) MainjobActivity.MainjobActivity;
        DetailjobActivity detailjob = (DetailjobActivity) DetailjobActivity.DetailjobActivity;

        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // If touch the back key on tool bar, then finish present activity.
                return true;
            case R.id.to_home:
                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.to_bookmark:
                intent = new Intent(getApplicationContext(), BookmarkActivity.class);
                if (from_job) { // Move in original order from job activity.
                    startActivity(intent);
                    finish();
                    job.finish();
                    detailjob.finish();
                    mainjob.finish();
                }
                else if (from_bookmark)
                    finish();
                else if (!from_job && !from_bookmark)  // Move in original order from main activity.
                    startActivity(intent);
                break;
            case R.id.is_bookmark:
                isTouchBookmark();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void isTouchBookmark() {

        String text;

        boolean flag_1, flag_2 = false;
        int count = 0, i;
        String[] temp_array = new String[max];

        while(count < temp_array.length) {
            for(int j = 0; j < temp_array.length; j++) {
                text = PreferenceManager.getString(getApplicationContext(), Integer.toString(j));

                if (text.equals(title)) {
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CertificationActivity.this);
                    builder.setTitle("오류")
                            .setMessage("이미 존재하는 북마크입니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).show();
                    flag_2 = true;
                    break;
                }
            }

            if(flag_2)
                break;

            while(true) {
                i = PreferenceManager.getInt(getApplicationContext(), "") + 1;
                PreferenceManager.setString(getApplicationContext(), Integer.toString(i), title);
                PreferenceManager.setInt(getApplicationContext(), "", i);
                flag_1 = true;
                count++;
                Snackbar.make(certification_layout, "북마크에 추가했어염", Snackbar.LENGTH_SHORT).show();
                break;
            }
            if(flag_1)
                break;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void isNetworkWorking() {
        if (!isNetworkConnected()) {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CertificationActivity.this);
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

    public void showCertification() {

        // TODO : 시험과목 , 로 되어있는건 분할해야함.
        ConnectDB connectDB = Broadcast.getRetrofit().create(ConnectDB.class);
        Call<List<Recycler_certification>> call = connectDB.certification_data();

        call.enqueue(new Callback<List<Recycler_certification>>() {
            @Override

            public void onResponse(Call<List<Recycler_certification>> call, Response<List<Recycler_certification>> response) {
                List<Recycler_certification> result = response.body();

                if(result != null)
                    if (result.size() != 0)
                        for (int i = 0; i < result.size(); i++) {
                                if (max <= result.get(i).getNUM())
                                    max = result.get(i).getNUM();
                                if (title.contains(result.get(i).getNAME()))
                                    mAdapter.add(new Recycler_certification(result.get(i).getNAME(), result.get(i).getDESCRIPTION(), result.get(i).getCOMPANY(), result.get(i).getJOB(), result.get(i).getLINK(), result.get(i).getNUM(), result.get(i).getSUBJECT_WRITTEN(), result.get(i).getSUBJECT_PRACTICAL(),  result.get(i).getRECEIPT_DATE(), result.get(i).getWRITTEN_DATE(), result.get(i).getPRACTICAL_DATE(), result.get(i).getANNOUNCEMENT_DATE()));
                        }
            }
            @Override
            public void onFailure(Call<List<Recycler_certification>> call, Throwable t) {
                Log.d("ERROR MESSAGE", "CONNECT FAIL TO SERVER");
            }
        });
    }

    class CertificationDetailAdapter extends RecyclerView.Adapter<CertificationActivity.CertificationDetailAdapter.ViewHolder> {

        List<Recycler_certification> mlist = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            TextView description;
            TextView company;
            TextView job;
            TextView link;
            TextView subject_written;
            TextView subject_practical;
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
                subject_written = (TextView) itemView.findViewById(R.id.subject_written);
                subject_practical = (TextView) itemView.findViewById(R.id.subject_practical);
                receipt_date = (TextView) itemView.findViewById(R.id.receipt_date);
                written_date = (TextView) itemView.findViewById(R.id.written_date);
                practical_date = (TextView) itemView.findViewById(R.id.practical_date);
                announcement_date = (TextView) itemView.findViewById(R.id.announcement_date);
            }

            public void setData(Recycler_certification data) {
                name.setText(data.getNAME());
                description.setText(data.getDESCRIPTION());
                company.setText(data.getCOMPANY());
                job.setText(data.getJOB());
                link.setText(data.getLINK());
                subject_written.setText(data.getSUBJECT_WRITTEN());
                subject_practical.setText(data.getSUBJECT_PRACTICAL());
                receipt_date.setText(data.getRECEIPT_DATE());
                written_date.setText(data.getWRITTEN_DATE());
                practical_date.setText(data.getPRACTICAL_DATE());
                announcement_date.setText(data.getANNOUNCEMENT_DATE());
            }
        }

        public void add(Recycler_certification item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public CertificationActivity.CertificationDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_certification, viewGroup, false);
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

    class Asynctask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(CertificationActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("데이터를 불러오는 중입니다.");
            dialog.show();
            dialog.setCancelable(false);
        }

        protected Void doInBackground(Void ... values) {

            try {
                showCertification();
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
