package com.oose2015.grapevine;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by venussoontornprueksa on 11/7/15.
 */
public class VolleySingleton {

    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;

    /**
     * Constructs the VolleySingleton by setting the RequestQueue
     */
    private VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(Grapevine.getGrapevineContext());
    }

    /**
     * Gets the instance of the VolleySingleton.
     * @return the instance
     */
    public static VolleySingleton getInstance() {
        if(sInstance == null) {
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }

    /**
     * Gets the request queue.
     * @return the request queue
     */
    public RequestQueue getRequestQueue() {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(Grapevine.getGrapevineContext());
        }
        return mRequestQueue;
    }
}