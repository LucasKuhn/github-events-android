package ucs.trabalho3.github_events.src.service;

import android.content.Context;

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

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
