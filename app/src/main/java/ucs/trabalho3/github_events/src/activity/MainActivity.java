package ucs.trabalho3.github_events.src.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

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
import ucs.trabalho3.github_events.src.model.Event;
import ucs.trabalho3.github_events.src.rest.ApiClient;
import ucs.trabalho3.github_events.src.rest.ApiInterface;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String GithubUser = preferences.getString("github_username", "Adicione seu usuário nas configurações");
        TextView userNameTextview = findViewById(R.id.textView);
        userNameTextview.setText(GithubUser);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<Event>> call = apiService.getReceivedEvents(GithubUser);
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                int statusCode = response.code();
                List<Event> events = response.body();
                //recyclerView.setAdapter(new PostsAdapter(posts, R.layout.list_item_post, getApplicationContext()));
                Log.d("resposta", events.get(0).getPayload().getAction());
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                //mostraAlerta("Erro",t.toString());
                // Log error here since request failed
                Log.e("erro", t.toString());
            }
        });

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
}
