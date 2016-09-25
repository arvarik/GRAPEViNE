package com.oose2015.grapevine;

/**
 * Created by venussoontornprueksa on 11/11/15.
 */
public interface Keys {

    /**
     * Keys used for shared preferences
     */
    public interface SharedPreferencesKeys {
        public static final String KEY_EDIT_MODE = "editMode";
        public static final String KEY_DELETE_MODE = "deleteMode";
    }

    /**
     * Keys used for intents.
     */
    public interface IntentKeys {
        public static final String KEY_EVENT = "event";
        public static final String KEY_CLICKED_EVENT = "clicked_event";
        public static final String KEY_EVENT_TITLE = "event_title";
        public static final String KEY_FEED = "feed";
        public static final String KEY_FEED_TITLE = "feed_title";
        public static final String KEY_RADIUS = "radius";
        public static final String KEY_DISPLAY_LOCATION = "display_location";
        public static final String KEY_HOUR_START = "hourStart";
        public static final String KEY_MINUTE_START = "minuteStart";
        public static final String KEY_HOUR_END = "hourEnd";
        public static final String KEY_MINUTE_END = "minuteEnd";
        public static final String KEY_START_DAY = "startDay";
        public static final String KEY_START_MONTH = "startMonth";
        public static final String KEY_START_YEAR = "startYear";
        public static final String KEY_END_DAY = "endDay";
        public static final String KEY_END_MONTH = "endMonth";
        public static final String KEY_END_YEAR = "endYear";
        public static final String KEY_DISCOVERABLE = "discoverable";
        public static final String KEY_LATITUDE = "latitude";
        public static final String KEY_LONGITUDE = "longitude";
        public static final String KEY_DESCRIPTION = "description";
        public static final String KEY_TITLE = "title";
        public static final String KEY_EVENT_LOCATION = "eventLocation";
        public static final String KEY_ALL_DAY = "allDay";
        public static final String KEY_FLAG = "flag";
        public static final String KEY_QUERY = "query";
        public static final String KEY_EVENT_ID = "id";
    }

    /**
     * Keys used when requesting user information.
     */
    public interface EndpointUser {
        public static final String KEY_USER = "user";
        public static final String KEY_ID = "id";
        public static final String KEY_USER_ID = "userID";
        public static final String KEY_USERNAME = "username";
        public static final String KEY_NAME = "name";
        public static final String KEY_PASSWORD = "password";
        public static final String KEY_OLD_PASSWORD = "oldPassword";
        public static final String KEY_EMAIL = "email";
        public static final String KEY_BIO = "bio"; // might make new interface: EndpointProfile
        public static final String KEY_CREATED_EVENTS = "createdEvents";
        public static final String KEY_ATTENDED_EVENTS = "attendedEvents";
        public static final String KEY_FEEDS = "feeds"; // will probably make interface: EndpointFeed
    }

    /**
     * Keys used when requesting event information.
     */
    public interface EndpointEvents {
        public static final String KEY_EVENTS = "events";
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_DESCRIPTION = "description";
        public static final String KEY_EXACT_LOCATION = "exactLocation";
        public static final String KEY_DISPLAY_LOCATION = "displayLocation";
        public static final String KEY_DATE_EVENT_START = "dateEventStart";
        public static final String KEY_DATE_EVENT_END = "dateEventEnd";
        public static final String KEY_DATE_CREATED = "dateCreated";
        public static final String KEY_TAGS = "tags";
        public static final String KEY_DISCOVERABLE = "discoverable";
        public static final String KEY_HOST = "host";
        public static final String KEY_ATTENDEES = "attendees";
        public static final String KEY_MAYBES = "maybes";
        public static final String KEY_PAST = "past";
    }

    public interface EndpointFeeds {
        public static final String KEY_ID = "id";
        public static final String KEY_FEEDS = "feeds";
        public static final String KEY_NAME = "name";
        public static final String KEY_OWNER = "owner";
        public static final String KEY_EXACT_LOCATION = "exactLocation";
        public static final String KEY_DISPLAY_LOCATION = "displayLocation";
        public static final String KEY_RADIUS = "radius";
        public static final String KEY_START_TIME = "startTime";
        public static final String KEY_END_TIME = "endTime";
        public static final String KEY_FEED_KEYWORDS = "feedKeywords";
        public static final String KEY_FEED_TAGS = "feedTags";
    }
}
