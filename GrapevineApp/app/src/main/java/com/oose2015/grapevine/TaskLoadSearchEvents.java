package com.oose2015.grapevine;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by venussoontornprueksa on 12/12/15.
 */
public class TaskLoadSearchEvents extends AsyncTask<Void, Void, ArrayList<Event>> {

    private AllEventsLoadedListener myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private HashMap<String, String> mQuery;

    /**
     * Sets the current component and retrieves the request queue
     * @param myComponent
     */
    public TaskLoadSearchEvents(AllEventsLoadedListener myComponent, String query) {
        this.myComponent = myComponent;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        mQuery = new HashMap<>();
        String regex = "[0-9]+";
        if (query.matches(regex)) {
            mQuery.put("id", query);
        } else {
            mQuery.put("query", query);
        }
    }

    /**
     * Loads all events in the app background
     * @param params
     * @return list of events
     */
    @Override
    protected ArrayList<Event> doInBackground(Void... params) {
        return RequestUtils.searchAllEvents(requestQueue, mQuery);
    }

    /**
     * Indicates that all events are loaded
     * @param listEvents
     */
    @Override
    protected void onPostExecute(ArrayList<Event> listEvents) {
        if (myComponent != null) {
            myComponent.onAllEventsLoaded(listEvents);
        }
    }
}
