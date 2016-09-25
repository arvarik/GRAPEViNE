package com.oose2015.grapevine;

import android.text.TextUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Created by Vamsi on 10/12/2015.
 */
public class Event implements Serializable {

    private String name;
    private String description;
    private String exactLocation;
    private String displayLocation;
    private double latitude;
    private double longitude;
    private GregorianCalendar startEvent;
    private GregorianCalendar endEvent;
    private Date dateCreated;
    private List<String> tags;
    //private boolean discoverable;
    private int hostId;
    private List<Integer> attendees;
    private List<Integer> maybes;
    private int id;
    private boolean past;

    /**
     * Constructor used for testing recyclerViews
     * @param name name of event
     * @param attendees list of Users attending event
     * @param maybes list of Users maybe attending the event
     * @param displayLocation displayed location of the event
     */
    public Event(String name, List<Integer> attendees, List<Integer> maybes, String displayLocation) {
        this.name = name;
        this.attendees = attendees;
        this.maybes = maybes;
        this.displayLocation = displayLocation;
    }

    /**
     * Creates an Event object based off of parameters sent in
     * @param name Name of event
     * @param description Description of the event
     * @param exactLocation Exact Location of the event
     * @param displayLocation Location entered by user
     * @param startEvent Date of the Event
     * @param dateCreated Date the Event was created
     * @param tags Tags associated with the event
     * @param hostId Host of the event
     */
    public Event(String name, String description, String exactLocation, String displayLocation,
                 GregorianCalendar startEvent, GregorianCalendar endEvent, Date dateCreated, List<String> tags,
                 /*boolean discoverable,*/ int hostId) {
        this.name = name;
        this.description = description;
        this.exactLocation = exactLocation;
        this.displayLocation = displayLocation;
        this.tags = tags;
        //this.discoverable = discoverable;
        this.hostId = hostId;
        this.id = -1;
        this.attendees = new ArrayList<>();
        this.maybes = new ArrayList<>();
        this.startEvent = startEvent;
        this.endEvent = endEvent;
        this.dateCreated = dateCreated;
    }

    /**
     * Creates an Event object with non-null list of attendees and maybes.
     */
    public Event() {
        this.attendees = new ArrayList<>();
        this.maybes = new ArrayList<>();
    }

    /**
     * Gets number of user attending event
     * @return the number of people attending
     */
    public int getNumAttending() {
        return this.attendees.size(
        );
    }

    /**
     * Gets number of maybes to the event
     * @return the number of maybes
     */
    public int getNumMaybe() {
        return this.maybes.size();
    }

    /**
     * Sets the ID of the event
     * @param id the ID of the event
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the ID of the event
     * @return the ID of the event
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the name of the event
     * @return the event name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the event.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the event
     * @return the event description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description using a given string
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the exact location of the event
     * @return the exact location
     */
    public String getExactLocation() {
        return this.exactLocation;
    }

    /**
     * Sets the exact location using a given string
     * @param exactLocation
     */
    public void setExactLocation(String exactLocation) {
        this.exactLocation = exactLocation;
    }

    /**
     * Gets the display location
     * @return the display location
     */
    public String getDisplayLocation() {
        return this.displayLocation;
    }

    /**
     * Sets the display location using a given string
     * @param displayLocation
     */
    public void setDisplayLocation(String displayLocation) {
        this.displayLocation = displayLocation;
    }

    /**
     * Gets the date of the Event
     * @return date of event
     */
    public GregorianCalendar getStartEvent() {
        return this.startEvent;
    }

    /**
     * Gets the ISO string representation of DateEvent
     * @return string of startEvent
     */
    public String getStartEventString() {
        return ISO8601.fromCalendar(startEvent);
    }

    /**
     * Sets the date of the event using a given String
     * @param startEventString
     */
    public void setStartEvent(String startEventString) {
        // must parse String to convert into a Gregorian Calendar object
        try {
            startEvent = ISO8601.toCalendar(startEventString);
        } catch (ParseException e) {
            //a parse exception generated here will store null in the release date
            Logger.debugLog("Error in setStartEvent: " + e.getMessage());
        }
    }

    /**
     * Gets the end of the event
     * @return endEvent
     */
    public GregorianCalendar getEndEvent() {
        return endEvent;
    }

    /**
     * Sets the end time of the event given a time
     * @param endEventString
     */
    public void setEndEvent(String endEventString) {
        // must parse String to convert into a Gregorian Calendar object
        try {
            endEvent = ISO8601.toCalendar(endEventString);
        } catch (ParseException e) {
            //a parse exception generated here will store null in the release date
            Logger.debugLog("Error in setEndEvent: " + e.getMessage());
        }
    }

    /**
     * Gets the ISO string representation of DateEvent
     * @return string of startEvent
     */
    public String getEndEventString() {
        return ISO8601.fromCalendar(endEvent);
    }

    /**
     * Gets the date of when the event was created
     * @return the date event was created
     */
    public Date getDateCreated() {
        return this.dateCreated;
    }

    /**
     * Gets the ISO string representation of DateEvent
     * @return string of startEvent
     */
    public String getDateCreatedString() {
        GregorianCalendar myCal = new GregorianCalendar();
        myCal.setTime(dateCreated);
        return ISO8601.fromCalendar(myCal);
    }

    /**
     * Sets the date the event was creating using a given String
     * @param dateCreatedString
     */
    public void setDateCreated(String dateCreatedString) {
        // must parse String to convert into a Gregorian Calendar object
        try {
            dateCreated = ISO8601.toCalendar(dateCreatedString).getTime();
        } catch (ParseException e) {
            //a parse exception generated here will store null in the release date
            Logger.debugLog("Error in setDateCreated: " + e.getMessage());
        }
    }

    /**
     * Get the list of attendees for the event
     * @return list of attendees
     */
    public List<Integer> getAttendees() {
        return attendees;
    }

    /**
     * Set the list of attendees for the event
     * @param attendees list of attendees
     */
    public void setAttendees(List<Integer> attendees) {
        this.attendees = attendees;
    }

    /**
     * Get the list of maybes for the event
     * @return list of maybes
     */
    public List<Integer> getMaybes() {
        return maybes;
    }

    /**
     * Set the list of attendees for the event
     * @param maybes lits of maybes
     */
    public void setMaybes(List<Integer> maybes) {
        this.maybes = maybes;
    }

    /**
     * Set the list of tags for the event
     * @param tags list of tags
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Gets a list of the tags associated with the event
     * @return a list of the tags
     */
    public List<String> getTags() {
        return this.tags;
    }

    /*public boolean getDiscoverable() {
        return this.discoverable;
    }*/

    /*public void setDiscoverable(boolean discoverable) {
        this.discoverable = discoverable;
    }*/

    /**
     * Gets the Host of the event
     * @return the id of User who is hosting the event
     */
    public int getHostId() {
        return this.hostId;
    }

    /**
     * Set the host ID
     * @param hostId the host integer
     */
    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    /**
     * Sets the past field using a given boolean
     * @param past
     */
    public void setPast(boolean past) {
        this.past = past;
    }

    /**
     * Sets the past field using a given String by parsing it into a boolean
     * @param past
     */
    public void setPast(String past) {
        this.past = (past.equals("true") ? true : false);
    }

    /**
     * Gets whether the event has already occurred
     * @return past
     */
    public boolean getPast() {
        return this.past;
    }

    /**
     * Edits the event information
     * @param name Name of event
     * @param description Description of the event
     * @param exactLocation Exact Location of the event
     * @param displayLocation Location entered by user
     * @param dateEvent Date of the Event
     * @param dateCreated Date the Event was created
     * @param tags Tags associated with the event
     * @param host Host of the event
     */
    public void editEvent(String name, String description, String exactLocation,
                          String displayLocation, Calendar dateEvent, Date dateCreated,
                          List<String> tags, /*boolean discoverable,*/ int host) {
        this.name = name;
        this.description = description;
        this.exactLocation = exactLocation;
        this.displayLocation = displayLocation;
        this.tags = tags;
        //this.discoverable = discoverable;
        this.hostId = host;
    }

    /**
     * Adds list of attendees to the event
     * @param attendees the Users to add to the event
     */
    public void addAttendees(ArrayList<Integer> attendees) {
        this.attendees = attendees;
    }

    /**
     * Checks if the event contains a specific user as an attendee
     * @param u the User we want to find
     * @return true if User is attending, otherwise false
     */
    public boolean isAttending(int u) {
        return attendees.contains(u);
    }

    /**
     * Adds maybe attending users to the event
     * @param maybes the Users to add maybe to the event
     */
    public void addMaybes(ArrayList<Integer> maybes) {
        this.maybes = maybes;
    }

    /**
     * Checks if the event contains a specific user as maybe attending event
     * @param u the User we want to find
     * @return true if User is maybe attending, otherwise false
     */
    public boolean isMaybe(int u) {
        return maybes.contains(u);
    }

    /**
     * Gets a String with event information
     * @return a String with the event ID, event name, description, location, and tags
     */
    public String toString(){
        String joinedTags = "";
        if (this.tags != null){
            joinedTags = TextUtils.join(" ", this.tags);
        }
        return String.format("%d %s %s %s %s", this.id, this.name, this.description,
                this.displayLocation, joinedTags);
    }

    /**
     * Checks if the event is equal to another event
     * @param e the event to compare with
     * @return true if they are the same event, otherwise false
     */
    public boolean equals(Event e)
    {
        return this.id == e.getId();
    }


    /**
     * Gets the latitude location of the event
     * @return latitude
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * Sets the latitude location of the event
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude location of the event
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude location of the event
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * add an attendee to the list
     * @param u user to add
     */
    public void addAttendee(int u) {
        this.attendees.add(u);
    }

    /**
     * adds a maybe to maybe list
     * @param u user to add
     */
    public void addMaybe(int u) {
        this.maybes.add(u);
    }

}
