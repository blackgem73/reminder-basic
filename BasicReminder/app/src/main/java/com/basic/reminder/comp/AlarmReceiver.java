package com.basic.reminder.comp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.basic.reminder.R;

/**
 * Created by rafi on 8/1/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent playIntent = new Intent();
        playIntent.setAction(AlarmMusicService.ACTION_ALARM_REMINDER);
        playIntent.putExtra(AlarmMusicService.ACTION_PLAY, true);
        context.sendBroadcast(playIntent);
        showNotification(context);

//        showCustomNotification(context);
    }

    private void showNotification(Context context){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("Reminder!").
                setSmallIcon(R.drawable.reminder_basic_icon)
                .setAutoCancel(false)
                .setOngoing(true)
                .addAction(getSnoozeAction(context, AlarmMusicService.ACTION_SNOOZE))
                .addAction(getCloseAction(context, AlarmMusicService.ACTION_CLOSE));

        Notification note = builder.build();
        note.defaults |= Notification.DEFAULT_VIBRATE;

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(AlarmMusicService.ALARM_NOTIF_ID, note);
    }

    private NotificationCompat.Action getSnoozeAction(Context context, String actionSnooze) {
        Intent intent = new Intent(AlarmMusicService.ACTION_ALARM_REMINDER);
        intent.putExtra(actionSnooze, true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 4, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action(
                R.drawable.snooze_icon, "Snooze", pendingIntent);
    }

    private NotificationCompat.Action getCloseAction(Context context, String action) {

        Intent intent = new Intent(AlarmMusicService.ACTION_ALARM_REMINDER);
        intent.putExtra(action, true);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 5, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action(
                R.drawable.alarm_close, "Close", pendingIntent);
    }
}
