package com.example.certification;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransparentActivity extends AppCompatActivity {

    Button close;
    TextView process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent);

        close = (Button) findViewById(R.id.close);
        process = (TextView) findViewById(R.id.process);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        ConnectDB();
    }

    public void ConnectDB() {
        ConnectDB connectDB = Broadcast.getRetrofit().create(ConnectDB.class);
        Call<String> call = connectDB.parsing_data();

        call.enqueue(new Callback<String>() {
            @Override

            public void onResponse(Call<String> call, Response<String> response) {
                String result = response.body();

                if(result != null) {
                    result = result.replace("&#39;", "'");
                    process.setText(result);
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("ERROR MESSAGE", "CONNECT FAIL TO SERVER");
            }
        });
    }
}
