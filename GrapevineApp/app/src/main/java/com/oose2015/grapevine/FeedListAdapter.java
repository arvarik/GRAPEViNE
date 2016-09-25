package com.oose2015.grapevine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oose2015.grapevine.Activities.EditFeedActivity;
import com.oose2015.grapevine.Activities.ViewFeedActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Tom on 10/25/2015.
 */
public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<Feed> data = Collections.emptyList();
    private Context context;

    /**
     * Constructs the list adapter for feeds.
     * @param context
     * @param data the list of feeds
     */
    public FeedListAdapter(Context context, List<Feed> data) {
        this.context = context;
        this.inflater=LayoutInflater.from(context);
        this.data=data;
    }

    /**
     * Constructs the list adapter for feeds.
     * @param context
     */
    public FeedListAdapter(Context context) {
        this.context = context;
        this.inflater=LayoutInflater.from(context);
    }

    /**
     * Set the list of events to be used in the adapter
     * @param listFeeds list of events for adapter
     */
    public void setFeeds(ArrayList<Feed> listFeeds) {
        this.data = listFeeds;
        notifyDataSetChanged();
    }


    @Override
    /**
     * Gets the view holder for the view with feeds.
     * @return holder the view holder
     */
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.feed_list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    /**
     * Binds the view holder with the correct title.
     */
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Feed current = data.get(position);
        holder.feedId = current.getId();
        holder.feedName.setText(current.getName());
    }

    @Override
    /**
     * Gets the number of feeds in the list.
     * @return size of the list of feeds
     */
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView feedName;
        private int feedId;

        /**
         * Constructs the view holder for the feeds.
         * @param itemView the view
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            feedName = (TextView) itemView.findViewById(R.id.feed_name);
        }

        @Override
        /**
         * Opens the feed activity page.
         */
        public void onClick(View v) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if(prefs.getBoolean(Keys.SharedPreferencesKeys.KEY_EDIT_MODE, false)) {
                Intent intent = new Intent(context, EditFeedActivity.class);
                String queryName = feedName.getText().toString();
                //Use queryNAme to query backend for the feed object, and then pass it to the next activity as an extra on the intent
                //intent.putExtra("feed", feed);

                //this line for testing
                List<String> tags = new ArrayList<String>();
                tags.add("OOSE2015");
                tags.add("BestSchoolEver");
                tags.add("ScottSmith");
                tags.add("GroupWork");
                tags.add("Applications");
                tags.add("SeniorYear");
                intent.putExtra(Keys.IntentKeys.KEY_FEED, new Feed("Johns Hopkins University", "exact", "Homewood", 5.5, tags, new GregorianCalendar(), new GregorianCalendar()));

                context.startActivity(intent);
            } else if(prefs.getBoolean(Keys.SharedPreferencesKeys.KEY_DELETE_MODE, false)) {
                //TODO Delete a Feed
            }else {
                Intent intent = new Intent(context, ViewFeedActivity.class);
                String queryName = feedName.getText().toString();
                //Use queryNAme to query backend for the feed object, and then pass it to the next activity as an extra on the intent
                //intent.putExtra("feed", feed);

                //this line for testing
                intent.putExtra(Keys.IntentKeys.KEY_FEED, feedId);

                context.startActivity(intent);
            }
        }
    }
}