package com.oose2015.grapevine;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

/**
 * Created by venussoontornprueksa on 12/16/15.
 */
public class TaskLoadMyEvents extends AsyncTask<Void, Void, ArrayList<Event>> {
    private AllEventsLoadedListener myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    /**
     * Sets the current component and retrieves the request queue
     * @param myComponent
     */
    public TaskLoadMyEvents(AllEventsLoadedListener myComponent) {
        this.myComponent = myComponent;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    /**
     * Loads all events in the app background
     * @param params
     * @return list of events
     */
    @Override
    protected ArrayList<Event> doInBackground(Void... params) {
        return RequestUtils.loadMyEvents(requestQueue);
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
