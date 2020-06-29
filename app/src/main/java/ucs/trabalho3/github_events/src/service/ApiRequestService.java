package ucs.trabalho3.github_events.src.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ucs.trabalho3.github_events.R;
import ucs.trabalho3.github_events.src.adapter.EventsAdapter;
import ucs.trabalho3.github_events.src.model.Event;
import ucs.trabalho3.github_events.src.rest.ApiClient;
import ucs.trabalho3.github_events.src.rest.ApiInterface;

// https://stackoverflow.com/questions/13820596/start-android-service-after-every-5-minutes
// https://stackoverflow.com/questions/10420358/android-periodic-background-service-advice
// https://www.vogella.com/tutorials/AndroidServices/article.html

public class ApiRequestService extends Service {

    public static final int interval = 60000;  //interval between two services in MS ( 1 minute )
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = new Timer();    //timer handling
    public int count = 0;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

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

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(ApiRequestService.this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
        /* By removing the next line I was able to make it run forever */
        super.onDestroy();
        mTimer.cancel();
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
                    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

                    if ( preferences.contains("github_username") ) {
                        String githubUser = preferences.getString("github_username", "username");
                        Call<List<Event>> call = apiService.getReceivedEvents(githubUser);

                        call.enqueue(new Callback<List<Event>>() {
                            @Override
                            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                                int statusCode = response.code();
                                List<Event> events = response.body();

                                String eventId = events.get(0).getId();
                                String lastEventId = preferences.getString("last_event_id", "id");

                                if (eventId.equals(lastEventId) == false) {
                                    editor.putString("last_event_id", eventId);
                                    editor.commit();

                                    NotificationHelper.showNotification(getApplicationContext(), events.get(0).getType(), events.get(0).getActor().getLogin());
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Event>> call, Throwable t) {
                                // Log error here since request failed
                                Log.e("erro", t.toString());
                            }
                        });
                    }


                    // display toast
                    Log.w("SERVICE", "Count " + count);
                    Toast.makeText(ApiRequestService.this, "Service is running " + count, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}