package ucs.trabalho3.github_events.src.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ucs.trabalho3.github_events.R;
import ucs.trabalho3.github_events.src.activity.MainActivity;
import ucs.trabalho3.github_events.src.activity.WebViewActivity;
import ucs.trabalho3.github_events.src.model.Event;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.PostViewHolder>  {

    private List<Event> events;
    private int rowLayout;
    private Context context;

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout eventsLayout;
        TextView eventType;
        TextView eventActor;
        TextView eventTime;
        TextView prAction;
        ImageView actorAvatar;

        public PostViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            eventsLayout = (LinearLayout) v.findViewById(R.id.events_layout);
            eventType = (TextView) v.findViewById(R.id.eventType);
            eventActor = (TextView) v.findViewById(R.id.eventActor);
            eventTime = (TextView) v.findViewById(R.id.eventTime);
            actorAvatar = (ImageView) v.findViewById(R.id.actorAvatar);
            prAction = (TextView) v.findViewById(R.id.prAction);
        }

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), WebViewActivity.class);
            String url = events.get(getLayoutPosition()).getUrl();
            intent.putExtra("url", url);
            view.getContext().startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public EventsAdapter(List<Event> events, int rowLayout, Context context, String githubUser) {
            this.events = events;
            this.rowLayout = rowLayout;
            this.context = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PostViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(PostViewHolder holder, final int position) {
        holder.eventType.setText(events.get(position).getType().replace("Event", ""));
        holder.eventActor.setText(events.get(position).getActor().getLogin());
        holder.eventTime.setText(events.get(position).getTimeAgo());
        Glide.with(this.context)
                .load(events.get(position).getActor().getAvatarUrl())
                .circleCrop()
                .into(holder.actorAvatar);

        if (events.get(position).getType().equals("PullRequestEvent")) {
            holder.prAction.setText(events.get(position).getPayload().getAction());
        }
        else {
            holder.prAction.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static List<Event> filterEvents(List<Event> events, String githubUser) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {

            if (event.getType().equals("PullRequestEvent") || event.getType().equals("ForkEvent")) {
                String repoName = event.getRepo().getName();
                int indexSlash = repoName.indexOf("/");
                String repoOwner = repoName.substring(0,indexSlash);
                Log.d("teste",githubUser);
                if (repoOwner.equals(githubUser)) {
                    filteredEvents.add(event);
                }
            }
            else if(event.getType().equals("CommitCommentEvent") || event.getType().equals("IssueCommentEvent") || event.getType().equals("PullRequestReviewCommentEvent")) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }
}

