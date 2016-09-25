package com.oose2015.grapevine.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;

import java.util.GregorianCalendar;

public class CreateFeedActivity extends AppCompatActivity {

    private EditText mFeedTitleText;
    private EditText mRadius;
    private TimePicker mTimeStart;
    private TimePicker mTimeEnd;
    static final int CONTACT_REQUEST = 1;
    private double latitude = -1, longitude = -1;
    private CheckBox mCheckbox;
    private TextView mTextStartTime;
    private TextView mTextEndTime;
    private EditText mDisplayLocationText;

    @Override
    /**
     * Opens up to screen that allows user to create a feed
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        mCheckbox = (CheckBox) findViewById(R.id.checkbox_allday);
        mTimeStart = (TimePicker) findViewById(R.id.time_picker_start);
        mTimeEnd = (TimePicker) findViewById(R.id.time_picker_end);
        mTextStartTime = (TextView) findViewById(R.id.start_time_label);
        mTextEndTime = (TextView) findViewById(R.id.end_time_label);
    }

    /**
     * Depending if the user wants to the feed to filter by time,
     * or not, set the TimePickers visibilities
     * @param view the checkbox for all day
     */
    public void setTimePicker(View view) {
        boolean allDay = ((CheckBox) view).isChecked();

        if (allDay) {
            mTimeStart.setVisibility(View.INVISIBLE);
            mTimeEnd.setVisibility(View.INVISIBLE);
            mTextStartTime.setVisibility(View.INVISIBLE);
            mTextEndTime.setVisibility(View.INVISIBLE);
        } else {
            mTimeStart.setVisibility(View.VISIBLE);
            mTimeEnd.setVisibility(View.VISIBLE);
            mTextStartTime.setVisibility(View.VISIBLE);
            mTextEndTime.setVisibility(View.VISIBLE);
        }
    }

    /**
     *   Creates a feed based off of input from User
     *   @param view is the View with the information pertaining to the feed
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void onClick(View view) {
        mFeedTitleText   = (EditText)findViewById(R.id.feed_title);
        mRadius = (EditText) findViewById(R.id.radius);
        mDisplayLocationText = (EditText) findViewById(R.id.display_location);

        String feedTitleString = mFeedTitleText.getText().toString();
        String radiusString = mRadius.getText().toString();
        String displayLocationString = mDisplayLocationText.getText().toString();

        boolean allDay = mCheckbox.isChecked();
        Log.d("debug", allDay +"");

        int hourStart = -1;
        int minuteStart = -1;
        int hourEnd = -1;
        int minuteEnd = -1;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hourStart = mTimeStart.getHour();
            minuteStart = mTimeStart.getMinute();
            hourEnd = mTimeEnd.getHour();
            minuteEnd = mTimeEnd.getMinute();
        } else {
            hourStart = mTimeStart.getCurrentHour();
            minuteStart = mTimeStart.getCurrentMinute();
            hourEnd = mTimeEnd.getCurrentHour();
            minuteEnd = mTimeEnd.getCurrentMinute();
        }

        if (allDay) {
            hourStart = 0;
            minuteStart = 0;
            hourEnd = 23;
            minuteEnd = 59;
            Log.d("debug", "checked");
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
            Intent intent = new Intent(this,CreateFeedActivity2.class);
            intent.putExtra(Keys.IntentKeys.KEY_FEED_TITLE,feedTitleString);
            intent.putExtra(Keys.IntentKeys.KEY_DISPLAY_LOCATION, displayLocationString);
            intent.putExtra(Keys.IntentKeys.KEY_RADIUS, Double.parseDouble(radiusString));
            intent.putExtra(Keys.IntentKeys.KEY_HOUR_START, hourStart);
            intent.putExtra(Keys.IntentKeys.KEY_MINUTE_START, minuteStart);
            intent.putExtra(Keys.IntentKeys.KEY_HOUR_END, hourEnd);
            intent.putExtra(Keys.IntentKeys.KEY_MINUTE_END, minuteEnd);
            intent.putExtra(Keys.IntentKeys.KEY_LATITUDE, latitude);
            intent.putExtra(Keys.IntentKeys.KEY_LONGITUDE, longitude);
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
