package com.example.certification;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class CertificationActivity extends AppCompatActivity {

    public static Activity certificationactivity;
    private CertificationDetailAdapter mAdapter;
    LinearLayout certification_layout;

    boolean from_bookmark, from_search;
    String title, receipt_date, written_date, practical_date, announcement_date;
    int max = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.certification);

        certificationactivity = CertificationActivity.this;

        Intent intent = getIntent();

        title = intent.getStringExtra("name");  // name of certification
        from_bookmark = intent.getBooleanExtra("bookmark", false);
        from_search = intent.getBooleanExtra("search", false);

        setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Broadcast.isNetworkWorking(certificationactivity, false);

        certification_layout = (LinearLayout) findViewById(R.id.certification_layout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.certification_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(certificationactivity, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CertificationDetailAdapter();
        recyclerView.setAdapter(mAdapter);

        downloadCertification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_certification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // If touch the back key on tool bar, then finish present activity.
                return true;
            case R.id.to_home:
                intent = new Intent(certificationactivity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.to_bookmark:
                intent = new Intent(certificationactivity, BookmarkActivity.class);
                if(from_search) {
                    finish();
                    startActivity(intent);
                    break;
                }

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
        int count = -1, num_temp;

        while(true) {
            count++;
            text = PreferenceManager.getString(certificationactivity, Integer.toString(count));

            if (text.equals(title)) { // already has bookmark of this certification
                Broadcast.AlertBuild(certificationactivity, "메시지", "이미 존재하는 북마크입니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .show();
                return ;
            }

            if(count == max)
                break;
        }

        // Below code is for Add Bookmark
        num_temp = PreferenceManager.getInt(certificationactivity, "") + 1;
        PreferenceManager.setString(certificationactivity, Integer.toString(num_temp), title);
        PreferenceManager.setInt(certificationactivity, "", num_temp);
        PreferenceManager.setString(certificationactivity, num_temp + "_date", receipt_date + ", " + written_date + ", " + practical_date + ", " + announcement_date);
        Snackbar.make(certification_layout, "북마크에 추가했습니다.", Snackbar.LENGTH_SHORT).show();
    }

    public void downloadCertification() {

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
                                if (title.contains(result.get(i).getNAME())) {
                                    mAdapter.add(new Recycler_certification(result.get(i).getNAME(), result.get(i).getDESCRIPTION(), result.get(i).getCOMPANY(), result.get(i).getJOB(), result.get(i).getLINK(), result.get(i).getNUM(), result.get(i).getSUBJECT_WRITTEN(), result.get(i).getSUBJECT_PRACTICAL(), result.get(i).getRECEIPT_DATE(), result.get(i).getWRITTEN_DATE(), result.get(i).getPRACTICAL_DATE(), result.get(i).getANNOUNCEMENT_DATE()));
                                    receipt_date = result.get(i).getRECEIPT_DATE();
                                    written_date = result.get(i).getWRITTEN_DATE();
                                    practical_date = result.get(i).getPRACTICAL_DATE();
                                    announcement_date = result.get(i).getANNOUNCEMENT_DATE();
                                }
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

            TextView description;
            TextView link;
            TextView subject_written;
            TextView subject_practical;
            TextView receipt_date;
            TextView written_date;
            TextView practical_date;
            TextView announcement_date;

            public ViewHolder(View itemView) {
                super(itemView);
                description= (TextView) itemView.findViewById(R.id.description);
                link = (TextView) itemView.findViewById(R.id.link);
                subject_written = (TextView) itemView.findViewById(R.id.subject_written);
                subject_practical = (TextView) itemView.findViewById(R.id.subject_practical);
                receipt_date = (TextView) itemView.findViewById(R.id.receipt_date);
                written_date = (TextView) itemView.findViewById(R.id.written_date);
                practical_date = (TextView) itemView.findViewById(R.id.practical_date);
                announcement_date = (TextView) itemView.findViewById(R.id.announcement_date);
            }

            public void setData(Recycler_certification data) {
                description.setText(data.getDESCRIPTION());
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
}
