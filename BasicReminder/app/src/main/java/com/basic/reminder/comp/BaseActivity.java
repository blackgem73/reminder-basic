package com.basic.reminder.comp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.basic.reminder.model.Reminder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafi on 9/1/15.
 */
public class BaseActivity extends ActionBarActivity {

    public static final int REQ_SET_ALARM = 13;

    private static final String APP_PREFS = "prefs_app";
    private static final String PREF_REMINDERS = "prefs_reminders";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected PendingIntent getAlarmPendingIntent(String reminderId) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.addCategory(reminderId);
        return PendingIntent.getBroadcast(getBaseContext(), REQ_SET_ALARM, intent, 0);
    }

    private SharedPreferences getSharedPrefs() {
        return getSharedPreferences(APP_PREFS, MODE_PRIVATE);
    }

    protected ArrayList<Reminder> getReminders() {
        String s = getSharedPrefs().getString(PREF_REMINDERS, "");
        if (s.isEmpty()) {
            return new ArrayList<>();
        }
        Type listType = new TypeToken<List<Reminder>>() {
        }.getType();
        return new Gson().fromJson(s, listType);
    }

    protected void saveReminder(Reminder reminder) {
        ArrayList<Reminder> reminders = getReminders();
        reminders.add(0, reminder);
        saveReminderList(reminders);
    }

    protected void saveReminderList(ArrayList<Reminder> reminders){
        String s = new Gson().toJson(reminders);
        saveReminderList(s);
    }

    private void saveReminderList(String s) {
        getSharedPrefs().edit().putString(PREF_REMINDERS, s).apply();
    }

}
