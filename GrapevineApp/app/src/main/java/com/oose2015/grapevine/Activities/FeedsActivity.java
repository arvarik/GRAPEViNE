package com.oose2015.grapevine.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oose2015.grapevine.AllFeedsLoadedListener;
import com.oose2015.grapevine.DividerItemDecoration;
import com.oose2015.grapevine.Feed;
import com.oose2015.grapevine.FeedListAdapter;
import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;
import com.oose2015.grapevine.ServiceFeeds;
import com.oose2015.grapevine.TaskLoadAllFeeds;

import java.util.ArrayList;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class FeedsActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, AllFeedsLoadedListener {

    private SharedPreferences mPrefs;
    //int corresponding to the id of our JobSchedulerService
    private static final int JOB_ID = 102;
    //Run the JobSchedulerService every 2 minutes
    private static final long POLL_FREQUENCY = 28800000;
    private JobScheduler mJobScheduler;
    private Toolbar mToolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView mRecyclerView;
    private FeedListAdapter mListAdapter;
    private FloatingActionButton mFAB;
    private ArrayList<Feed> mListFeeds;
    private View.OnClickListener mFabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(FeedsActivity.this, CreateFeedActivity.class);
            startActivity(intent);
        }
    };


    @Override
    /**
     * Opens the Feeds page of the application.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);

        setupJob();

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

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mListAdapter = new FeedListAdapter(this);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFAB = (FloatingActionButton) findViewById(R.id.create_feed_fab);
        mFAB.setOnClickListener(mFabClickListener);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mPrefs.edit().putBoolean(Keys.SharedPreferencesKeys.KEY_EDIT_MODE, false).apply();
        mPrefs.edit().putBoolean(Keys.SharedPreferencesKeys.KEY_DELETE_MODE, false).apply();

        mListFeeds = Grapevine.getWritableDatabase().readFeeds();
        if (mListFeeds == null) {
            new TaskLoadAllFeeds(this).execute();
        }
    }

    public void setFeedsList(ArrayList<Feed> feeds) {
        mListAdapter.setFeeds(feeds);
    }

    /**
     * Method run when resuming this activity when coming back from another activity
     */
    @Override
    public void onResume() {
        super.onResume();
        new TaskLoadAllFeeds(this).execute();
        setFeedsList(mListFeeds);
    }
    /**
     * Sets up the job for the sending network requests
     */
    private void setupJob() {
        mJobScheduler = JobScheduler.getInstance(this);
        //set an initial delay with a Handler so that the data loading by the JobScheduler does not clash with the loading inside the Fragment
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //schedule the job after the delay has been elapsed
                buildJob();
            }
        }, 30000);
    }

    /**
     * Builds the job for the service that gets all events using the network
     */
    private void buildJob() {
        //attach the job ID and the name of the Service that will work in the background
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, ServiceFeeds.class));
        //set periodic polling that needs net connection and works across device reboots
        builder.setPeriodic(POLL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }

    @Override
    /**
     * Called whenever an option in menu is selected.
     * @return true if successful, false otherwise
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.edit_feeds) {
            Toast t;
            if (!mPrefs.getBoolean(Keys.SharedPreferencesKeys.KEY_DELETE_MODE, false)) {
                if (mPrefs.getBoolean(Keys.SharedPreferencesKeys.KEY_EDIT_MODE, false)) {
                    t = Toast.makeText(getApplicationContext(), "You are No Longer in Edit Mode.", Toast.LENGTH_SHORT);
                    t.show();
                    mPrefs.edit().putBoolean(Keys.SharedPreferencesKeys.KEY_EDIT_MODE, false).apply();
                } else {
                    t = Toast.makeText(getApplicationContext(), "Click a Feed to Edit!", Toast.LENGTH_SHORT);
                    t.show();
                    mPrefs.edit().putBoolean(Keys.SharedPreferencesKeys.KEY_EDIT_MODE, true).apply();
                }
            } else {
                t = Toast.makeText(getApplicationContext(), "You are in Delete Mode.", Toast.LENGTH_SHORT);
                t.show();
            }
            return true;
        } else if(id == R.id.delete_feeds) {
            Toast t;
            if (!mPrefs.getBoolean(Keys.SharedPreferencesKeys.KEY_EDIT_MODE, false)) {
                if (mPrefs.getBoolean(Keys.SharedPreferencesKeys.KEY_DELETE_MODE, false)) {
                    t = Toast.makeText(getApplicationContext(), "You are No Longer in Delete Mode.", Toast.LENGTH_SHORT);
                    t.show();
                    mPrefs.edit().putBoolean(Keys.SharedPreferencesKeys.KEY_DELETE_MODE, false).apply();
                } else {
                    t = Toast.makeText(getApplicationContext(), "Click a Feed to Delete!", Toast.LENGTH_SHORT);
                    t.show();
                    mPrefs.edit().putBoolean(Keys.SharedPreferencesKeys.KEY_DELETE_MODE, true).apply();
                }
            } else {
                t = Toast.makeText(getApplicationContext(), "You are in Edit Mode.", Toast.LENGTH_SHORT);
                t.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    /**
     * Called by the system when the device configuration changes while your activity is running.
     */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Inflate options menu in toolbar
     * @param menu toolbar menu
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar_feeds, menu);
        return true;
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
            Toast t = Toast.makeText(getApplicationContext(), "You are already on the Feeds screen.", Toast.LENGTH_SHORT);
            t.show();
            return true;
        }
        if(menuItem.getItemId() == R.id.navigation_item_4) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, EventsActivity.class);
            startActivity(intent);
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
     * Sets the feeds for the adapter once all feeds are loaded
     * @param listFeeds
     */
    @Override
    public void onAllFeedsLoaded(ArrayList<Feed> listFeeds) {
        setFeedsList(listFeeds);
    }
}