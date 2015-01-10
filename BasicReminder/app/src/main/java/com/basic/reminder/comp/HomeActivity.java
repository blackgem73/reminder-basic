package com.basic.reminder.comp;

import android.app.AlarmManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.reminder.R;
import com.basic.reminder.model.Reminder;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.DynamicListViewWrapper;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.util.ArrayList;


public class HomeActivity extends BaseActivity implements OnDismissCallback, OnAlarmDeleteListener {

    private static final int REQ_MAKE_ALARM_ACT = 7;

    public static final String RESULT_DATA = "result_data";

    private DynamicListView mAlarmsLv;
    private ArrayList<Reminder> mReminders = new ArrayList<>();
    private TextView mSetAlarmTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSetAlarmTv = (TextView) findViewById(R.id.set_alarm_tv);
        mAlarmsLv = (DynamicListView) findViewById(R.id.alarms_lv);

        mReminders = getReminders();
        mAlarmsLv.enableSwipeToDismiss(this);

        mSetAlarmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSetAlarmActivity();
            }
        });

        invalidateListView();
        setListShown(!mReminders.isEmpty());
        startMusicService();
    }

    private void startMusicService() {

        Intent intent = new Intent(this, AlarmMusicService.class);
        startService(intent);
    }

    private void invalidateListView() {
        ReminderAdapter reminderAdapter = new ReminderAdapter(this, this, mReminders);
        mAlarmsLv.setAdapter(reminderAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            launchSetAlarmActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void launchSetAlarmActivity() {
        Intent intent = new Intent(this, SetAlarmActivity.class);
        startActivityForResult(intent, REQ_MAKE_ALARM_ACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_MAKE_ALARM_ACT && resultCode == RESULT_OK) {
            Reminder reminder = data.getParcelableExtra(RESULT_DATA);
            saveReminder(reminder);

            if (mReminders.isEmpty()) {
                setListShown(true);
            }
            mReminders.add(0, reminder);
            invalidateListView();
            Toast.makeText(this, "Reminder added", Toast.LENGTH_SHORT).show();
        }
    }

    private void setListShown(boolean b) {
        mAlarmsLv.setVisibility(b ? View.VISIBLE : View.GONE);
        mSetAlarmTv.setVisibility(b ? View.GONE : View.VISIBLE);
    }

    private void onReminderDelete(Reminder reminder) {

        String reminderId = String.valueOf(reminder.getReminderId());
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.cancel(getAlarmPendingIntent(reminderId));

        mReminders.remove(reminder);
        saveReminderList(mReminders);
        invalidateListView();

        if (mReminders.isEmpty()) {
            setListShown(false);
        }
    }

    @Override
    public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] ints) {
        Reminder reminder = mReminders.get(ints[0]);
        onReminderDelete(reminder);
    }

    @Override
    public void onAlarmDelete(Reminder reminder) {
        onReminderDelete(reminder);
    }
}
