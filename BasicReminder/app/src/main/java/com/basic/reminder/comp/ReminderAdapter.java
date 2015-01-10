package com.basic.reminder.comp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.basic.reminder.R;
import com.basic.reminder.model.Reminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rafi on 9/1/15.
 */
public class ReminderAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Reminder> mReminders = new ArrayList<>();
    private TextView mTime;
    private TextView mDate;
    private OnAlarmDeleteListener onAlarmDeleteListener;
    private ImageView mDelete;

    public ReminderAdapter(Context context, OnAlarmDeleteListener onAlarmDeleteListener,
                           ArrayList<Reminder> reminders) {
        this.mContext = context;
        this.onAlarmDeleteListener = onAlarmDeleteListener;
        this.mReminders = reminders;
    }

    @Override
    public int getCount() {
        return mReminders.size();
    }

    @Override
    public Object getItem(int position) {
        return mReminders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mReminders.get(position).getReminderId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_reminder, null);

            mTime = (TextView) convertView.findViewById(R.id.reminder_time);
            mDate = (TextView) convertView.findViewById(R.id.reminder_date);
            mDelete = (ImageView) convertView.findViewById(R.id.delete);

        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        final Reminder reminder = mReminders.get(position);
        Date date = new Date(reminder.getDateTime());

        String timeTxt = timeFormat.format(date);
        mTime.setText(timeTxt);

        String dateTxt = dateFormat.format(date);
        mDate.setText(dateTxt);

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlarmDeleteListener.onAlarmDelete(reminder);
            }
        });
        return convertView;
    }
}
