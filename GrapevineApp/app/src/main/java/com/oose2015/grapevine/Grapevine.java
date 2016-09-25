package com.oose2015.grapevine;

import android.app.Application;
import android.content.Context;

/**
 * Created by venussoontornprueksa on 11/7/15.
 */
public class Grapevine extends Application {
    private static Grapevine sInstance;
    public static final String TAG = Grapevine.class.getName();
    private static GrapevineDB grapevineDb;
    private static int myID;
    private static String myUsername;

    /**
     * Sets the instance and request queue on creation of the application.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        grapevineDb = new GrapevineDB(this);
    }

    /**
     * Gets the Grapevine instance.
     * @return sInstance
     */
    public static Grapevine getInstance() {
        return sInstance;
    }

    /**
     * Gets the Grapevine context.
     * @return the context of the Grapevine instance
     */
    public static Context getGrapevineContext() {
        return sInstance.getApplicationContext();
    }

    /**
     * Creates a writable database if there isn't one
     * @return the database
     */
    public synchronized static GrapevineDB getWritableDatabase() {
        if (grapevineDb == null) {
            grapevineDb = new GrapevineDB(getGrapevineContext());
        }
        return grapevineDb;
    }

    /**
     * Sets the User id that is currently logged in
     * @param id
     */
    public static void setMyID(int id) {
        Logger.debugLog("setMyID: " + id);
        Grapevine.myID = id;
    }

    /**
     * Get the id of the User that is logged in
     * @return
     */
    public static int getMyID() {
        return myID;
    }

    /**
     * Gets username of the user
     * @return username
     */
    public static String getMyUsername() {
        return myUsername;
    }

    /**
     * Sets user username
     * @param myUsername username to set as new username
     */
    public static void setMyUsername(String myUsername) {
        Grapevine.myUsername = myUsername;
    }
}
