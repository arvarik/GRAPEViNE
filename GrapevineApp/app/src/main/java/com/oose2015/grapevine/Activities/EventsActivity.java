package com.oose2015.grapevine.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oose2015.grapevine.AllEventsLoadedListener;
import com.oose2015.grapevine.DividerItemDecoration;
import com.oose2015.grapevine.Event;
import com.oose2015.grapevine.EventListAdapter;
import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.Logger;
import com.oose2015.grapevine.MyLinearLayoutManager;
import com.oose2015.grapevine.R;
import com.oose2015.grapevine.TaskLoadMyEvents;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AllEventsLoadedListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String STATE_EVENTS = "state_events";
    private Toolbar mToolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView mRecyclerViewMyEvents;
    private RecyclerView mRecyclerViewEventsAttending;
    private EventListAdapter mListAdapterMyEvents;
    private EventListAdapter mListAdapterEventsAttending;
    private ArrayList<Event> mListEvents;
    private ArrayList<Event> mMyEvents;
    private ArrayList<Event> mEventsAttending;
    private FloatingActionButton mFAB;
    private SwipeRefreshLayout mSwipeLayout;
    private View.OnClickListener mFabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(EventsActivity.this, CreateEventActivity.class);
            startActivity(intent);
        }
    };

    @Override
    /**
     * Opens the Events page of the application.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        mToolbar =  (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        View headerView = mDrawer.inflateHeaderView(R.layout.drawer_header);
        TextView usernameText = (TextView) headerView.findViewById(R.id.username_text);
        usernameText.setText(Grapevine.getMyUsername());

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("debug", "REFRESHED");
                mSwipeLayout.setRefreshing(false);
            }
        });

        mRecyclerViewMyEvents = (RecyclerView) findViewById(R.id.recycler_view_my_events);
        mRecyclerViewMyEvents.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerViewMyEvents.setHasFixedSize(false);
        mRecyclerViewMyEvents.setLayoutManager(new MyLinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerViewMyEvents.setItemAnimator(new DefaultItemAnimator());

        mRecyclerViewEventsAttending = (RecyclerView) findViewById(R.id.recycler_view_events_attending);
        mRecyclerViewEventsAttending.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerViewEventsAttending.setHasFixedSize(false);
        mRecyclerViewEventsAttending.setLayoutManager(new MyLinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerViewEventsAttending.setItemAnimator(new DefaultItemAnimator());

        mFAB = (FloatingActionButton) findViewById(R.id.create_event_fab);
        mFAB.setOnClickListener(mFabClickListener);
    }

    /**
     * Method run when resuming this activity when coming back from another activity
     */
    @Override
    public void onResume() {
        super.onResume();
        mListEvents = Grapevine.getWritableDatabase().readEvents();
        //if the database is empty, trigger an AsycnTask to download event list from the web
        if (mListEvents.isEmpty()) {
            Logger.debugLog("Fragment: executing task from fragment");
            new TaskLoadMyEvents(this).execute();
        }
        setLists(mListEvents);
    }

    /**
     * Sort what events go into My Events or Events Attending
     * @param list list of events to be sorted
     */
    public void setLists(ArrayList<Event> list) {
        mMyEvents = new ArrayList<>();
        mEventsAttending = new ArrayList<>();

        for(Event e : list) {
            int mId = Grapevine.getMyID();
            if(e.getHostId() == mId) {
                mMyEvents.add(e);
            }
            if(e.isAttending(mId)) {
                mEventsAttending.add(e);
            }
        }

        LinearLayout myEventsLabel = (LinearLayout) mSwipeLayout.findViewById(R.id.my_events_label);
        LinearLayout attendingEventsLabel = (LinearLayout) mSwipeLayout.findViewById(R.id.events_attending_label);

        if(mMyEvents.size() == 0) {
            myEventsLabel.setVisibility(LinearLayout.GONE);
        } else {
            myEventsLabel.setVisibility(LinearLayout.VISIBLE);
        }
        if(mEventsAttending.size() == 0) {
            attendingEventsLabel.setVisibility(LinearLayout.GONE);
        } else {
            attendingEventsLabel.setVisibility(LinearLayout.VISIBLE);
        }

        mListAdapterMyEvents = new EventListAdapter(this, mMyEvents);
        mListAdapterEventsAttending = new EventListAdapter(this, mEventsAttending);

        mRecyclerViewMyEvents.setAdapter(mListAdapterMyEvents);
        mRecyclerViewEventsAttending.setAdapter(mListAdapterEventsAttending);

        LinearLayout.LayoutParams paramsToday = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramsTomorrow = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // calculate height of RecyclerView based on child count
        paramsToday.height=73* mMyEvents.size();
        // set height of RecyclerView
        mRecyclerViewMyEvents.setLayoutParams(paramsToday);
        paramsTomorrow.height=73* mEventsAttending.size();
        mRecyclerViewEventsAttending.setLayoutParams(paramsTomorrow);
    }

    @Override
    /**
     * Called by the system when the device configuration changes while your activity is running.
     */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    /**
     * Selects the next screen to go to based off of their button choice.
     * @return true if it is able to successfully transition, false otherwise
     */
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Intent intent = null;
        if(menuItem.getItemId() == R.id.navigation_item_1) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, MainScreen.class);
            startActivity(intent);
            return true;
        }
        if(menuItem.getItemId() == R.id.navigation_item_2) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        if(menuItem.getItemId() == R.id.navigation_item_3) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, FeedsActivity.class);
            startActivity(intent);
            return true;
        }
        if(menuItem.getItemId() == R.id.navigation_item_4) {
            Toast t = Toast.makeText(getApplicationContext(), "You are already on the Events screen.", Toast.LENGTH_SHORT);
            t.show();
            return true;
        }
        if(menuItem.getItemId() == R.id.navigation_item_5) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    /**
     * Called when the activity has detected the user's press of the back key.
     */
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Sets the events for the adapter once all events are loaded
     * @param listEvents
     */
    @Override
    public void onAllEventsLoaded(ArrayList<Event> listEvents) {
        Logger.debugLog("MainScreen Fragment: onAllEventsLoaded Fragment");
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
        setLists(listEvents);
    }

    /**
     * save the event list to a parcelable prior to rotation or configuration change
     * @param outState out state
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_EVENTS, mListEvents);
    }

    /**
     * When the user swipes down to refresh
     */
    @Override
    public void onRefresh() {
        //load the whole feed again on refresh
        new TaskLoadMyEvents(this).execute();

    }
}
