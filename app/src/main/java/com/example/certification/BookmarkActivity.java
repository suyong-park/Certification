package com.example.certification;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

    private static Toast toast;

    int max;
    String num;

    Button move_display, delete_display;

    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listView;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);

        setTitle("북마크");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String text, value;

        delete_display = (Button) findViewById(R.id.delete_display);
        move_display = (Button) findViewById(R.id.move_display);
        listView = (ListView) findViewById(R.id.listview);
        linear = (LinearLayout) findViewById(R.id.linear);

        items = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(BookmarkActivity.this, android.R.layout.simple_list_item_multiple_choice, items);
        listView.setAdapter(adapter);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);

        move_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SparseBooleanArray sbArray = listView.getCheckedItemPositions();
                if(sbArray.size() > 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);
                    builder.setTitle("메시지")
                            .setMessage("하나의 항목만 선택해 주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    listView.clearChoices();
                    return ;
                }
                else if(sbArray.size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);
                    builder.setTitle("메시지")
                            .setMessage("어디로 이동할지 선택해 주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    listView.clearChoices();
                    return ;
                }
                else {
                    for (int i = listView.getCount() - 1; i >= 0; i--)
                        if (sbArray.get(i)) {
                            Intent intent = new Intent(getApplicationContext(), CertificationActivity.class);
                            intent.putExtra("name", items.get(i));
                            intent.putExtra("num", num);
                            startActivity(intent);
                        }
                    listView.clearChoices();
                }
            }
        });

        delete_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SparseBooleanArray sbArray = listView.getCheckedItemPositions();
                // Notify location of selected items. ex) 0, 3, 7, 9 => true return

                AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this);

                if (items.isEmpty()) {
                    showToast(getApplicationContext(), "삭제할 북마크가 없습니다.");
                    return;
                }

                if (sbArray.size() == 0) {
                    builder.setTitle("메시지")
                            .setMessage("삭제할 북마크를 선택해 주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).show();
                    listView.clearChoices();
                }
                else {
                    builder.setTitle("메시지")
                            .setMessage("정말 북마크를 삭제하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (sbArray.size() != 0) {
                                        for (int i = listView.getCount() - 1; i >= 0; i--)
                                            if (sbArray.get(i))
                                                for (int j = 1; j <= max; j++) {
                                                    String temp_num = String.valueOf(j);
                                                    String tmp = PreferenceManager.getString(getApplicationContext(), temp_num);
                                                    if (tmp.equals(items.get(i))) {
                                                        items.remove(i);
                                                        PreferenceManager.removeKey(getApplicationContext(), temp_num);
                                                        break;
                                                    }
                                                }
                                        listView.clearChoices();
                                        adapter.notifyDataSetChanged();

                                        showToast(getApplicationContext(), "북마크가 삭제되었습니다.");
                                        startActivity(getIntent());
                                        finish();
                                        overridePendingTransition(0, 0);
                                    }
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            }).show();
                }
            }
        });

        try {
            value = PreferenceManager.getString_max(getApplicationContext(), "value"); // max value
            max = Integer.parseInt(value);

            for(int i = 1; i <= max; i++) {
                String temp = String.valueOf(i);
                text = PreferenceManager.getString(getApplicationContext(), temp);
                if(!text.equals(""))
                {
                    items.add(text);
                    num = temp;
                }
            }
        }
        catch (NumberFormatException e) {
        }
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

    public void TextView(String data){
        TextView textView = new TextView(getApplicationContext());
        textView.setText(data);
        textView.setTextSize(30);

        LinearLayout.LayoutParams linear = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linear.gravity = Gravity.CENTER;
        textView.setLayoutParams(linear);

        //panel.addView(textView);
    }
}
