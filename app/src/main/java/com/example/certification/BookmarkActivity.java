package com.example.certification;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BookmarkActivity extends AppCompatActivity {

    /*
       TODO
        1. 제거하기 버튼을 제거하고 swipelayout을 구성하여 사용할 수 있도록 한다.
        2. TextView 왼쪽에 RadioButton을 추가하여 삭제할 컴포넌트를 클릭할 수 있도록 한다.
     */

    private static Toast toast;
    private static final float FONT_SIZE = 30;
    LinearLayout container;

    String state, num;

    Button delete;
    int max, count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);

        setTitle("북마크");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String text, value;

        container = (LinearLayout) findViewById(R.id.container);
        delete = (Button) findViewById(R.id.delete);

        try {
            value = PreferenceManager.getString_max(getApplicationContext(), "value"); // max value
            max = Integer.parseInt(value);

            for(int i = 1; i <= max; i++) {
                String temp = String.valueOf(i);
                text = PreferenceManager.getString(getApplicationContext(), temp);
                if(!text.equals(""))
                {
                    TextView(text);
                    num = temp;
                    count++;
                }
            }
            if(count == 0)
                TextView("북마크가 없어용");
        }
        catch (NumberFormatException e) {
            TextView("북마크가 없어용");
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);

                if(state.equals("북마크가 없어용")) {
                    showToast(getApplicationContext(), "삭제할 북마크가 없습니다.");
                    return ;
                }

                builder.setTitle("메시지")
                        .setMessage("정말 북마크를 삭제하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for(int i = 1; i <= max; i++) {
                                    String temp = String.valueOf(i);
                                    PreferenceManager.removeKey(getApplicationContext(), temp);
                                }
                                showToast(getApplicationContext(), "북마크가 삭제되었습니다.");
                                finish();
                                startActivity(getIntent());
                                overridePendingTransition(0, 0); // Blocking an activity animation when an activity was switch over.
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return ;
                            }
                        }).show();
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

    public static void showToast(Context context,String message) {

        if(toast == null)
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        else
            toast.setText(message);
        toast.show();
    }

    public void TextView(final String data) {

        state = data;

        TextView text = new TextView(getApplicationContext());

        text.setText(data);
        text.setTextSize(FONT_SIZE);

        LinearLayout.LayoutParams linear_par = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linear_par.gravity = Gravity.CENTER;
        text.setLayoutParams(linear_par);

        container.addView(text);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(data.equals("북마크가 없어용"))
                    return ;
                Intent intent = new Intent(getApplicationContext(), CertificationActivity.class);
                intent.putExtra("name", data);
                intent.putExtra("num", num);
                startActivity(intent);
            }
        });
    }
}
