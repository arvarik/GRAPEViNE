package com.oose2015.grapevine.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.R;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;

    @Override
    /**
     * Called when the settings are clicked
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
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

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingsFragment()).commit();
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
            mDrawerLayout.closeDrawer(GravityCompat.START);
            intent = new Intent(this, EventsActivity.class);
            startActivity(intent);
            return true;
        }
        if(menuItem.getItemId() == R.id.navigation_item_5) {
            Toast t = Toast.makeText(getApplicationContext(), "You are already on the Settings screen.", Toast.LENGTH_SHORT);
            t.show();
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

    public static class SettingsFragment extends PreferenceFragment {

        /**
         * Creates the settings fragment
         * @param savedInstanceState
         */
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}