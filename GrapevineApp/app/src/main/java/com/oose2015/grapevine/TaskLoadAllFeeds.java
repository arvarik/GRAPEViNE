package com.oose2015.grapevine;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

/**
 * Created by venussoontornprueksa on 12/15/15.
 */
public class TaskLoadAllFeeds extends AsyncTask<Void, Void, ArrayList<Feed>> {

    private AllFeedsLoadedListener myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    /**
     * Sets the current component and retrieves the request queue
     * @param myComponent
     */
    public TaskLoadAllFeeds(AllFeedsLoadedListener myComponent) {
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
    protected ArrayList<Feed> doInBackground(Void... params) {
        return RequestUtils.loadAllFeeds(requestQueue);
    }

    /**
     * Indicates that all events are loaded
     * @param listFeeds
     */
    @Override
    protected void onPostExecute(ArrayList<Feed> listFeeds) {
        if (myComponent != null) {
            myComponent.onAllFeedsLoaded(listFeeds);
        }
    }
}
