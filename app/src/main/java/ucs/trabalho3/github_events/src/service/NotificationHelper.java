package ucs.trabalho3.github_events.src.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ucs.trabalho3.github_events.R;
import ucs.trabalho3.github_events.src.activity.MainActivity;

public class NotificationHelper {

    public static void showNotification(Context context, String title, String actor) {

        NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(actor)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);


        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
