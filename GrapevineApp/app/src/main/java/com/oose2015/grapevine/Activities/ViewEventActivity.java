package com.oose2015.grapevine.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.oose2015.grapevine.Event;
import com.oose2015.grapevine.Grapevine;
import com.oose2015.grapevine.Keys;
import com.oose2015.grapevine.Logger;
import com.oose2015.grapevine.R;
import com.oose2015.grapevine.Requester;
import com.oose2015.grapevine.VolleySingleton;

import java.util.Calendar;
import java.util.List;

public class ViewEventActivity extends AppCompatActivity {

    private TextView mEventName;
    private TextView mDisplayLocation;
    private TextView mDate;
    private TextView mDescription;
    private TextView mTags;
    private TextView mAttendees;
    private TextView mMaybes;
    private int myID;
    private Button button1;
    private Button button2;
    private Event event;
    private RequestQueue requestQueue;

    @Override
    /**
     * Displays event activity screen when user goes to event.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        requestQueue = VolleySingleton.getInstance().getRequestQueue();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GRAPEViNE");

        Bundle info = getIntent().getExtras();
        int mEventID = (int) info.getSerializable(Keys.IntentKeys.KEY_EVENT);
        event = Grapevine.getWritableDatabase().getEvent(mEventID);
        Logger.debugLog("ViewEventActivity mEventID: " + mEventID);

        mEventName = (TextView) findViewById(R.id.event_name);
        mDisplayLocation = (TextView) findViewById(R.id.display_location);
        mDate = (TextView) findViewById(R.id.date);
        mDescription = (TextView) findViewById(R.id.description);
        mTags = (TextView) findViewById(R.id.tags);
        mTags.setMovementMethod(new ScrollingMovementMethod());
        mAttendees = (TextView) findViewById(R.id.attendees);
        mMaybes = (TextView) findViewById(R.id.maybes);

        mEventName.setText(event.getName());
        mDisplayLocation.setText("Location: " + event.getDisplayLocation());

        Calendar nowCal = Calendar.getInstance();
        Calendar eventCal = event.getStartEvent();
        int nowDay = nowCal.get(Calendar.DAY_OF_MONTH);
        int nowYear = nowCal.get(Calendar.YEAR);
        int nowMonth = nowCal.get(Calendar.MONTH)+1;
        int eventDay = eventCal.get(Calendar.DAY_OF_MONTH);
        int eventYear = eventCal.get(Calendar.YEAR);
        int eventMonth = eventCal.get(Calendar.MONTH)+1;
        int eventHour = eventCal.get(Calendar.HOUR_OF_DAY);
        int eventMin = eventCal.get(Calendar.MINUTE);
        if (nowDay== eventDay && nowYear==eventYear && nowMonth==eventMonth) {
            String s = String.format(":%02d", eventMin);
            mDate.setText("Date: Today - " + eventHour + s);
        } else {
            String s = String.format(":%02d", eventMin);
            mDate.setText("Date: " + eventMonth + "/" + eventDay + "/" + eventYear + " - " + eventHour + s);
        }

        mDescription.setText("Description:\n" + event.getDescription());
        List<String> tagsList = event.getTags();
        String tags = "";
        if (tagsList != null) {
            for(String tag : tagsList) {
                tags += "#" + tag + "\n";
            }
            mTags.setText(tags);
        }

        mAttendees.setText("Going\n" + event.getNumAttending());
        mMaybes.setText("Maybe\n" + event.getNumMaybe());

        changeStatusOfEvent();
    }

    /**
     * Decide if user is attending, maybeing, or nothing an event
     * and set button accordingly
     */
    private void changeStatusOfEvent() {
        myID = Grapevine.getMyID();
        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);

        if(!event.isAttending(myID) && !event.isMaybe(myID)) {
            button1.setText(getResources().getString(R.string.attend));
            button2.setText(getResources().getString(R.string.maybe));
            button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Requester.attendEvent(requestQueue, event.getId());
                    Intent i = new Intent(ViewEventActivity.this, MainScreen.class);
                    i.putExtra(Keys.IntentKeys.KEY_FLAG,1);
                    i.putExtra(Keys.IntentKeys.KEY_EVENT_TITLE, event.getName());
                    startActivity(i);
                }
            });
            button2.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Requester.maybeEvent(requestQueue, event.getId());
                    Intent i = new Intent(ViewEventActivity.this, MainScreen.class);
                    i.putExtra(Keys.IntentKeys.KEY_FLAG,2);
                    i.putExtra(Keys.IntentKeys.KEY_EVENT_TITLE, event.getName());
                    startActivity(i);
                }
            });
        } else if (!event.isAttending(myID) && event.isMaybe(myID)) {
            button1.setText(getResources().getString(R.string.attend));
            button2.setText(getResources().getString(R.string.not_going));
            button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Requester.attendEvent(requestQueue, event.getId());
                    Intent i = new Intent(ViewEventActivity.this, MainScreen.class);
                    i.putExtra(Keys.IntentKeys.KEY_FLAG,1);
                    i.putExtra(Keys.IntentKeys.KEY_EVENT_TITLE, event.getName());
                    startActivity(i);
                }
            });
            button2.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Requester.notMaybeEvent(requestQueue, event.getId());
                    Intent i = new Intent(ViewEventActivity.this, MainScreen.class);
                    i.putExtra(Keys.IntentKeys.KEY_FLAG,0);
                    i.putExtra(Keys.IntentKeys.KEY_EVENT_TITLE, event.getName());
                    startActivity(i);
                }
            });
        } else if (event.isAttending(myID) && !event.isMaybe(myID)) {
            button2.setText(getResources().getString(R.string.maybe));
            button1.setText(getResources().getString(R.string.not_going));
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Requester.maybeEvent(requestQueue, event.getId());
                    Intent i = new Intent(ViewEventActivity.this, MainScreen.class);
                    i.putExtra(Keys.IntentKeys.KEY_FLAG,2);
                    i.putExtra(Keys.IntentKeys.KEY_EVENT_TITLE, event.getName());
                    startActivity(i);
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Requester.notAttendEvent(requestQueue, event.getId());
                    Intent i = new Intent(ViewEventActivity.this, MainScreen.class);
                    i.putExtra(Keys.IntentKeys.KEY_FLAG,0);
                    i.putExtra(Keys.IntentKeys.KEY_EVENT_TITLE, event.getName());
                    startActivity(i);
                }
            });
        }
    }

    /**
     * Inflates the action bar buttons/menu
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar_event, menu);
        return true;
    }

    /**
     * Called whenever an option in menu is selected.
     * @return true if successful, false otherwise
     */
    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.share_event) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", "GRAPEViNE EVENT: " + event.getId());
                clipboard.setPrimaryClip(clip);
            } else {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText("GRAPEViNE EVENT: " + event.getId());
            }
            Toast t = Toast.makeText(getApplicationContext(), "Your clipboard has the search query for this Event.", Toast.LENGTH_SHORT);
            t.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}