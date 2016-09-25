package com.oose2015.grapevine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class CreateEventActivity2 extends AppCompatActivity {

    private RequestQueue requestQueue;
    private EditText mDescription;
    private EditText mTags;

    @Override
    /**
     * Opens to screen that allows user to create an event
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    /**
     *   Creates an event based off of input from User
     *   @param view is the View with the information pertaining to the event
     */
    public void onClick(View view) {
        mDescription   = (EditText)findViewById(R.id.description_area);
        mTags = (EditText) findViewById(R.id.tag_area);

        Bundle eventInfo = getIntent().getExtras();
        String eventTitle = eventInfo.getString(Keys.IntentKeys.KEY_EVENT_TITLE);
        String displayLocation = eventInfo.getString(Keys.IntentKeys.KEY_DISPLAY_LOCATION);
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
        boolean discoverable = eventInfo.getBoolean(Keys.IntentKeys.KEY_DISCOVERABLE);
        double latitude = eventInfo.getDouble(Keys.IntentKeys.KEY_LATITUDE);
        double longitude = eventInfo.getDouble(Keys.IntentKeys.KEY_LONGITUDE);

        String exactLocation = "(" + latitude + "," + longitude + ")";
        String eventDescriptionString = mDescription.getText().toString();
        String eventTagsString = mTags.getText().toString();

        GregorianCalendar eventStart = new GregorianCalendar(startYear, startMonth, startDay,
                hourStart, minuteStart);
        String eventStartString = ISO8601.fromCalendar(eventStart);

        GregorianCalendar eventEnd = new GregorianCalendar(endYear, endMonth, endDay,
                hourEnd, minuteEnd);
        String eventEndString = ISO8601.fromCalendar(eventEnd);

        GregorianCalendar dateCreated = new GregorianCalendar();
        dateCreated.setTime(new Date());
        String dateCreatedString = ISO8601.fromCalendar(dateCreated);

        if(eventDescriptionString.equals("")) {
            mDescription.setError("Event Description cannot be blank");
        } else {
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put(Keys.EndpointEvents.KEY_NAME, eventTitle);
            params.put(Keys.EndpointEvents.KEY_DESCRIPTION, eventDescriptionString);
            params.put(Keys.EndpointEvents.KEY_EXACT_LOCATION, exactLocation);
            params.put(Keys.EndpointEvents.KEY_DISPLAY_LOCATION, displayLocation);
            params.put(Keys.EndpointEvents.KEY_DATE_CREATED, dateCreatedString);
            params.put(Keys.EndpointEvents.KEY_DATE_EVENT_START, eventStartString);
            params.put(Keys.EndpointEvents.KEY_DATE_EVENT_END, eventEndString);
            params.put(Keys.EndpointEvents.KEY_DISCOVERABLE, discoverable);
            params.put(Keys.EndpointEvents.KEY_PAST, false);
            params.put(Keys.EndpointEvents.KEY_HOST, Grapevine.getMyID() + "");
            Requester.createEvent(requestQueue, params);

            Calendar beginTime = Calendar.getInstance();
            beginTime.set(startYear, startMonth, startDay, hourStart, minuteStart);
            Calendar endTime = Calendar.getInstance();
            endTime.set(endYear, endMonth, endDay, hourEnd, minuteEnd);
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
            intent.putExtra(Keys.IntentKeys.KEY_DESCRIPTION, eventDescriptionString);
            intent.putExtra(Keys.IntentKeys.KEY_TITLE, eventTitle);
            intent.putExtra(Keys.IntentKeys.KEY_EVENT_LOCATION, displayLocation);
            intent.putExtra(Keys.IntentKeys.KEY_ALL_DAY, false);
            startActivityForResult(intent, 1);
        }
    }

    /**
     * Sets the result to OK.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setResult(RESULT_OK);
        finish();
    }
}