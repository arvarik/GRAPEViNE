package com.oose2015.grapevine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.ISO8601;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;
import com.oose2015.grapevine.Requester;
import com.oose2015.grapevine.VolleySingleton;

import java.util.GregorianCalendar;
import java.util.HashMap;

public class CreateFeedActivity2 extends AppCompatActivity {

    private EditText mTags;
    private RequestQueue requestQueue;

    @Override
    /**
     * Opens up to screen that allows user to create a feed
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feed2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GRAPEViNE");
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    /**
     *   Creates a feed based off of input from User
     *   @param view is the View with the information pertaining to the feed
     */
    public void onClick(View view) {
        mTags = (EditText) findViewById(R.id.tag_area);

        Bundle eventInfo = getIntent().getExtras();
        String feedName = eventInfo.getString(Keys.IntentKeys.KEY_FEED_TITLE);
        double radius = eventInfo.getDouble(Keys.IntentKeys.KEY_RADIUS);
        int hourStart = eventInfo.getInt(Keys.IntentKeys.KEY_HOUR_START);
        int minuteStart = eventInfo.getInt(Keys.IntentKeys.KEY_MINUTE_START);
        int hourEnd = eventInfo.getInt(Keys.IntentKeys.KEY_HOUR_END);
        int minuteEnd = eventInfo.getInt(Keys.IntentKeys.KEY_MINUTE_END);
        int startDay = eventInfo.getInt(Keys.IntentKeys.KEY_START_DAY);
        int startMonth = eventInfo.getInt(Keys.IntentKeys.KEY_START_MONTH);
        int startYear = eventInfo.getInt(Keys.IntentKeys.KEY_START_YEAR);
        int endDay = eventInfo.getInt(Keys.IntentKeys.KEY_END_DAY);
        int endMonth = eventInfo.getInt(Keys.IntentKeys.KEY_END_MONTH);
        int endYear = eventInfo.getInt(Keys.IntentKeys.KEY_END_YEAR);
        double latitude = eventInfo.getDouble(Keys.IntentKeys.KEY_LATITUDE);
        double longitude = eventInfo.getDouble(Keys.IntentKeys.KEY_LONGITUDE);
        String displayLocation = eventInfo.getString(Keys.IntentKeys.KEY_DISPLAY_LOCATION);
        String eventTagsString = mTags.getText().toString();

        GregorianCalendar startTime = new GregorianCalendar(startYear, startMonth, startDay, hourStart, minuteStart);
        GregorianCalendar endTime = new GregorianCalendar(endYear, endMonth, endDay, hourEnd, minuteEnd);

        HashMap<String, Object> params = new HashMap<>();
        params.put(Keys.EndpointFeeds.KEY_OWNER, Grapevine.getMyID());
        params.put(Keys.EndpointFeeds.KEY_NAME, feedName);
        params.put(Keys.EndpointFeeds.KEY_EXACT_LOCATION, "(" + latitude + "," + longitude  + ")");
        params.put(Keys.EndpointFeeds.KEY_DISPLAY_LOCATION, displayLocation);
        params.put(Keys.EndpointFeeds.KEY_START_TIME, ISO8601.calendarToTimeString(startTime));
        params.put(Keys.EndpointFeeds.KEY_END_TIME, ISO8601.calendarToTimeString(endTime));
        params.put(Keys.EndpointFeeds.KEY_RADIUS, radius);

        Requester.createFeed(requestQueue, params);

        Intent intent = new Intent(this, FeedsActivity.class);
        startActivity(intent);
    }
}
