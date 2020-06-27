package ucs.trabalho3.github_events.src.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

// Tried using the AlarmManager to make the service be called forever, could not get it to work :(

// https://android-developers.googleblog.com/2018/10/modern-background-execution-in-android.html
// https://stackoverflow.com/questions/49233608/start-a-service-with-alarmmanager-at-specific-date-and-time-and-repeating
// https://stackoverflow.com/questions/58797621/how-to-run-a-foreground-service-for-10-seconds-every-5-minutes

public class AlarmService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Your Logic
        Toast.makeText(AlarmService.this, "Service is running ", Toast.LENGTH_SHORT).show();
        stopSelf();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this,0, new Intent(this,AlarmService.class),0);
        alarmManager.set(alarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5000), pendingIntent);
    }
}