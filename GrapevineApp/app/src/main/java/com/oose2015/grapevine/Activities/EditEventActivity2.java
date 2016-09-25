package com.oose2015.grapevine.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.oose2015.grapevine.Event;
import com.oose2015.grapevine.ISO8601;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.R;
import com.oose2015.grapevine.Requester;
import com.oose2015.grapevine.VolleySingleton;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class EditEventActivity2 extends AppCompatActivity {

    private EditText mDescription;
    private EditText mTags;
    private Bundle eventInfo;
    private RequestQueue requestQueue;
    private Event mClickedEvent;

    @Override
    /**
     * Starts the screen for the second edit activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        eventInfo = getIntent().getExtras();
        mClickedEvent = (Event) eventInfo.getSerializable(Keys.IntentKeys.KEY_CLICKED_EVENT);

        mDescription = (EditText) findViewById(R.id.description_area);
        mDescription.setText(mClickedEvent.getDescription(), TextView.BufferType.EDITABLE);
        
        List<String> tagsList = mClickedEvent.getTags();
        if(tagsList != null) {
            String tags = "";
            for(String tag : tagsList) {
                tags += "#" + tag + "\n";
            }
            mTags = (EditText) findViewById(R.id.tag_area);
            mTags.setText(tags, TextView.BufferType.EDITABLE);
        }
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
    }

    /**
     *   Creates an event based off of input from User
     *   @param view is the View with the information pertaining to the event
     */
    public void onClick(View view) {
        mDescription   = (EditText)findViewById(R.id.description_area);
        mTags = (EditText) findViewById(R.id.tag_area);

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
        //boolean discoverable = eventInfo.getBoolean(Keys.IntentKeys.KEY_DISCOVERABLE);
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

        if(eventDescriptionString.equals("")) {
            mDescription.setError("Event Description cannot be blank");
        } else {
            HashMap<String, Object> params = new HashMap<>();
            params.put(Keys.EndpointEvents.KEY_NAME, eventTitle);
            params.put(Keys.EndpointEvents.KEY_DESCRIPTION, eventDescriptionString);
            params.put(Keys.EndpointEvents.KEY_EXACT_LOCATION, exactLocation);
            params.put(Keys.EndpointEvents.KEY_DISPLAY_LOCATION, displayLocation);
            params.put(Keys.EndpointEvents.KEY_DATE_EVENT_START, eventStartString);
            params.put(Keys.EndpointEvents.KEY_DATE_EVENT_END, eventEndString);
            Requester.editEvent(requestQueue, params, mClickedEvent.getId());

            Intent i = new Intent(this, EventsActivity.class);
            startActivity(i);
        }
    }
}