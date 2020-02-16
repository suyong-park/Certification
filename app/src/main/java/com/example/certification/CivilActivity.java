package com.example.certification;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
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

public class CivilActivity extends AppCompatActivity {

    private ArrayList<Recycler_title> mArrayList;
    private CertificationTitleAdapter mAdapter;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.civil);

        setTitle("민간자격증");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.title_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        mArrayList = new ArrayList<>();
        mAdapter = new CertificationTitleAdapter(mArrayList);
        recyclerView.setAdapter(mAdapter);

        if(!isNetworkConnected())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(CivilActivity.this);
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

        Button button = (Button) findViewById(R.id.insert_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                Recycler_title data = new Recycler_title(count + "");
                mArrayList.add(data);
                mAdapter.notifyDataSetChanged();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // If touch the back key on tool bar, then finish present activity.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class CertificationTitleAdapter extends RecyclerView.Adapter<CertificationTitleAdapter.ViewHolder> {

        private ArrayList<Recycler_title> mlist;

        public class ViewHolder extends RecyclerView.ViewHolder {

            protected TextView title;

            public ViewHolder(View view) {
                super(view);
                this.title = (TextView) view.findViewById(R.id.item);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO : process click event, bring about that certification info to switch another activity
                        Intent intent = new Intent(getApplicationContext(), CertificationActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }

        public CertificationTitleAdapter(ArrayList<Recycler_title> list) {
            this.mlist = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
            ViewHolder viewholder = new ViewHolder(view);
            return viewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) { // Define properties of Recycler View items.
            viewholder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            viewholder.title.setGravity(Gravity.CENTER);
            viewholder.title.setText(mlist.get(position).getTitle());
        }

        @Override
        public int getItemCount() { // Count of Recycler View items.
            return mlist.size();
        }
    }
}