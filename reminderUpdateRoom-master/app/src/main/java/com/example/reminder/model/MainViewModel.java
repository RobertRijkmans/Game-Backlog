package com.example.reminder.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.reminder.database.ReminderRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private ReminderRepository mRepository;

    private LiveData<List<Reminder>> mReminders;

    public MainViewModel(@NonNull Application application) {

        super(application);

        mRepository = new ReminderRepository(application.getApplicationContext());

        mReminders = mRepository.getAllReminders();

    }

    public LiveData<List<Reminder>> getReminders() {

        return mReminders;

    }

    public void insert(Reminder reminder) {

        mRepository.insert(reminder);

    }

    public void update(Reminder reminder) {

        mRepository.update(reminder);

    }

    public void delete(Reminder reminder) {

        mRepository.delete(reminder);

    }

}
