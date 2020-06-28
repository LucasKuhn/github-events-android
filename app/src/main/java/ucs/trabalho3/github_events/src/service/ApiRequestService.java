package ucs.trabalho3.github_events.src.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

// https://stackoverflow.com/questions/13820596/start-android-service-after-every-5-minutes
// https://stackoverflow.com/questions/10420358/android-periodic-background-service-advice
// https://www.vogella.com/tutorials/AndroidServices/article.html

public class ApiRequestService extends Service {

    public static final int interval = 5000;  //interval between two services in MS ( 5 seconds )
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = new Timer();    //timer handling
    public int count = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, interval);   //Schedule task
    }

    @Override
    public void onDestroy() {
        Toast.makeText(ApiRequestService.this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
        /* By removing the next line I was able to make it run forever */
        // super.onDestroy();
        // mTimer.cancel();
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            count++;
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // Our code to call the API / show notification could be made here

                    // display toast
                    Log.w("SERVICE", "Count " + count);
                    Toast.makeText(ApiRequestService.this, "Service is running " + count, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}