package com.oose2015.grapevine;

import com.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by venussoontornprueksa on 12/1/15.
 */
public class RequestUtils {
    /**
     * Put all events into local DB
     * @param requestQueue queue to add request to
     * @return list of events
     */
    public static ArrayList<Event> loadAllEvents(RequestQueue requestQueue, int feedId) {
        JSONArray response = Requester.requestEventsJSON(requestQueue, feedId);
        ArrayList<Event> listEvents = Parser.parseEventsJSON(response);
        Grapevine.getWritableDatabase().insertEvents(GrapevineDB.GRAPEVINE_DB, listEvents, false);
        return listEvents;
    }

    public static ArrayList<Event> loadMyEvents(RequestQueue requestQueue) {
        JSONArray response = Requester.requestMyEvents(requestQueue);
        ArrayList<Event> listEvents = Parser.parseEventsJSON(response);
        Grapevine.getWritableDatabase().insertEvents(GrapevineDB.GRAPEVINE_DB, listEvents, true);
        return listEvents;
    }

    /**
     * Load a User into local DB
     * @param requestQueue queue to add request to
     * @return User
     */
    public static User loadUser(RequestQueue requestQueue) {
        JSONObject response = Requester.requestProfile(requestQueue, Grapevine.getMyID());
        User user = Parser.parseUserProfileJSON(response);
        Logger.debugLog("RequestUtils user: " + user.toString());
        Grapevine.getWritableDatabase().insertUsers(GrapevineDB.GRAPEVINE_DB, user, true);
        return user;
    }

    /**
     * Search all Events
     * @param requestQueue queue to add request to
     * @param param search parameters
     * @return list of events
     */
    public static ArrayList<Event> searchAllEvents(RequestQueue requestQueue, HashMap<String, String> param) {
        JSONArray response = Requester.searchEvents(requestQueue, param);
        ArrayList<Event> foundEvents = Parser.parseEventsJSON(response);
        Grapevine.getWritableDatabase().insertEvents(GrapevineDB.GRAPEVINE_DB, foundEvents, true);
        return foundEvents;
    }

    /**
     * Get all of the user's feeds
     * @param requestQueue queue to add request to
     * @return list of feeds
     */
    public static ArrayList<Feed> loadAllFeeds(RequestQueue requestQueue) {
        JSONArray response = Requester.requestFeeds(requestQueue);
        ArrayList<Feed> foundFeeds = Parser.parseFeeds(response);
        Grapevine.getWritableDatabase().insertFeeds(GrapevineDB.GRAPEVINE_DB, foundFeeds, true);
        return foundFeeds;
    }
}
