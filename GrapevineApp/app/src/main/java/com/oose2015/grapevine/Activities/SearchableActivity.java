package com.oose2015.grapevine.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.oose2015.grapevine.AllEventsLoadedListener;
import com.oose2015.grapevine.DividerItemDecoration;
import com.oose2015.grapevine.Event;
import com.oose2015.grapevine.EventListAdapter;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.Logger;
import com.oose2015.grapevine.R;
import com.oose2015.grapevine.TaskLoadSearchEvents;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity implements AllEventsLoadedListener {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private EventListAdapter mListAdapter;

    /**
     * Sets the necesary view to display the searched for data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searachable);

        mToolbar =  (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mListAdapter = new EventListAdapter(this);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String query = this.getIntent().getExtras().getString(Keys.IntentKeys.KEY_QUERY);
        search(query);
    }

    /**
     * Searches backend based off of query
     * @param query query user wants to search for
     */
    private void search(String query) {
        new TaskLoadSearchEvents(this, query).execute();
    }

    /**
     * When events from search are all loaded.
     * @param listEvents
     */
    @Override
    public void onAllEventsLoaded(ArrayList<Event> listEvents) {
        if (listEvents.isEmpty()) {
            Logger.shortToast("No results found");
        } else {
            mListAdapter.setEvents(listEvents);
        }
    }
}