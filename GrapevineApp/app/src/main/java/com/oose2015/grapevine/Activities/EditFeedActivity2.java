package com.oose2015.grapevine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.oose2015.grapevine.Feed;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;

import java.util.List;

public class EditFeedActivity2 extends AppCompatActivity {

    private EditText mTags;
    private Bundle feedInfo;

    /**
     * Method run to create the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_feed2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        mTags = (EditText) findViewById(R.id.tag_area);

        feedInfo = getIntent().getExtras();
        Feed clickedFeed = (Feed) feedInfo.getSerializable(Keys.IntentKeys.KEY_FEED);

        List<String> tagsList = clickedFeed.getTags();
        if(tagsList != null) {
            String tags = "";
            for (String tag : tagsList) {
                tags += "#" + tag + "\n";
            }
            mTags.setText(tags, TextView.BufferType.EDITABLE);
        }
    }

    /**
     *   Creates a feed based off of input from User
     *   @param view is the View with the information pertaining to the feed
     */
    public void onClick(View view) {
        Bundle eventInfo = getIntent().getExtras();
        String feedTitle = eventInfo.getString(Keys.IntentKeys.KEY_FEED_TITLE);
        String displayLocation = eventInfo.getString(Keys.IntentKeys.KEY_DISPLAY_LOCATION);
        double radius = eventInfo.getDouble(Keys.IntentKeys.KEY_RADIUS);
        int hourStart = eventInfo.getInt(Keys.IntentKeys.KEY_HOUR_START);
        int minuteStart = eventInfo.getInt(Keys.IntentKeys.KEY_MINUTE_START);
        int hourEnd = eventInfo.getInt(Keys.IntentKeys.KEY_HOUR_END);
        int minuteEnd = eventInfo.getInt(Keys.IntentKeys.KEY_MINUTE_END);
        double latitude = eventInfo.getDouble(Keys.IntentKeys.KEY_LATITUDE);
        double longitude = eventInfo.getDouble(Keys.IntentKeys.KEY_LONGITUDE);

        String eventTagsString = mTags.getText().toString();

        //Do something with backend

        Intent intent = new Intent(this, FeedsActivity.class);
        startActivity(intent);
    }
}
