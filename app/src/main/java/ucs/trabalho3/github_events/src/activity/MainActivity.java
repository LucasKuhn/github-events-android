package ucs.trabalho3.github_events.src.activity;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ucs.trabalho3.github_events.R;
import ucs.trabalho3.github_events.src.adapter.EventsAdapter;
import ucs.trabalho3.github_events.src.model.Event;
import ucs.trabalho3.github_events.src.rest.ApiClient;
import ucs.trabalho3.github_events.src.rest.ApiInterface;
import ucs.trabalho3.github_events.src.service.ApiRequestService;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "github_events";
    public static Intent serviceIntent;
    public static RecyclerView recyclerView;
    public static String GithubUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        killBackgroundProcesses();

        serviceIntent = new Intent(this, ApiRequestService.class);
        startService(serviceIntent);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.eventsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        GithubUser = preferences.getString("github_username", "Adicione seu usuário nas configurações");
        TextView userNameTextView = findViewById(R.id.textView);
        userNameTextView.setText(GithubUser);


        if ( preferences.contains("github_username") ) {
            GithubUser = preferences.getString("github_username", "");
            CallApi(GithubUser);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        final String finalGithubUser = GithubUser;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallApi(finalGithubUser);
                Snackbar.make(view, "List updated", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void CallApi(final String GithubUser) {
        ApiInterface apiService = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);

        Call<List<Event>> call = apiService.getReceivedEvents(GithubUser);
                    call.enqueue(new Callback<List<Event>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                    int statusCode = response.code();

                    if (statusCode == 200) {
                        List<Event> events = response.body();
                        List<Event> filteredEvents = EventsAdapter.filterEvents(events, GithubUser);

                        if (filteredEvents.size() != 0) {
                            recyclerView.setAdapter(new EventsAdapter(filteredEvents, R.layout.list_item_event, getApplicationContext(), GithubUser));
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Event>> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("erro", t.toString());
                }
            });
    }

    private void killBackgroundProcesses() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        manager.killBackgroundProcesses("ucs.trabalho3.github_events");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void settingsButtonClick(MenuItem item) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
