package com.oose2015.grapevine.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CreateEventActivity extends AppCompatActivity {

    private EditText mEventTitleText;
    private EditText mDisplayLocationText;
    private DatePicker mStartDate;
    private DatePicker mEndDate;
    private TimePicker mStartTime;
    private TimePicker mEndTime;
    private CheckBox mCheckbox;
    private static final int CONTACT_REQUEST = 1;
    private double latitude = -1, longitude = -1;

    @Override
    /**
     * Opens to screen that allows user to create an event
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        mCheckbox = (CheckBox) findViewById(R.id.checkbox);
        if (!mCheckbox.isChecked()) {
            mCheckbox.setChecked(true);
        }
        mCheckbox.setVisibility(CheckBox.GONE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    /**
     *   Creates an event based off of input from User
     *   @param view is the View with the information pertaining to the event
     */
    public void onClick(View view) {
        mEventTitleText   = (EditText)findViewById(R.id.event_title);
        mDisplayLocationText = (EditText) findViewById(R.id.display_location);
        mStartDate = (DatePicker) findViewById(R.id.start_date_picker);
        mEndDate = (DatePicker) findViewById(R.id.end_date_picker);
        mStartTime = (TimePicker) findViewById(R.id.time_picker_start);
        mEndTime = (TimePicker) findViewById(R.id.time_picker_end);

        String eventTitleString = mEventTitleText.getText().toString();
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

        int startDay = mStartDate.getDayOfMonth();
        int startMonth = mStartDate.getMonth();
        int startYear = mStartDate.getYear();
        int endDay = mEndDate.getDayOfMonth();
        int endMonth = mEndDate.getMonth();
        int endYear = mEndDate.getYear();

        GregorianCalendar start = new GregorianCalendar(startYear, startMonth, startDay, hourStart, minuteStart);
        GregorianCalendar end = new GregorianCalendar(endYear, endMonth, endDay, hourEnd, minuteEnd);
        Calendar now = GregorianCalendar.getInstance();

        boolean discoverable = mCheckbox.isChecked();
        if(start.before(now) || end.before(now)) {
            Toast.makeText(getApplicationContext(), "Dates must start and end in the future.", Toast.LENGTH_SHORT).show();
        } else if(!start.before(end)) {
            String message = "Start day must be before the end day";
            Toast.makeText(getApplicationContext(), message + "", Toast.LENGTH_SHORT).show();
        } else if(eventTitleString.equals("")) {
            mEventTitleText.setError("Event Title cannot be blank");
        } else if(displayLocationString.equals("")) {
            mDisplayLocationText.setError("Display Location cannot be blank");
        } else if(latitude == -1 || longitude == -1) {
            Toast t = Toast.makeText(getApplicationContext(), "You must choose an exact location for this Event.", Toast.LENGTH_SHORT);
            t.show();
        } else {
            Intent intent = new Intent(this,CreateEventActivity2.class);
            intent.putExtra(Keys.IntentKeys.KEY_EVENT_TITLE,eventTitleString);
            intent.putExtra(Keys.IntentKeys.KEY_DISPLAY_LOCATION,displayLocationString);
            intent.putExtra(Keys.IntentKeys.KEY_HOUR_START, hourStart);
            intent.putExtra(Keys.IntentKeys.KEY_MINUTE_START, minuteStart);
            intent.putExtra(Keys.IntentKeys.KEY_HOUR_END, hourEnd);
            intent.putExtra(Keys.IntentKeys.KEY_MINUTE_END, minuteEnd);
            intent.putExtra(Keys.IntentKeys.KEY_START_DAY, startDay);
            intent.putExtra(Keys.IntentKeys.KEY_START_MONTH, startMonth);
            intent.putExtra(Keys.IntentKeys.KEY_START_YEAR, startYear);
            intent.putExtra(Keys.IntentKeys.KEY_END_DAY, endDay);
            intent.putExtra(Keys.IntentKeys.KEY_END_MONTH, endMonth);
            intent.putExtra(Keys.IntentKeys.KEY_END_YEAR, endYear);
            intent.putExtra(Keys.IntentKeys.KEY_DISCOVERABLE, discoverable);
            intent.putExtra(Keys.IntentKeys.KEY_LATITUDE, latitude);
            intent.putExtra(Keys.IntentKeys.KEY_LONGITUDE, longitude);
            startActivityForResult(intent,2);
        }
    }

    /**
     * Opens Google Maps to get exact location
     * @param view View object
     */
    public void onClickGoogleMaps(View view) {
        //start google maps
        Intent i = new Intent(this, MapsActivity.class);
        startActivityForResult(i, CONTACT_REQUEST);
    }

    /**
     * Depending on the request code, performs specific actions (if 1, from maps, 2 from create
     * activity 2 after adding calendar event
     * @param requestCode requestCode of previous activity
     * @param resultCode resultCode of activity
     * @param data data from previous activity
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
        else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }
}