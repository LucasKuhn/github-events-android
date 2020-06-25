package ucs.trabalho3.github_events.src.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ucs.trabalho3.github_events.R;
import ucs.trabalho3.github_events.src.model.Event;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.PostViewHolder>  {

    private List<Event> events;
    private int rowLayout;
    private Context context;

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        LinearLayout eventsLayout;
        TextView eventType;
        TextView eventActor;

        public PostViewHolder(View v) {
            super(v);
            eventsLayout = (LinearLayout) v.findViewById(R.id.events_layout);
            eventType = (TextView) v.findViewById(R.id.eventType);
            eventActor = (TextView) v.findViewById(R.id.eventActor);
        }
    }

    public EventsAdapter(List<Event> events, int rowLayout, Context context) {
        this.events = events;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, final int position) {
        holder.eventType.setText(events.get(position).getType());
        holder.eventActor.setText(events.get(position).getActor().getLogin());
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}

