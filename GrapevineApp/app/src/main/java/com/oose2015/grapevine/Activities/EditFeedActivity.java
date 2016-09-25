package com.oose2015.grapevine.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.oose2015.grapevine.Feed;
import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditFeedActivity extends AppCompatActivity {

    private EditText mFeedTitleText;
    private EditText mRadius;
    private TimePicker mStartTime;
    private TimePicker mEndTime;
    private Feed mFeed;
    static final int CONTACT_REQUEST = 1;
    private double latitude = -1, longitude = -1;
    private EditText mDisplayLocationText;


    /**
     * Method run to create the activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        mFeedTitleText   = (EditText)findViewById(R.id.feed_title);
        mRadius = (EditText) findViewById(R.id.radius);
        mStartTime = (TimePicker) findViewById(R.id.time_picker_start);
        mEndTime = (TimePicker) findViewById(R.id.time_picker_end);
        mDisplayLocationText = (EditText) findViewById(R.id.display_location);

        Bundle info = getIntent().getExtras();
        mFeed = (Feed) info.getSerializable(Keys.IntentKeys.KEY_FEED);

        mFeedTitleText.setText(mFeed.getName(), TextView.BufferType.EDITABLE);
        mRadius.setText((""+mFeed.getRadius()), TextView.BufferType.EDITABLE);
        mDisplayLocationText.setText(mFeed.getDisplayLocation(), TextView.BufferType.EDITABLE);

        setupTimePickers(mFeed.getStartTime(), mFeed.getEndTime());

        latitude = mFeed.getLatitude();
        longitude = mFeed.getLongitude();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void setupTimePickers(Calendar startDate, Calendar endDate) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mStartTime.setHour(startDate.get(Calendar.HOUR_OF_DAY));
            mStartTime.setMinute(startDate.get(Calendar.MINUTE));
            mEndTime.setHour(endDate.get(Calendar.HOUR_OF_DAY));
            mEndTime.setMinute(endDate.get(Calendar.MINUTE));
        } else {
            mStartTime.setCurrentHour(startDate.get(Calendar.HOUR_OF_DAY));
            mStartTime.setCurrentMinute(startDate.get(Calendar.MINUTE));
            mEndTime.setCurrentHour(endDate.get(Calendar.HOUR_OF_DAY));
            mEndTime.setCurrentMinute(endDate.get(Calendar.MINUTE));
        }
    }

    /**
     *   Creates a feed based off of input from User
     *   @param view is the View with the information pertaining to the feed
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void onClick(View view) {
        String feedTitleString = mFeedTitleText.getText().toString();
        String radiusString = mRadius.getText().toString();
        String displayLocationString = mDisplayLocationText.getText().toString();

        int hourStart = -1;
        int minuteStart = -1;
        int hourEnd = -1;
        int minuteEnd = -1;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hourStart = mStartTime.getHour();
            minuteStart = mStartTime.getMinute();
            hourEnd = mEndTime.getHour();
            minuteEnd = mEndTime.getMinute();
        } else {
            hourStart = mStartTime.getCurrentHour();
            minuteStart = mStartTime.getCurrentMinute();
            hourEnd = mEndTime.getCurrentHour();
            minuteEnd = mEndTime.getCurrentMinute();
        }

        GregorianCalendar start = new GregorianCalendar(0, 0, 0, hourStart, minuteStart);
        GregorianCalendar end = new GregorianCalendar(0, 0, 0, hourEnd, minuteEnd);

        if (!start.before(end)) {
            String message = "Start time must be before the end time";
            Toast.makeText(Grapevine.getGrapevineContext(), message + "", Toast.LENGTH_SHORT).show();
        }
        else if(feedTitleString.equals("")) {
            mFeedTitleText.setError("Feed Title cannot be blank");
        } else if(radiusString.equals("")) {
            mRadius.setError("Radius cannot be blank");
        } else if(displayLocationString.equals("")) {
                mDisplayLocationText.setError("Display Location cannot be blank");
        } else if(latitude == -1 || longitude == -1) {
            Toast t = Toast.makeText(getApplicationContext(), "You must choose an exact location for this Feed.", Toast.LENGTH_SHORT);
            t.show();
        } else {
            Intent intent = new Intent(this, EditFeedActivity2.class);
            intent.putExtra(Keys.IntentKeys.KEY_FEED_TITLE,feedTitleString);
            intent.putExtra(Keys.IntentKeys.KEY_RADIUS, Double.parseDouble(radiusString));
            intent.putExtra(Keys.IntentKeys.KEY_DISPLAY_LOCATION,displayLocationString);
            intent.putExtra(Keys.IntentKeys.KEY_HOUR_START, hourStart);
            intent.putExtra(Keys.IntentKeys.KEY_MINUTE_START, minuteStart);
            intent.putExtra(Keys.IntentKeys.KEY_HOUR_END, hourEnd);
            intent.putExtra(Keys.IntentKeys.KEY_MINUTE_END, minuteEnd);
            intent.putExtra(Keys.IntentKeys.KEY_LATITUDE, latitude);
            intent.putExtra(Keys.IntentKeys.KEY_LONGITUDE, longitude);
            intent.putExtra(Keys.IntentKeys.KEY_FEED, mFeed);
            startActivity(intent);
        }
    }

    /**
     * Opens up Google Maps to get exact location
     * @param view View object
     */
    public void onClickGoogleMaps(View view) {
        //start google maps
        Intent i = new Intent(this, MapsActivity.class);
        startActivityForResult(i, CONTACT_REQUEST);
    }

    /**
     * Sets latitude and longitube from Google Places request result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                latitude = b.getDouble(Keys.IntentKeys.KEY_LATITUDE);
                longitude = b.getDouble(Keys.IntentKeys.KEY_LONGITUDE);
                Toast.makeText(this, "An Exact Location successfully chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
