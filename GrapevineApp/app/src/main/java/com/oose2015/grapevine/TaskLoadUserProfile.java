package com.oose2015.grapevine;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

/**
 * Created by venussoontornprueksa on 12/10/15.
 */
public class TaskLoadUserProfile extends AsyncTask<Void, Void, User> {

    private UserProfileLoadedListener myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    /**
     * Sets the current component and retrieves the request queue
     * @param myComponent
     */
    public TaskLoadUserProfile(UserProfileLoadedListener myComponent) {
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
    protected User doInBackground(Void... params) {
        return RequestUtils.loadUser(requestQueue);
    }

    /**
     * Indicates that all events are loaded
     * @param user
     */
    @Override
    protected void onPostExecute(User user) {
        if (myComponent != null) {
            myComponent.onUserProfileLoaded(user);
        }
    }
}