package ucs.trabalho3.github_events.src.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;

import ucs.trabalho3.github_events.R;
import ucs.trabalho3.github_events.src.activity.MainActivity;

public class NotificationHelper {

    public static void showNotification(Context context, String title, String actor, String avatar_url) {
        final Context thisContext = context;

        final NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(actor)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);

        Glide.with(context)
                .asBitmap()
                .load(avatar_url)
                .circleCrop()
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                        if(resource != null) {
                            mBuilder.setLargeIcon(resource);
                            NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(thisContext);
                            mNotificationManager.notify(1, mBuilder.build());
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
            });


    }

}
