package com.oose2015.grapevine;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a dummy database created so that we could perform basic tests.
 * Created by venussoontornprueksa on 10/23/15.
 * This may become our service for sending data between front- and back-end.
 */
public class Database {
    private HashMap<String, User> users;
    private ArrayList<Event> events;
    private ArrayList<Feed> feeds;
    private int eventIdCounter;

    /**
     * Construct the database that holds users, events, and feeds.
     */
    public Database() {
        users = new HashMap<String, User>();
        events = new ArrayList<Event>();
        feeds = new ArrayList<Feed>();
        eventIdCounter = 0;
    }

    /**
     * Adds a new user to the database.
     * @param u User to add
     * @return true if the new user's login id is not already taken, false otherwise
     */
    public boolean createUser(User u) {
        if (users.containsKey(u.getUsername()))
            return false;
        users.put(u.getUsername(), u);
        return true;
    }

    /**
     * Deletes a current user from the database.
     * @param u the User to delete
     */
    public void deleteUser(User u) {
        users.remove(u);
    }

    /**
     * Adds a new event to the database and assigns the event an event id.
     * @param e the Event to add
     * @return true if added successfully to the arraylist, false otherwise
     */
    public boolean createEvent(Event e) {
        eventIdCounter++;
        e.setId(eventIdCounter);
        return events.add(e);
    }

    /**
     * Replaces an existing event with an updated one, assigning the correct id to the updated
     * event.
     * @param original the Event to replace
     * @param updated the Event to add
     * @return true if added successfully to the arraylist, false otherwise
     */
    public boolean updateEvent(Event original, Event updated) {
        updated.setId(original.getId());
        deleteEvent(original);
        return events.add(updated);
    }

    /**
     * Deletes an existing event from the database.
     * @param e the Event to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteEvent(Event e) {
        return events.remove(e);
    }

    /**
     * Returns the user if the user is in the database.
     * @param u the user to find
     * @return the user if found, null otherwise
     */
    public User getUser(User u) {
        if (users.containsValue(u))
            return u;
        else
            return null;
    }

    /**
     * Searches all events containing the given string. A more comprehensive search needs to be
     * implemented in the next iteration.
     * @param s the string to search on
     * @return the events containing the string searched
     */
    public ArrayList<Event> searchEvents(String s) {
        ArrayList<Event> results = new ArrayList<Event>();
        Event currEvent;

        for(int i = 0; i < events.size(); i++){
            currEvent = events.get(i);
            if(currEvent.toString().contains(s)) {
                results.add(currEvent);
            }
        }
        return results;
    }

    /**
     * Finds the event with the given id.
     * @param id to search on
     * @return the event if found, null otherwise
     */
    public Event searchEvents(int id) {
        Event currEvent;
        for(int i = 0; i < events.size(); i++){
            currEvent = events.get(i);
            if(currEvent.getId() == id) {
                return currEvent;
            }
        }
        return null;
    }

    /**
     * Adds a feed to the database.
     * @param f the feed to add
     * @return true if successfully added, false otherwise
     */
    public boolean createFeed(Feed f) { return feeds.add(f); }

    /**
     * Removes a feed from the database.
     * @param f the feed to delete
     * @return true if successfully deleted, false otherwise
     */
    public boolean removeFeed(Feed f) {
        return feeds.remove(f);
    }

    /**
     * Checks if the given feed is in the database.
     * @param f the feed to find
     * @return true if found, false otherwise
     */
    public boolean contains(Feed f) { return feeds.contains(f);}

    /**
     * Checks if the given user is in the database.
     * @param u the User to find
     * @return true if found, false otherwise
     */
    public boolean contains(User u) {
        return users.containsKey(u.getUsername());
    }

    /**
     * Checks if the given event is in the database.
     * @param e the Event to find
     * @return true if found, false otherwise
     */
    public boolean contains(Event e) { return events.contains(e); }
}