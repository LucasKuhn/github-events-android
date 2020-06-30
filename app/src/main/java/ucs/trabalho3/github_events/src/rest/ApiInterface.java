package ucs.trabalho3.github_events.src.rest;

import androidx.preference.PreferenceManager;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import ucs.trabalho3.github_events.src.model.Event;

public interface ApiInterface {
    @GET("users/{username}/received_events")
    Call<List<Event>> getReceivedEvents(@Path("username") String username, @Header("If-None-Match") String ETag);
}
