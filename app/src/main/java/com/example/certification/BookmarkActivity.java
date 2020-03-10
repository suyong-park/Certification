package com.example.certification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    private BookmarkAdapter mAdapter;
    public GestureDetector gesture_detector;
    List<Recycler_title> mlist = new ArrayList<>();

    TextView blank;
    LinearLayout bookmark_layout;

    String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);

        setTitle("북마크");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        blank = (TextView) findViewById(R.id.blank_components);
        bookmark_layout = (LinearLayout) findViewById(R.id.bookmark_layout);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.title_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new BookmarkAdapter();
        recyclerView.setAdapter(mAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        addBookmark();

        if(mAdapter.getItemCount() == 0)
            blank.setVisibility(View.VISIBLE);
        else
            blank.setVisibility(View.GONE);

        gesture_detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener()
        {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());

                if(childView != null && gesture_detector.onTouchEvent((e))) {
                    int currentPos = rv.getChildAdapterPosition(childView);
                    Intent it = new Intent(BookmarkActivity.this, CertificationActivity.class);
                    it.putExtra("name", mAdapter.getRecycler_title(currentPos).getTitle());
                    it.putExtra("num", num);
                    startActivity(it);
                    return true;
                }
                return false;
            }
            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_bookmarksort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // If touch the back key on tool bar, then finish present activity.
                return true;
            case R.id.word_sort:
                Toast.makeText(getApplicationContext(), "글자순", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.time_sort:
                Toast.makeText(getApplicationContext(), "시간순", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addBookmark() {

        int max;
        String text;

        try {
            max = Integer.parseInt(PreferenceManager.getString_max(getApplicationContext(), "value")); // max value

            for(int i = 1; i <= max; i++) {
                String temp = String.valueOf(i);
                text = PreferenceManager.getString(getApplicationContext(), temp);
                if(!text.equals("")) {
                    mAdapter.add(new Recycler_title(text));
                    num = temp;
                }
            }
        }
        catch (NumberFormatException e) {
        }
    }

    class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.item);
            }

            public void setData(Recycler_title data) {
                title.setText(data.getTitle());
            }
        }

        public void add(Recycler_title item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_title_bookmark, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) { // Define properties of Recycler View items.
            viewholder.setData(mlist.get(position));
        }

        @Override
        public int getItemCount() { // Count of Recycler View items.
            return mlist.size();
        }

        public Recycler_title getRecycler_title(int pos) {
            return mlist.get(pos);
        }
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            int max = Integer.parseInt(PreferenceManager.getString_max(getApplicationContext(), "value")); // max value

            for (int i = 1; i <= max; i++) {
                String temp_num = String.valueOf(i);
                String tmp = PreferenceManager.getString(getApplicationContext(), temp_num);
                if (tmp.equals(mlist.get(position).getTitle())) {
                    mlist.remove(position);
                    PreferenceManager.removeKey(getApplicationContext(), temp_num);
                    break;
                }
            }
            if(mAdapter.getItemCount() == 0) {
                Snackbar.make(bookmark_layout, "북마크가 모두 지워졌군요!", Snackbar.LENGTH_SHORT).show();
                blank.setVisibility(View.VISIBLE);
            }

            mAdapter.notifyItemRemoved(position);
        }
    };
}
