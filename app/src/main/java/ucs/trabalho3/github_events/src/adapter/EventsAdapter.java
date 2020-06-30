package ucs.trabalho3.github_events.src.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import ucs.trabalho3.github_events.R;
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

        public PostViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            eventsLayout = (LinearLayout) v.findViewById(R.id.events_layout);
            eventType = (TextView) v.findViewById(R.id.eventType);
            eventActor = (TextView) v.findViewById(R.id.eventActor);
            eventTime = (TextView) v.findViewById(R.id.eventTime);
        }

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), WebViewActivity.class);
            String url = events.get(getLayoutPosition()).getUrl();
            intent.putExtra("url", url);
            view.getContext().startActivity(intent);
        }
    }

    public EventsAdapter(List<Event> events, int rowLayout, Context context) {
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
        holder.eventType.setText(events.get(position).getType());
        holder.eventActor.setText(events.get(position).getActor().getLogin());
        holder.eventTime.setText(events.get(position).getTimeAgo());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}

