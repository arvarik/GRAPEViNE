package com.oose2015.grapevine.Activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.MyPagerAdapter;
import com.oose2015.grapevine.R;
import com.oose2015.grapevine.ServiceAllEvents;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

/**
 * Created by Vamsi on 10/12/2015.
 */
public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //int corresponding to the id of our JobSchedulerService
    private static final int JOB_ID = 100;
    //Run the JobSchedulerService every 2 minutes
    private static final long POLL_FREQUENCY = 28800000;
    private JobScheduler mJobScheduler;
    private Toolbar mToolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TabLayout mTabLayout;
    private ViewPager mPager;
    private MyPagerAdapter mAdapter;
    private FloatingActionButton mFAB;
    private View.OnClickListener mFabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainScreen.this, CreateEventActivity.class);
            startActivity(intent);
        }
    };

    @Override
    /**
     * Displays screen of default feed after log in.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        setupJob();

        mToolbar =  (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("GRAPEViNE");
        setUpPager();

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

        mFAB = (FloatingActionButton) findViewById(R.id.create_event_fab);
        mFAB.setOnClickListener(mFabClickListener);

        Bundle eventStatus = getIntent().getExtras();
        if (eventStatus != null) {
            int flag = eventStatus.getInt(Keys.IntentKeys.KEY_FLAG);
            String event = eventStatus.getString(Keys.IntentKeys.KEY_EVENT_TITLE);
            String message = "";

            if (flag == 1) {
                message = "You are attending event, " + event + "!";
                Toast.makeText(Grapevine.getGrapevineContext(), message + "", Toast.LENGTH_SHORT).show();
            } else if (flag == 2) {
                message = "You might attend event, " + event + "!";
                Toast.makeText(Grapevine.getGrapevineContext(), message + "", Toast.LENGTH_SHORT).show();
            } else {
                message = "You are not attending event, " + event + "!";
                Toast.makeText(Grapevine.getGrapevineContext(), message + "", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setUpPager() {
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    /**
     * Inflates the action bar buttons/menu
     * @param menu
     * @return boolean
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(MainScreen.this, SearchableActivity.class);
                String q;
                if (query.contains("GRAPEViNE EVENT:")) {
                    q = query.substring(query.indexOf(":") + 1).trim();
                    i.putExtra(Keys.IntentKeys.KEY_QUERY, q);
                } else {
                    i.putExtra(Keys.IntentKeys.KEY_QUERY, query);
                }
                startActivity(i);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;

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
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, ServiceAllEvents.class));
        //set periodic polling that needs net connection and works across device reboots
        builder.setPeriodic(POLL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
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
            Toast t = Toast.makeText(getApplicationContext(), "You are already on the Home screen.", Toast.LENGTH_SHORT);
            t.show();
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

}

