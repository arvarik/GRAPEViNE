package com.oose2015.grapevine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by venussoontornprueksa on 11/11/15.
 */
public class Parser {

    /**
     * Parse the user logging in
     * @param response response
     * @return code
     */
    public static int parseLoginUserIDJSON(JSONObject response) {
        int id = -1;
        if(response != null && response.length() > 0) {
            try {
                id = response.getInt(Keys.EndpointUser.KEY_ID);
            } catch (JSONException e) {

            }
        }
        return id;
    }

    /**
     * Parses the user name
     * @param response response
     * @return code
     */
    public static String parseLoginUsernameJSON(JSONObject response) {
        String username = "";
        if(response != null && response.length() > 0) {
            try {
                JSONObject user = response.getJSONObject("user");
                username = user.getString(Keys.EndpointUser.KEY_USERNAME);
            } catch (JSONException e) {

            }
        }
        return username;
    }

    /**
     * Converts a JSON object into a User.
     * @param response the response to convert
     * @return the User
     */
    public static User parseBasicUserJSON(JSONObject response) {
        User user = new User();
        if(response != null && response.length() > 0) {
            try {
                JSONObject currentUser = response.getJSONObject("user");
                int id = -1;
                String username = "N/A";
                String name = "N/A";
                String password = "N/A";
                String email = "N/A";
                String bio = "N/A";

                if (Utils.contains(currentUser, Keys.EndpointUser.KEY_ID)) {
                    id = currentUser.getInt(Keys.EndpointUser.KEY_ID);
                }
                if (Utils.contains(currentUser, Keys.EndpointUser.KEY_USERNAME)) {
                    username = currentUser.getString(Keys.EndpointUser.KEY_USERNAME);
                }
                name = response.getString(Keys.EndpointUser.KEY_NAME);
                email = response.getString(Keys.EndpointUser.KEY_EMAIL);
                bio = response.getString(Keys.EndpointUser.KEY_BIO);
                user.setId(id);
                user.setUsername(username);
                user.setName(name);
                user.setPassword(password);
                user.setEmail(email);
                user.setBio(bio);
            } catch (JSONException e) {
            }
            return user;
        }
        return user;
    }

    /**
     * Converts a JSON object response into an Arraylist of Events.
     * @param response the response
     * @return the Arraylist of Events
     */
    public static ArrayList<Event> parseEventsJSON(JSONArray response) {
        ArrayList<Event> listEvents = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    int id = -1;
                    String name = "N/A";
                    String description = "N/A";
                    String exactLocation = "N/A";
                    String displayLocation = "N/A";
                    String dateEventStartString = "N/A";
                    String dateEventEndString = "N/A";
                    String dateCreatedString = "N/A";
                    ArrayList<String> tags = new ArrayList<>();
                    boolean discoverable = false;
                    final User[] host = new User[1];
                    ArrayList<Integer> attendees = new ArrayList<Integer>();
                    ArrayList<Integer> maybes = new ArrayList<Integer>();
                    boolean past;

                    JSONObject currentEvent = response.getJSONObject(i);
                    Event event = new Event();
                    if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_ID)) {
                        id = currentEvent.getInt(Keys.EndpointEvents.KEY_ID);
                        event.setId(id);
                    }
                    if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_NAME)) {
                        name = currentEvent.getString(Keys.EndpointEvents.KEY_NAME);
                        event.setName(name);
                    }
                    if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_DESCRIPTION)) {
                        description = currentEvent.getString(Keys.EndpointEvents.KEY_DESCRIPTION);
                        event.setDescription(description);
                    }
                    if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_EXACT_LOCATION)) {
                        exactLocation = currentEvent.getString(Keys.EndpointEvents
                                .KEY_EXACT_LOCATION);
                        event.setExactLocation(exactLocation);
                    }
                    if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_DISPLAY_LOCATION)) {
                        displayLocation = currentEvent.getString(Keys.EndpointEvents
                                .KEY_DISPLAY_LOCATION);
                        event.setDisplayLocation(displayLocation);
                    }
                    if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_DATE_EVENT_START)) {
                        dateEventStartString = currentEvent.getString(Keys.EndpointEvents
                                .KEY_DATE_EVENT_START);
                        event.setStartEvent(dateEventStartString);
                    }
                    if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_DATE_EVENT_END)) {
                        dateEventEndString = currentEvent.getString(Keys.EndpointEvents
                                .KEY_DATE_EVENT_END);
                        event.setEndEvent(dateEventEndString);
                    }
                    if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_DATE_CREATED)) {
                        dateCreatedString = currentEvent.getString(Keys.EndpointEvents
                                .KEY_DATE_CREATED);
                        event.setDateCreated(dateCreatedString);
                    }
                    if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_TAGS)) {
                        JSONArray tagsArray = currentEvent.getJSONArray(Keys.EndpointEvents
                                .KEY_TAGS);
                        for (int j = 0; j < tagsArray.length(); j++) {
                            tags.add(tagsArray.getString(i));
                        }
                        event.setTags(tags);
                    }
                    /*if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_DISCOVERABLE)) {
                        discoverable = currentEvent.getBoolean(Keys.EndpointEvents
                                .KEY_DISCOVERABLE);
                        event.setDiscoverable(discoverable);
                    }*/
                    if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_HOST)) {
                        int hostID = currentEvent.getInt(Keys.EndpointEvents.KEY_HOST);
                        event.setHostId(hostID);
                    }
                    if(Utils.contains(currentEvent, Keys.EndpointEvents.KEY_ATTENDEES)) {
                        JSONArray attendeesJSON = currentEvent.getJSONArray(Keys.EndpointEvents.KEY_ATTENDEES);
                        for (int j = 0; j < attendeesJSON.length(); j++) {
                            attendees.add(attendeesJSON.getInt(j));
                        }
                        event.addAttendees(attendees);
                    }
                    if(Utils.contains(currentEvent, Keys.EndpointEvents.KEY_MAYBES)) {
                        JSONArray maybesJSON = currentEvent.getJSONArray(Keys.EndpointEvents.KEY_MAYBES);
                        for (int j = 0; j < maybesJSON.length(); j++) {
                            maybes.add(maybesJSON.getInt(j));
                        }
                        event.addMaybes(maybes);
                    }
                    if (Utils.contains(currentEvent, Keys.EndpointEvents.KEY_PAST)) {
                        past = currentEvent.getBoolean(Keys.EndpointEvents.KEY_PAST);
                        event.setPast(past);
                    }

                    listEvents.add(event);
                }
            } catch (JSONException e) {

            }
        } else {
            if (response == null || response.equals(null))
                Logger.debugLog("Response was null");
            else
                Logger.debugLog("Response length is " + response.length());
        }
        return listEvents;
    }

    /**
     * Parse the user profile from back-end
     * @param response response to be parsed
     * @return User object
     */
    public static User parseUserProfileJSON(JSONObject response) {
        User user = new User();
        if(response != null && response.length() > 0) {
            try {
                String username = "N/A";
                String name = "N/A";
                String email = "N/A";
                String bio = "N/A";
                int id = -1;

                JSONObject currentUser = response.getJSONObject("user");
                if (Utils.contains(currentUser, Keys.EndpointUser.KEY_USERNAME)) {
                    username = currentUser.getString(Keys.EndpointUser.KEY_USERNAME);
                }
                if (Utils.contains(currentUser, Keys.EndpointUser.KEY_EMAIL)) {
                    email = currentUser.getString(Keys.EndpointUser.KEY_EMAIL);
                }
                if (Utils.contains(response, Keys.EndpointUser.KEY_BIO)) {
                    bio = response.getString(Keys.EndpointUser.KEY_BIO);
                }
                if (Utils.contains(response, Keys.EndpointUser.KEY_ID)) {
                    id = response.getInt((Keys.EndpointUser.KEY_ID));
                }
                if (Utils.contains(response, Keys.EndpointUser.KEY_NAME)) {
                    name = response.getString(Keys.EndpointUser.KEY_NAME);
                }
                Logger.debugLog(response.keys().toString());
                user.setUsername(username);
                user.setName(name);
                user.setEmail(email);
                user.setBio(bio);
                user.setId(id);
            } catch (JSONException e) {

            }
        }
        return user;
    }

    /**
     * Parses feeds
     * @param response response
     * @return array of feeds
     */
    public static ArrayList<Feed> parseFeeds(JSONArray response) {
        ArrayList<Feed> listFeeds = new ArrayList<>();
        if (response != null && response.length() > 0) {
            try {
                for (int i = 0; i < response.length(); i++) {
                    int id = -1;
                    String name = "N/A";
                    String exactLocation = "(0,0)";
                    String displayLocation = "N/A";
                    double radius;
                    String startTime;
                    String endTime;

                    JSONObject currentFeed = response.getJSONObject(i);
                    Feed feed = new Feed();
                    if (Utils.contains(currentFeed, Keys.EndpointFeeds.KEY_ID)) {
                        id = currentFeed.getInt(Keys.EndpointFeeds.KEY_ID);
                        feed.setId(id);
                    }
                    if (Utils.contains(currentFeed, Keys.EndpointFeeds.KEY_NAME)) {
                        name = currentFeed.getString(Keys.EndpointFeeds.KEY_NAME);
                        feed.setName(name);
                    }
                    if (Utils.contains(currentFeed, Keys.EndpointFeeds.KEY_EXACT_LOCATION)) {
                        exactLocation = currentFeed.getString(Keys.EndpointFeeds.KEY_EXACT_LOCATION);
                        feed.setExactLocation(exactLocation);
                    }
                    if (Utils.contains(currentFeed, Keys.EndpointFeeds.KEY_DISPLAY_LOCATION)) {
                        displayLocation = currentFeed.getString(Keys.EndpointFeeds.KEY_DISPLAY_LOCATION);
                        feed.setDisplayLocation(displayLocation);
                    }
                    if (Utils.contains(currentFeed, Keys.EndpointFeeds.KEY_RADIUS)) {
                        radius = currentFeed.getDouble(Keys.EndpointFeeds.KEY_RADIUS);
                        feed.setRadius(radius);
                    }
                    if (Utils.contains(currentFeed, Keys.EndpointFeeds.KEY_START_TIME)) {
                        startTime = currentFeed.getString(Keys.EndpointFeeds.KEY_START_TIME);
                        feed.setStartTime(startTime);
                    }
                    if (Utils.contains(currentFeed, Keys.EndpointFeeds.KEY_END_TIME)) {
                        endTime = currentFeed.getString(Keys.EndpointFeeds.KEY_END_TIME);
                        feed.setEndTime(endTime);
                    }
                    listFeeds.add(feed);
                }
            }catch(JSONException e){

            }
        }
        return listFeeds;
    }
}