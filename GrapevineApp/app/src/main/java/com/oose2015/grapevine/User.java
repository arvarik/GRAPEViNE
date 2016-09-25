package com.oose2015.grapevine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vamsi on 10/12/2015.
 */
public class User {

    private String username;
    private String name;
    private String password;
    private String email;
    private int createdEvents;
    private int attendedEvents;
    private List<Event> events;
    private List<Feed> feeds;
    private String bio;
    private int id;

    /**
     * Creates a user object.
     * @param username login of the user
     * @param name name of the user
     * @param password password of user's account
     * @param email email address of user
     */
    public User(String username, String name, String password, String email) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.events = new ArrayList<Event>();
        this.feeds = new ArrayList<Feed>();
    }

    /**
     * Creates a user object.
     * @param username login of the user
     * @param email email address of the user
     * @param password password of the user's account
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Empty constructor for a user object.
     */
    public User() {}

    /**
     * Sets a user's id.
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets a user's id.
     * @return id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Sets a user's login id.
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets a user's name.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets a user's email.
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets a user's password.
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Adds an event to the user's list of events.
     * @param e the event to add
     */
    public void addEvent(Event e) {
        events.add(e);
    }

    /**
     * Removes an event from the user's list of events.
     * @param e the event to remove
     */
    public void removeEvent(Event e) {
        events.remove(e);
    }

    /**
     * Adds a feed to the user's list of feeds.
     * @param f the feed to add
     * @return true if added successfully, false otherwise
     */
    public boolean addFeed(Feed f) {
        return feeds.add(f);
    }

    /**
     * Finds an event that the user is attending by event id.
     * @param id the id to search
     * @return the Event found, null if not found
     */
    public Event getEvent(int id) {
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
     * Checks if the user is attending/is a maybe for a particular event.
     * @param e the event to check
     * @return true if user is attending/is maybe, false if not attending
     */
    public boolean contains(Event e) {
        return events.contains(e);
    }

    /**
     * Removes a feed from the user's list of feeds.
     * @param f the feed to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeFeed(Feed f) {
        return feeds.remove(f);
    }

    /**
     * Gets the user's login id.
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the user's password.
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Gets the user's email.
     * @return email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets the user's username.
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the user's bio.
     * @return bio
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the user's bio.
     * @param bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Converts the user information into a string.
     * @return the string
     */
    public String toString() {
        return String.format("%d %s %s %s %s", this.id, this.username, this.name, this.password,
                this.email);
    }
}