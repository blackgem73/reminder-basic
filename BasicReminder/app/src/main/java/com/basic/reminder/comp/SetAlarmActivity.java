
package com.basic.reminder.comp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basic.reminder.R;
import com.basic.reminder.model.Reminder;

import java.util.Calendar;

/**
 * Created by rafi on 8/1/15.
 */
public class SetAlarmActivity extends BaseActivity{

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        mDatePicker = (DatePicker) findViewById(R.id.date_picker);
        mTimePicker = (TimePicker) findViewById(R.id.time_picker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_set_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_set_alarm) {
            populateAlarm();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateAlarm() {

        Calendar calendar = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();

        calendar.set(mDatePicker.getYear(),
                mDatePicker.getMonth(),
                mDatePicker.getDayOfMonth(),
                mTimePicker.getCurrentHour(),
                mTimePicker.getCurrentMinute(),
                00);

        if (calendar.compareTo(currentDate) <= 0) {
            Toast.makeText(this, R.string.invalid_date, Toast.LENGTH_SHORT).show();
            return;
        }
        setAlarm(calendar);
    }

    private void setAlarm(Calendar calendar) {

        long catId = calendar.getTimeInMillis();
        long timeInMillis = calendar.getTimeInMillis();

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.addCategory(String.valueOf(catId));
        PendingIntent pendingIntent = getAlarmPendingIntent(String.valueOf(timeInMillis));
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

        Reminder reminder = new Reminder();
        reminder.setDesc("Alarm");
        reminder.setActive(true);
        reminder.setDateTime(calendar.getTimeInMillis());
        reminder.setReminderId(catId);

        Intent result = new Intent();
        result.putExtra(HomeActivity.RESULT_DATA, reminder);
        setResult(RESULT_OK, result);
        finish();
    }
}
