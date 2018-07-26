package com.jpp.mall.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.jpp.mall.R;
import com.jpp.mall.acitivity.NotifyCenterActivity;


/**
 * Created by Elysion on 2017/6/21.
 */

public class NotificationUtil {

    private static NotificationManager mNotificationManager;
    private static NotificationCompat.Builder mBuilder;

    public static void setNotify(Context context, String title, String message, String ticker) {

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(context);
        }

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Uri uri  = Uri.parse("android.resource://" + context.getPackageName() + "/"+ R.raw.demodave);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setContentIntent(getDefaultIntent(context, Notification.FLAG_AUTO_CANCEL))
                .setTicker(ticker)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .setOngoing(false)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSound(uri)
                .setVibrate(new long[]{0, 300, 500, 700})
                .setLargeIcon(bitmap)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher);
        mNotificationManager.notify(0, mBuilder.build());
    }

    public static PendingIntent getDefaultIntent(Context context, int flags) {
        Intent appIntent = new Intent(context, NotifyCenterActivity.class);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//关键的一步，设置启动模式
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, flags);
        return pendingIntent;
    }

    public void init(){

    }
}
