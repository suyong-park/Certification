package com.example.certification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {

    public static Activity BookmarkActivity;
    private BookmarkAdapter mAdapter;
    public GestureDetector gesture_detector;
    List<Recycler_onething> mlist = new ArrayList<>();
    RecyclerView recyclerView;

    TextView blank;
    LinearLayout bookmark_layout;
    String[] components;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark);

        BookmarkActivity = BookmarkActivity.this;

        setTitle("북마크");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        blank = (TextView) findViewById(R.id.blank_components);
        bookmark_layout = (LinearLayout) findViewById(R.id.bookmark_layout);

        recyclerView = (RecyclerView) findViewById(R.id.title_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookmarkActivity, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new BookmarkAdapter();
        recyclerView.setAdapter(mAdapter);

        addBookmark();

        components = new String[mAdapter.getItemCount()];
        for(int i = components.length - 1; i >= 0; i--)
            components[i] = mlist.get(i).getTitle();

        if(mAdapter.getItemCount() == 0)
            blank.setVisibility(View.VISIBLE);
        else
            blank.setVisibility(View.GONE);

        gesture_detector = new GestureDetector(BookmarkActivity, new GestureDetector.SimpleOnGestureListener() {
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
                    Intent it = new Intent(BookmarkActivity, CertificationActivity.class);
                    it.putExtra("name", mAdapter.getRecycler_title(currentPos).getTitle());
                    it.putExtra("bookmark", true);
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

        enableSwipeToDelete(BookmarkActivity);
    }

    private void enableSwipeToDelete(Activity activity) {
        SwipeToDeleteCallback swipe = new SwipeToDeleteCallback(activity) {

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final int position = viewHolder.getAdapterPosition();
                int last_num, remove_position;
                String text;

                last_num = PreferenceManager.getInt(BookmarkActivity, "");
                for(int i = 0; i <= last_num; i++) {
                    String temp = String.valueOf(i);
                    text = PreferenceManager.getString(BookmarkActivity, temp);
                    if (text.equals(mlist.get(position).getTitle())) {
                        remove_position = position + 1;
                        mlist.remove(position);
                        PreferenceManager.removeKey(BookmarkActivity, temp);
                        for(int j = remove_position; j < mAdapter.getItemCount() + 1; j++) {
                            String temp_text = PreferenceManager.getString(BookmarkActivity, String.valueOf(j));
                            PreferenceManager.setString(BookmarkActivity, String.valueOf(j - 1), temp_text);
                        }
                        PreferenceManager.setInt(BookmarkActivity, "", mAdapter.getItemCount() - 1);
                        // setting newest number after delete bookmark
                        break;
                    }
                }

                if(mAdapter.getItemCount() == 0) {
                    PreferenceManager.removeKey(BookmarkActivity, "");
                    Snackbar.make(bookmark_layout, "북마크가 모두 지워졌군요!", Snackbar.LENGTH_SHORT).show();
                    blank.setVisibility(View.VISIBLE);
                }
                else
                    Snackbar.make(bookmark_layout, "삭제되었습니다.", Snackbar.LENGTH_SHORT).show();

                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= 26)
                    vibrator.vibrate(VibrationEffect.createOneShot(100, 30));
                else
                    vibrator.vibrate(100);
                mAdapter.notifyItemRemoved(position);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tab_bookmark, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // If touch the back key on tool bar, then finish present activity.
                return true;
            case R.id.help:
                Snackbar.make(bookmark_layout, "북마크를 터치하면 해당 자격증 화면으로 이동합니다.", Snackbar.LENGTH_LONG)
                        .setAction("다음", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Snackbar.make(v, "북마크를 왼쪽으로 슬라이스하여 삭제할 수 있습니다.", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addBookmark() {

        int last_num;
        String text;

        last_num = PreferenceManager.getInt(BookmarkActivity, "");
        for(int i = 0; i <= last_num; i++) {
            String temp = String.valueOf(i);
            text = PreferenceManager.getString(BookmarkActivity, temp);
            Log.d("북마크 현재 상황", "현재 " + temp + "번째에 있는 데이터 값은 " + text + "입니다.");
            if(text.equals(null) || text.equals(""))
                continue;
            mAdapter.add(new Recycler_onething(text));
        }
    }

    class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
            }

            public void setData(Recycler_onething data) {
                title.setText(data.getTitle());
            }
        }

        public void add(Recycler_onething item) {
            mlist.add(item);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_only_title, viewGroup, false);
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

        public Recycler_onething getRecycler_title(int pos) {
            return mlist.get(pos);
        }
    }

    abstract class SwipeToDeleteCallback extends ItemTouchHelper.Callback {

        Context mContext;
        private Paint mClearPaint;
        private ColorDrawable mBackground;
        private int backgroundColor;
        private Drawable deleteDrawable;
        private int intrinsicWidth;
        private int intrinsicHeight;

        SwipeToDeleteCallback(Context context) {
            mContext = context;
            mBackground = new ColorDrawable();
            backgroundColor = Color.parseColor("#b80f0a");
            mClearPaint = new Paint();
            mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            deleteDrawable = ContextCompat.getDrawable(mContext, R.drawable.icon_delete);
            intrinsicWidth = deleteDrawable.getIntrinsicWidth();
            intrinsicHeight = deleteDrawable.getIntrinsicHeight();
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(0, ItemTouchHelper.LEFT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return false;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            View itemView = viewHolder.itemView;
            int itemHeight = itemView.getHeight();

            boolean isCancelled = dX == 0 && !isCurrentlyActive;
            if (isCancelled) {
                clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                return;
            }

            mBackground.setColor(backgroundColor);
            mBackground.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
            mBackground.draw(c);

            int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
            int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
            int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
            int deleteIconRight = itemView.getRight() - deleteIconMargin;
            int deleteIconBottom = deleteIconTop + intrinsicHeight;

            deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
            deleteDrawable.draw(c);

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
            c.drawRect(left, top, right, bottom, mClearPaint);
        }

        @Override
        public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
            return 0.7f;
        }
    }
}