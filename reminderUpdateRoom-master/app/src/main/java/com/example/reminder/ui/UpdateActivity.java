package com.example.reminder.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.reminder.R;
import com.example.reminder.model.MainViewModel;
import com.example.reminder.model.Reminder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText mTitle;
    private EditText mPlatform;
    private String mCurrentStatus;
    private Intent intent;
    private boolean update;

    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        update = false;
        setContentView(R.layout.activity_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //Init local variables
        mTitle = findViewById(R.id.editText_update);
        mPlatform = findViewById(R.id.Description);
        Spinner mStatus = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Status,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mStatus.setAdapter(adapter);
        mStatus.setOnItemSelectedListener(this);

        intent = getIntent();
        if(intent.getStringExtra(MainActivity.UPDATE_REMINDER) !=null){
            String text = intent.getStringExtra(MainActivity.UPDATE_REMINDER);
            String[] sections = text.split("-");
            if(sections.length >= 3) {
                mTitle.setText(sections[0]);
                mPlatform.setText(sections[1]);
                ArrayAdapter ar = (ArrayAdapter) mStatus.getAdapter();
                int itemPos = ar.getPosition(sections[2]);
                mStatus.setSelection(itemPos);
                update = true;
            }
        }
        FloatingActionButton fab = findViewById(R.id.FAB2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBack();
            }
        });
        /*button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendBack();
            }
        });*/
    }
    void sendBack(){
        Date dt = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("dd/mm/yyyy");
        String fd = sd.format(dt);
        String string = mTitle.getText().toString()+"-"+ mPlatform.getText()+"-"+mCurrentStatus+"-"+ fd;
        Reminder newReminder = new Reminder(string);

        if (update == false) {
            mMainViewModel.insert(newReminder);
        }
        else {
            mMainViewModel.update(newReminder);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mCurrentStatus = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
