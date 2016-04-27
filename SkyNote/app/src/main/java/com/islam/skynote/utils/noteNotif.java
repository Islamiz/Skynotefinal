package com.islam.skynote.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.islam.skynote.Accueil;
import com.islam.skynote.R;
import com.islam.skynote.db.NoteDb;

import java.util.List;

/**
 * Created by islam on 26/04/2016.
 */


// partie qui gére la notif

public class noteNotif {

    protected Context mContext ;
    protected NoteDb _datebase;

    protected NotificationManager notificationManager;

    public noteNotif(Context context, NoteDb _db)
    {
        mContext = context;
        _datebase = _db;

        notificationManager =  (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }



    public void checkNotif()
    {
        notificationManager.cancelAll();
        // On recupere que les notification future ( Temps de la notif > au temps actuelle)
        List<noteObj> notifobj = _datebase.GetWhere(NoteDb.NOTE_NOTIF + " > " + System.currentTimeMillis()/1000);
        for(int i = 0; i <notifobj.size();i++)
        {
            // Faire la notification dans la future
            NotifEnattend(notifobj.get(i).Titre,notifobj.get(i).Description,notifobj.get(i).Notif_at);
        }
    }

    public void NotifEnattend(String title, String desrip, long time)
    {
        Intent intent = new Intent(mContext, Accueil.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(mContext);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(time*1000)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(desrip)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo(desrip)
                .setShowWhen(true);


        Intent notificationIntent = new Intent(mContext , ReceiveAlarm.class);

        notificationIntent.putExtra(ReceiveAlarm.NOTIFICATION_ID, (int)time);
        notificationIntent.putExtra(ReceiveAlarm.NOTIFICATION_TITLE, title);
        notificationIntent.putExtra(ReceiveAlarm.NOTIFICATION, b.build());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,time*1000,pendingIntent);

        Log.d("NOTIF", "Log at " + time  * 1000);
    }

    /*
    public void TestNotif ()
    {
        Intent intent = new Intent(mContext, Accueil.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(mContext);

        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Hearty365")
                .setContentTitle("Default notification")
                .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent)
                .setContentInfo("Info");


        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, b.build());
    }*/
}
