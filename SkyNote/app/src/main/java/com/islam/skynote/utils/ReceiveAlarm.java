package com.islam.skynote.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by islam  .
 */
// classe qui permet d'intéragire avec l'horloe du systéme android afin d'envoyer la notif a la bonne heure demander
public class ReceiveAlarm extends BroadcastReceiver {

    public static String NOTIFICATION_TITLE = "notification-title";
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("ALARM","LOG ME IN THE TIME");
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        String title = intent.getStringExtra(NOTIFICATION_TITLE);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        Notification not = intent.getParcelableExtra(NOTIFICATION);

        notificationManager.notify(title,id,not);
    }
}
