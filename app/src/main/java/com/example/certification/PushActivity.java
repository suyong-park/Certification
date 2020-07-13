package com.example.certification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class PushActivity extends AppCompatActivity {

    MaterialButton confirm;
    SwitchMaterial[] switchMaterials;
    LinearLayout linearLayout;

    NotificationManager manager;
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";
    public static Activity PushActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push);

        PushActivity = PushActivity.this;
        PushActivity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        switchMaterials = new SwitchMaterial[4];
        switchMaterials[0] = findViewById(R.id.switch_receipt_push);
        switchMaterials[1] = findViewById(R.id.switch_written_push);
        switchMaterials[2] = findViewById(R.id.switch_practical_push);
        switchMaterials[3] = findViewById(R.id.switch_success_push);

        linearLayout = findViewById(R.id.linear);
        confirm = findViewById(R.id.confirm);

        boolean[] state = new boolean[4];
        state[0] = PreferenceManager.getBoolean(PushActivity, "switch_receipt");
        state[1] = PreferenceManager.getBoolean(PushActivity, "switch_written");
        state[2] = PreferenceManager.getBoolean(PushActivity, "switch_practical");
        state[3] = PreferenceManager.getBoolean(PushActivity, "switch_success");

        for(int i = 0; i < state.length; i++) {
            final int count = i;
            if(state[i])
                switchMaterials[i].setChecked(true);
            else
                switchMaterials[i].setChecked(false);
            switchMaterials[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        switch (count) {
                            case 0 :
                                PreferenceManager.setBoolean(PushActivity, "switch_receipt", isChecked);
                                showNotice();
                                break;
                            case 1 :
                                PreferenceManager.setBoolean(PushActivity, "switch_written", isChecked);
                                break;
                            case 2 :
                                PreferenceManager.setBoolean(PushActivity, "switch_practical", isChecked);
                                break;
                            case 3 :
                                PreferenceManager.setBoolean(PushActivity, "switch_success", isChecked);
                                break;
                        }
                        Snackbar.make(linearLayout, "푸시알림 허용하셨습니다.", Snackbar.LENGTH_SHORT).show();
                    }
                    else {
                        switch (count) {
                            case 0 :
                                PreferenceManager.setBoolean(PushActivity, "switch_receipt", isChecked);
                                break;
                            case 1 :
                                PreferenceManager.setBoolean(PushActivity, "switch_written", isChecked);
                                break;
                            case 2 :
                                PreferenceManager.setBoolean(PushActivity, "switch_practical", isChecked);
                                break;
                            case 3 :
                                PreferenceManager.setBoolean(PushActivity, "switch_success", isChecked);
                                break;
                        }
                        Snackbar.make(linearLayout, "푸시알림 해제하셨습니다.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                PushActivity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }

    public void showNotice() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (manager.getNotificationChannel(CHANNEL_ID) == null)
                manager.createNotificationChannel(new NotificationChannel(
                        CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
                ));
            builder = new NotificationCompat.Builder(PushActivity, CHANNEL_ID);
        }
        else
            builder = new NotificationCompat.Builder(PushActivity, CHANNEL_ID);

        Intent intent = new Intent(PushActivity, PushActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(PushActivity, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle("자격증어플 입니다.")
                .setContentText("푸시알림에 동의하셨습니다.")
                .setSmallIcon(android.R.drawable.ic_menu_view)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        Notification noti = builder.build();
        manager.notify(2, noti);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE) // Do not close if you touch outer layout.
            return false;
        return true;
    }

    @Override
    public void onBackPressed() {
        // Blocking back button
        return;
    }
}
