package com.oose2015.grapevine.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;
import com.oose2015.grapevine.Requester;
import com.oose2015.grapevine.ServiceProfile;
import com.oose2015.grapevine.TaskLoadUserProfile;
import com.oose2015.grapevine.User;
import com.oose2015.grapevine.UserProfileLoadedListener;
import com.oose2015.grapevine.VolleySingleton;

import java.util.HashMap;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, UserProfileLoadedListener {

    //int corresponding to the id of our JobSchedulerService
    private static final int JOB_ID = 101;
    //Run the JobSchedulerService every 2 minutes
    private static final long POLL_FREQUENCY = 28800000;
    private JobScheduler mJobScheduler;

    private Toolbar mToolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mUsername;
    private TextView mEmail;
    private TextView mCreated;
    private TextView mAttended;
    private EditText mBio;
    private User mUser;
    private RequestQueue requestQueue;


    @Override
    /**
     * Displays main screen for profile when application is open to Profile.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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

        mUsername = (TextView) findViewById(R.id.username);
        mEmail = (TextView) findViewById(R.id.email);
        mBio = (EditText) findViewById(R.id.bio_area);

        //mUser = Grapevine.getWritableDatabase().getUser(Grapevine.getMyID());
        //if (mUser == null) {
            new TaskLoadUserProfile(this).execute();
        //}
        setupUserInformation(mUser);

        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    /**
     * Sets up the job for the sending network requests
     */
    private void setupJob() {
        mJobScheduler = JobScheduler.getInstance(this);
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
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, ServiceProfile.class));
        //set periodic polling that needs net connection and works across device reboots
        builder.setPeriodic(POLL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }

    /**
     * Fills in User info
     * @param user the user
     */
    private void setupUserInformation(User user) {
        if(user != null) {
            mUsername.setText("Username: " + user.getUsername());
            mEmail.setText("Email: " + user.getEmail());
            mBio.setText(user.getBio(), TextView.BufferType.EDITABLE);
        }
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
            Toast t = Toast.makeText(getApplicationContext(), "You are already on the Profile screen.", Toast.LENGTH_SHORT);
            t.show();
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

    /**
     * When the profile is loaded from back-end
     * @param user the user object
     */
    @Override
    public void onUserProfileLoaded(User user) {
        setupUserInformation(user);
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
     * Confirms the changes to the bio and goes to the main screen
     * @param view
     */
    public void onClick(View view) {
        mBio = (EditText) findViewById(R.id.bio_area);
        String bio = mBio.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(Keys.EndpointUser.KEY_BIO, bio);
        Requester.changeBio(requestQueue, ProfileActivity.this,
                MainScreen.class, params);
    }
}
