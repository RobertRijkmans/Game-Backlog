package com.example.reminder.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.reminder.model.Reminder;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReminderRepository {


    private ReminderRoomDatabase mAppDatabase;

    private ReminderDao mReminderDao;

    private LiveData<List<Reminder>> mReminders;

    private Executor mExecutor = Executors.newSingleThreadExecutor();


    public ReminderRepository (Context context) {

        mAppDatabase = ReminderRoomDatabase.getDatabase(context);

        mReminderDao = mAppDatabase.reminderDao();

        mReminders = mReminderDao.getAllReminders();

    }


    public LiveData<List<Reminder>> getAllReminders() {

        return mReminders;

    }


    public void insert(final Reminder reminder) {

        mExecutor.execute(new Runnable() {

            @Override

            public void run() {

                mReminderDao.insertReminder(reminder);

            }

        });

    }


    public void update(final Reminder reminder) {

        mExecutor.execute(new Runnable() {

            @Override

            public void run() {

                mReminderDao.updateReminder(reminder);

            }

        });

    }


    public void delete(final Reminder reminder) {

        mExecutor.execute(new Runnable() {

            @Override

            public void run() {

                mReminderDao.deleteReminder(reminder);

            }

        });

    }

}