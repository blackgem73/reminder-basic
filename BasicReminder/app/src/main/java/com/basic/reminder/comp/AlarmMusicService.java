package com.basic.reminder.comp;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.basic.reminder.R;

import java.util.Calendar;

/**
 * Created by rafi on 10/1/15.
 */
public class AlarmMusicService extends Service {

    public static final int ALARM_NOTIF_ID = 233;
    private MediaPlayer mPlayer;
    public static final String ACTION_ALARM_REMINDER = "action_alarm_reminder_command";

    public static final String ACTION_PLAY = "action_play_alarm";
    public static final String ACTION_SNOOZE = "action_snooze_alarm";
    public static final String ACTION_CLOSE = "action_close_alarm";

    public static final long SNOOZE_TIME = 15 * 60 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerReceiver(mAlarmCommandReceiver, new IntentFilter(ACTION_ALARM_REMINDER));
        return START_STICKY;
    }

    protected BroadcastReceiver mAlarmCommandReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.hasExtra(ACTION_PLAY)) {
                playMusic();
            } else if (intent.hasExtra(ACTION_SNOOZE)) {
                onSnoozeCommand();
            } else if (intent.hasExtra(ACTION_CLOSE)) {
                stopMusic();
                cancelAlarmNotif();
            }
        }
    };

    private void onSnoozeCommand() {

        stopMusic();
        Intent alarmIntent = new Intent(AlarmMusicService.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),
                BaseActivity.REQ_SET_ALARM, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() + SNOOZE_TIME);
        calendar.set(Calendar.SECOND, 00);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Snoozed", Toast.LENGTH_SHORT).show();
        cancelAlarmNotif();
    }

    private void playMusic() {

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 50, 0);

        mPlayer = MediaPlayer.create(this, R.raw.reminder_tone);

        try {

            if (mPlayer.isPlaying()) {
                return;
            }

            float volume = (float) (1 - (Math.log(100 - 80) / Math.log(100)));
            mPlayer.setVolume(volume, volume);
            mPlayer.setLooping(true);
            mPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        try {
            if (mPlayer != null) {
                if (mPlayer.isPlaying()) {
                    mPlayer.stop();
                }
                mPlayer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelAlarmNotif() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(ALARM_NOTIF_ID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mAlarmCommandReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
