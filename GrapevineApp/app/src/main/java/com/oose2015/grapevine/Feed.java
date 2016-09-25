package com.oose2015.grapevine;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Vamsi on 10/12/2015.
 */
public class Feed implements Serializable {

    private int id;
    private String name;
    private String exactLocation;
    private double latitude;
    private double longitude;
    private String displayLocation;
    private double radius;
    private List<String> tags;
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;

    /**
     * Empty constructor to create a Feed.
     */
    public Feed() {
    }

    /**
     * Constructor used for testing recyclerViews.
     * @param name name of the feed
     */
    public Feed(String name) {
        this.name = name;
    }

    /**
     * Creates a Feed object.
     * @param name name of the feed
     * @param exactLocation the exact location of the feed
     * @param displayLocation the display location of the feed
     * @param radius the radius of events within the feed
     * @param tags tags associated with feed
     * @param startTime the start time of events in the feed
     * @param endTime the end time of events in the feed
     */
    public Feed(String name, String exactLocation, String displayLocation, double radius,
                /*List<String> keywords,*/ List<String> tags, GregorianCalendar startTime, GregorianCalendar endTime) {
        this.name = name;
        this.exactLocation = exactLocation;
        this.displayLocation = displayLocation;
        this.radius = radius;
        //this.keywords = keywords;
        this.tags = tags;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Edits the feed.
     * @param title name of the feed
     * @param exactLocation the exact location of the feed
     * @param displayLocation the display location of the feed
     * @param radius the radius of events within the feed
     * @param tags tags associated with feed
     * @param startTime the start time of events in the feed
     * @param endTime the end time of events in the feed
     */
    public void editFeed(String title, String exactLocation, String displayLocation, double radius,
                         /*List<String> keywords,*/ List<String> tags, Date startTime, Date endTime) {

    }

    /**
     * Selects the location of the event.
     * @return true if the location was found, false otherwise
     */
    public boolean selectLocation() {
        return true;
    }

    /**
     * Gets the name of the feed.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the feed.
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the display location
     * @return displayLocation
     */
    public String getDisplayLocation() {
        return displayLocation;
    }

    /**
     * Sets the display location of the feed
     * @param displayLocation
     */
    public void setDisplayLocation(String displayLocation) {
        this.displayLocation = displayLocation;
    }

    /**
     * Gets the radius of the feed
     * @return radius
     */
    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Gets the tags
     * @return tags
     */
    public List<String> getTags() { return tags; }

    /**
     * Gets the start time
     * @return startTime
     */
    public GregorianCalendar getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time given a string
     * @param startTimeString
     */
    public void setStartTime(String startTimeString) {
        startTime = ISO8601.stringToTime(startTimeString);
    }

    /**
     * Gets the end time
     * @return endTime
     */
    public GregorianCalendar getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTimeString) {
        endTime = ISO8601.stringToTime(endTimeString);
    }

    /**
     * Get the latitude of a feed
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Set the latitude of a feed
     * @param latitude latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Get the longitude of the feed
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude for a feed
     * @param longitude longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets id of feed
     * @return id of feed
     */
    public int getId() {
        return id;
    }

    /**
     * Sets feed id
     * @param id id of feed
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets exact location from location sent in
     * @param location location to set to latitutde and longitude
     */
    public void setExactLocation(String location) {
        latitude = Double.parseDouble(location.substring(1, location.indexOf(',')));
        longitude = Double.parseDouble(location.substring(location.indexOf(',')+1, location.length()-1));
    }

    public String getExactLocation() {
        return "(" + latitude + "," + longitude + ")";
    }
}
