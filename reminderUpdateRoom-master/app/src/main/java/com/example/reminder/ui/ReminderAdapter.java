package com.example.reminder.ui;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.reminder.R;
import com.example.reminder.model.Reminder;

import java.util.List;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>  {
    private List<Reminder> mReminders;
    private MainActivity main;

    public static class ReminderViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitle;
        public TextView mPlatform;
        public TextView mStatus;
        public TextView mDate;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.TitleText);
            mPlatform = itemView.findViewById(R.id.Platform);
            mStatus = itemView.findViewById(R.id.Status);
            mDate = itemView.findViewById(R.id.Date);
        }
    }

    public ReminderAdapter(List<Reminder> mReminders,MainActivity origin) {
        this.mReminders = mReminders;
        main = origin;
    }

    @NonNull
    @Override
    public ReminderAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        ReminderAdapter.ViewHolder viewHolder = new ReminderAdapter.ViewHolder(view);
//invalte recyclerview with a card view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.new_card_view, viewGroup,false);
        ReminderViewHolder reView = new ReminderViewHolder(v);
        return reView;
    }

    @Override
    public void onBindViewHolder(@NonNull final ReminderAdapter.ReminderViewHolder viewHolder, int i) {
//fill card with text
        final Reminder currentItem = mReminders.get(i);
        final String mainText= currentItem.getReminderText();
        String[] m = mainText.split("-");
        if(m.length >=4) {
            viewHolder.mTitle.setText(m[0]);
            viewHolder.mPlatform.setText(m[1]);
            viewHolder.mStatus.setText(m[2]);
            viewHolder.mDate.setText(m[3]);
        }
    }

    @Override
    public int getItemCount() {
        return mReminders.size();
    }

    public void swapList (List<Reminder> newList) {
        mReminders = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }

}
