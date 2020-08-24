package com.example.certification;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.DayOfWeek;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    public static Activity CalendarActivity;
    private CalendarAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        CalendarActivity = CalendarActivity.this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("캘린더");

        MaterialCalendarView materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.calendar_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CalendarActivity, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new CalendarActivity.CalendarAdapter();
        recyclerView.setAdapter(mAdapter);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(DayOfWeek.SUNDAY)
                .setMinimumDate(CalendarDay.from(2019, 1, 1))
                .setMaximumDate(CalendarDay.from(2021, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        //HashMap<String, String> test_date = Broadcast.HashMap_calendar(calendarActivity).get(0);
        Log.d("값을 알아봅시다 : ", "" + Broadcast.HashMap_calendar(CalendarActivity).get(0));

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator()
                //oneDayDecorator
                );

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Toast.makeText(CalendarActivity, "날짜변경 : " + date.getDate(), Toast.LENGTH_LONG).show();
            }
        });

        addCalendar();
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

    public void addCalendar() {

        int last_num;
        String title, date;

        last_num = PreferenceManager.getInt(CalendarActivity, "");
        for(int i = 0; i <= last_num; i++) {
            String temp = String.valueOf(i);
            title = PreferenceManager.getString(CalendarActivity, temp);
            date = PreferenceManager.getString(CalendarActivity, temp + "_date");
            String[] detail_date = date.split(",");
            Log.d("배열의 크기는? : ", "" + detail_date.length);
            if(title.equals(null) || title.equals(""))
                continue;
            for(int j = 0; j < detail_date.length; j++) {
                Log.d("원소 구성을 알아봅시다 : ", "" + detail_date[j]);
                mAdapter.add(new Recycler_calendar(title, detail_date[j]));
            }
        }
    }

    class CalendarAdapter extends RecyclerView.Adapter<CalendarActivity.CalendarAdapter.ViewHolder> {

        List<Recycler_calendar> mlist = new ArrayList<>();

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView title;
            TextView date;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.description);
                date = (TextView) itemView.findViewById(R.id.link);
            }

            public void setData(Recycler_calendar data) {
                title.setText(data.getTitle());
                date.setText(data.getDate());
            }
        }

        public void add(Recycler_calendar item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public CalendarActivity.CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.calendar, viewGroup, false);
            return new CalendarActivity.CalendarAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CalendarActivity.CalendarAdapter.ViewHolder viewholder, int position) { // Define properties of Recycler View items.
            viewholder.setData(mlist.get(position));
        }

        @Override
        public int getItemCount() { // Count of Recycler View items.
            return mlist.size();
        }
    }
}

class SundayDecorator implements DayViewDecorator {
    // color add to Sunday

    private final Calendar calendar = Calendar.getInstance();

    SundayDecorator() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        int weekDay = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(day.getDate().toString());
            calendar.setTime(date);
            weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return weekDay == Calendar.SUNDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED));
    }
}

class SaturdayDecorator implements DayViewDecorator {
    // color add to Sunday

    private final Calendar calendar = Calendar.getInstance();

    public SaturdayDecorator() {
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        int weekDay = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(day.getDate().toString());
            calendar.setTime(date);
            weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return weekDay == Calendar.SATURDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.BLUE));
    }
}
