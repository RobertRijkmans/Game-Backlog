package com.example.reminder.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.reminder.R;
import com.example.reminder.database.ReminderRoomDatabase;
import com.example.reminder.model.MainViewModel;
import com.example.reminder.model.Reminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

	//instance variables
	private List<Reminder> mReminders;
	private ReminderAdapter mAdapter;
	private RecyclerView mRecyclerView;
	private EditText mNewReminderText;

	boolean forDelete = false;
	private GestureDetector mGestureDetector;
	private MainViewModel mMainViewModel;

	//Constants used when calling the update activity
	public static final String EXTRA_REMINDER = "Reminder";
	public static final int REQUESTCODE = 1234;
	public static final int UPDATECODE = 4321;
	public static final String UPDATE_REMINDER = "Updating";
	private int mModifyPosition;

	private ReminderRoomDatabase db;
	private Executor executor = Executors.newSingleThreadExecutor();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = ReminderRoomDatabase.getDatabase(this);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		//Initialize the instance variables
		mRecyclerView = findViewById(R.id.recyclerView);
		mReminders = new ArrayList<>();

		mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

		mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				return true;
			}
		});

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getReminders().observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(@Nullable List<Reminder> reminders) {
                mReminders = reminders;
                updateUI();
            }
        });



        FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
				startActivityForResult(intent, REQUESTCODE);
			}
		});

		ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
				new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
					@Override
					public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
						return false;
					}

					//Called when a user swipes left or right on a ViewHolder
					@Override
					public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
						//Get the index corresponding to the selected position
						int position = (viewHolder.getAdapterPosition());
                        mMainViewModel.delete(mReminders.get(position));
						mReminders.remove(position);
						mAdapter.notifyItemRemoved(position);
						Log.d("deleting","can delete");
					}
				};

		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
		itemTouchHelper.attachToRecyclerView(mRecyclerView);
		mRecyclerView.addOnItemTouchListener(this);
	}

	private void updateUI() {
		if (mAdapter == null) {
			mAdapter = new ReminderAdapter(mReminders,this);
			mRecyclerView.setAdapter(mAdapter);
		} else {
			mAdapter.swapList(mReminders);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.Delete) {
		    for(int i = mReminders.size()-1; i >=0 ;i--){
                mMainViewModel.delete(mReminders.get(i));
            }
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
		View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
		if (child != null) {
			int mAdapterPosition = recyclerView.getChildAdapterPosition(child);
			if (mGestureDetector.onTouchEvent(motionEvent)) {
				Intent intent = new Intent(this,UpdateActivity.class);
				intent.putExtra(UPDATE_REMINDER,mReminders.get(mAdapterPosition).getReminderText());
				startActivityForResult(intent, UPDATECODE);
			}
		}
		return false;
	}

	@Override
	public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

	}
	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean b) {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}
}
