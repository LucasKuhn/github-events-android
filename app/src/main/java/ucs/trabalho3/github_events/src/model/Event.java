package ucs.trabalho3.github_events.src.model;

import android.os.Build;
import android.text.format.DateUtils;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class Event {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("actor")
    @Expose
    private Actor actor;
    @SerializedName("payload")
    @Expose
    private Payload payload;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public String getCreatedAt() { return created_at; }

    // https://stackoverflow.com/questions/2201925/converting-iso-8601-compliant-string-to-java-util-date
    // https://stackoverflow.com/questions/3859288/how-to-calculate-time-ago-in-java
    // https://stackoverflow.com/questions/35858608/how-to-convert-time-to-time-ago-in-android
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTimeAgo() {
        Date date = Date.from(Instant.parse(created_at));
        long time = date.getTime();
        long now = System.currentTimeMillis();
        CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        return ago.toString();
    }

    public String getUrl() {
        if (type.equals("PullRequestEvent")) {
            return getPayload().getPullRequest().getHtmlUrl();
        }
        return "";
    }

}

