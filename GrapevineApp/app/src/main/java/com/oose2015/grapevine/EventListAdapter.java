package com.oose2015.grapevine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oose2015.grapevine.Activities.EditEventActivity;
import com.oose2015.grapevine.Activities.ViewEventActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tom on 10/25/2015.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Event> data = Collections.emptyList();
    private Context context;

    /**
     * Constructs the list adapter for events.
     * @param context context of app
     * @param data the list of events
     */
    public EventListAdapter(Context context, List<Event> data) {
        this.context = context;
        this.inflater=LayoutInflater.from(context);
        this.data=data;
    }

    /**
     * Constructs the list adapter of events without a list of events
     * @param context context of app
     */
    public EventListAdapter(Context context) {
        this.context = context;
        this.inflater=LayoutInflater.from(context);
    }

    /**
     * Set the list of events to be used in the adapter
     * @param listEvents list of events for adapter
     */
    public void setEvents(ArrayList<Event> listEvents) {
        this.data = listEvents;
        notifyDataSetChanged();
    }

    @Override
    /**
     * Gets the view holder for the view with events.
     * @return holder the view holder
     */
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    /**
     * Binds the view holder with the correct name, display location, number attending, number of
     * maybes.
     */
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event current = data.get(position);
        holder.eventID = current.getId();
        holder.eventName.setText(current.getName());
        holder.displayLocation.setText(current.getDisplayLocation());
        holder.attending.setText("" + current.getNumAttending());
        holder.maybe.setText(""+current.getNumMaybe());
        holder.hostID = current.getHostId();
    }

    @Override
    /**
     * Gets the number of events in the list.
     * @return size of the list of events
     */
    public int getItemCount() {
        return (data == null ? -1 : data.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView eventName;
        private int eventID;
        private TextView displayLocation;
        private TextView attending;
        private TextView maybe;
        private int hostID;

        /**
         * Constructs the view holder for the events.
         * @param itemView the view
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            eventName = (TextView) itemView.findViewById(R.id.event_name);
            displayLocation = (TextView) itemView.findViewById(R.id.display_location);
            attending = (TextView) itemView.findViewById(R.id.num_attending);
            maybe = (TextView) itemView.findViewById(R.id.num_maybe);
        }

        @Override
        /**
         * Opens the event activity page.
         */
        public void onClick(View v) {
            Intent intent = null;

            if((""+context).contains("com.oose2015.grapevine.Activities.MainScreen") || (""+context).contains("com.oose2015.grapevine.Activities.SearchableActivity")) {
                intent = new Intent(context, ViewEventActivity.class);
            } else if((""+context).contains("com.oose2015.grapevine.Activities.EventsActivity")) {
                int mId = Grapevine.getMyID();

                if(hostID == mId) {
                    intent = new Intent(context, EditEventActivity.class);
                } else {
                    intent = new Intent(context, ViewEventActivity.class);
                }
            }
            intent.putExtra(Keys.IntentKeys.KEY_EVENT, eventID);
            context.startActivity(intent);
        }
    }
}
