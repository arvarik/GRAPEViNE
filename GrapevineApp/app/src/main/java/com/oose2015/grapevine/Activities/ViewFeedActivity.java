package com.oose2015.grapevine.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.oose2015.grapevine.Feed;
import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;

import java.util.Calendar;
import java.util.List;

public class ViewFeedActivity extends AppCompatActivity {

    private TextView mTitle;
    private TextView mDisplayLocation;
    private TextView mRadius;
    private TextView mTags;
    private TextView mStartTime;
    private TextView mEndTime;
    private Feed feed;

    @Override
    /**
     * Displays feed activity screen when user goes to feed.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        Bundle info = getIntent().getExtras();
        int feedId = info.getInt(Keys.IntentKeys.KEY_FEED);
        feed = Grapevine.getWritableDatabase().getFeed(feedId);

        mTitle = (TextView) findViewById(R.id.feed_title);
        mDisplayLocation = (TextView) findViewById(R.id.display_location);
        mRadius = (TextView) findViewById(R.id.radius);
        mTags = (TextView) findViewById(R.id.tags);
        mStartTime = (TextView) findViewById(R.id.start_time);
        mEndTime = (TextView) findViewById(R.id.end_time);

        //Change these lines to get from backend?
        mTitle.setText(feed.getName());
        mDisplayLocation.setText(feed.getDisplayLocation());
        mRadius.setText(feed.getRadius()+"");

        Calendar nowCal = Calendar.getInstance();
        Calendar startFeedCal = feed.getStartTime();
        int nowDay = nowCal.get(Calendar.DAY_OF_MONTH);
        int nowYear = nowCal.get(Calendar.YEAR);
        int nowMonth = nowCal.get(Calendar.MONTH)+1;
        int startFeedDay = startFeedCal.get(Calendar.DAY_OF_MONTH);
        int startFeedYear = startFeedCal.get(Calendar.YEAR);
        int startFeedMonth = startFeedCal.get(Calendar.MONTH)+1;
        int startFeedHour = startFeedCal.get(Calendar.HOUR_OF_DAY);
        int startFeedMin = startFeedCal.get(Calendar.MINUTE);
        if (nowDay== startFeedDay && nowYear==startFeedYear && nowMonth==startFeedMonth) {
            String s = String.format(":%02d", startFeedMin);
            mStartTime.setText("Date: Today - " + startFeedHour + s);
        } else {
            String s = String.format(":%02d", startFeedMin);
            mStartTime.setText("Date: " + startFeedHour + "/" + startFeedDay + "/" + startFeedYear + " - " + startFeedHour + s);
        }
        Calendar endFeedCal = feed.getEndTime();
        int endFeedDay = endFeedCal.get(Calendar.DAY_OF_MONTH);
        int endFeedYear = endFeedCal.get(Calendar.YEAR);
        int endFeedMonth = endFeedCal.get(Calendar.MONTH)+1;
        int endFeedHour = endFeedCal.get(Calendar.HOUR_OF_DAY);
        int endFeedMin = endFeedCal.get(Calendar.MINUTE);
        if (nowDay== startFeedDay && nowYear==startFeedYear && nowMonth==startFeedMonth)
            mEndTime.setText("Date: Today - " + endFeedHour + ":" + endFeedMin);
        else
            mEndTime.setText("Date: " + endFeedMonth + "/" + endFeedDay + "/" + endFeedYear + " - " + endFeedHour + ":" + endFeedMin);

        List<String> tags = feed.getTags();
        String allTags = "";
        if (tags != null) {
            for (String tag : tags) {
                allTags += "#" + tag + "\n";
            }
        }
        mTags.setText(allTags);
        mTags.setMovementMethod(new ScrollingMovementMethod());
    }
}