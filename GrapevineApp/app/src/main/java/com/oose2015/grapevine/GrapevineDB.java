package com.oose2015.grapevine;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by venussoontornprueksa on 12/1/15.
 */
public class GrapevineDB {
    public static final int GRAPEVINE_DB = 0;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    /**
     * Constructor for the database that hold events by making the writable databse
     * @param context
     */
    public GrapevineDB(Context context) {
        mDBHelper = new DBHelper(context);
        mDatabase = mDBHelper.getWritableDatabase();
    }

    /**
     * Inserts the given list of events into the database
     * @param table the table number to insert into
     * @param listEvents
     * @param clearPrevious
     */
    public void insertEvents(int table, ArrayList<Event> listEvents, boolean clearPrevious) {
        if (clearPrevious) {
            deleteEvents();
            deleteAttendees();
            deleteMaybes();
        }

        //create a sql prepared statement
        String sql = DBHelper.INSERT + DBHelper.TABLE_EVENTS + DBHelper.EVENT_VALUES;
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listEvents.size(); i++) {
            insertOneEvent(listEvents.get(i));
        }
        //set the transaction as successful and end the transaction
        Logger.debugLog("inserting entries " + listEvents.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    /**
     * Insert an event into local DB
     * @param event event to be inserted
     */
    private void insertOneEvent(Event event) {
        insertBasicEventDetails(event);
        insertEventAttendees(event.getId(), event.getAttendees());
        insertEventMaybes(event.getId(), event.getMaybes());
        // insertEventTags(event.getId(), event.getTags());
    }

    /**
     * Insert details of an event
     * @param event the event to add details to
     */
    private void insertBasicEventDetails(Event event) {
        String delete = "DELETE FROM " + DBHelper.TABLE_EVENTS + " WHERE " + DBHelper.COLUMN_EVENT_ID
                + DBHelper.EQUALS + event.getId() + DBHelper.END;
        SQLiteStatement deleteStatement = mDatabase.compileStatement(delete);
        deleteStatement.execute();

        String sql = DBHelper.INSERT + DBHelper.TABLE_EVENTS + DBHelper.EVENT_VALUES;
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        statement.clearBindings();

        //for a given column index, simply bind the data to be put inside that index
        statement.bindLong(1, event.getId());
        statement.bindString(2, event.getName());
        statement.bindString(3, event.getDescription());
        statement.bindString(4, event.getExactLocation());
        statement.bindString(5, event.getDisplayLocation());
        statement.bindString(6, event.getStartEventString());
        statement.bindString(7, event.getEndEventString());
        statement.bindString(8, event.getDateCreatedString());
        //statement.bindString(9, ((event.getDiscoverable()) ? "true" : "false"));
        statement.bindString(9, (event.getHostId() + ""));
        statement.bindString(10, ((event.getPast()) ? "true" : "false"));

        statement.execute();
    }

    /**
     * Add attendees to an event
     * @param eventId event to add attendees
     * @param attendees list of attendees
     */
    private void insertEventAttendees(int eventId, List<Integer> attendees) {
        String delete = "DELETE FROM " + DBHelper.TABLE_ATTENDEES + " WHERE " + DBHelper.COLUMN_EVENT_ID
                + DBHelper.EQUALS + eventId + DBHelper.END;
        SQLiteStatement deleteStatement = mDatabase.compileStatement(delete);
        deleteStatement.execute();

        String sql = DBHelper.INSERT + DBHelper.TABLE_ATTENDEES + DBHelper.ATTENDEES_VALUES;
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        statement.clearBindings();

        for (int i = 0; i < attendees.size(); i++) {
            statement.bindLong(1, eventId);
            statement.bindLong(2, attendees.get(i));

            statement.execute();
        }
    }

    /**
     * Add maybes to an event
     * @param eventId event to add maybes
     * @param maybes list of maybes
     */
    private void insertEventMaybes(int eventId, List<Integer> maybes) {
        String delete = "DELETE FROM " + DBHelper.TABLE_MAYBES + " WHERE " + DBHelper.COLUMN_EVENT_ID
                + DBHelper.EQUALS + eventId + DBHelper.END;
        SQLiteStatement deleteStatement = mDatabase.compileStatement(delete);
        deleteStatement.execute();

        String sql = DBHelper.INSERT + DBHelper.TABLE_MAYBES + DBHelper.MAYBES_VALUES;
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        statement.clearBindings();

        for (int i = 0; i < maybes.size(); i++) {
            statement.bindLong(1, eventId);
            statement.bindLong(2, maybes.get(i));

            statement.execute();
        }
    }

    /**
     * Deletes all events in the events table
     */
    public void deleteEvents() {
        mDatabase.execSQL("delete from " + DBHelper.TABLE_EVENTS);
    }

    /**
     * Deletes all attendees in the attendees table
     */
    public void deleteAttendees() {
        mDatabase.execSQL("delete from " + DBHelper.TABLE_ATTENDEES);
    }

    /**
     * Deletes all attendees in the attendees table
     */
    public void deleteMaybes() {
        mDatabase.execSQL("delete from "+ DBHelper.TABLE_MAYBES);
    }

    /**
     * Returns the list of events
     * @return listEvents
     */
    public ArrayList<Event> readEvents() {
        ArrayList<Event> listEvents = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] eventColumns = {
                DBHelper.COLUMN_EVENT_ID,
                DBHelper.COLUMN_NAME,
                DBHelper.COLUMN_DESCRIPTION,
                DBHelper.COLUMN_EXACT_LOCATION,
                DBHelper.COLUMN_DISPLAY_LOCATION,
                DBHelper.COLUMN_DATE_EVENT_START,
                DBHelper.COLUMN_DATE_EVENT_END,
                DBHelper.COLUMN_DATE_CREATED,
                //DBHelper.COLUMN_DISCOVERABLE,
                DBHelper.COLUMN_HOST_ID,
                DBHelper.COLUMN_PAST
        };
        Cursor cursor = mDatabase.query(DBHelper.TABLE_EVENTS, eventColumns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //create a new event object and retrieve the data from the cursor to be stored in this event object
                Event event = setBasicEventFromCursor(cursor);
                event.setAttendees(getAttendees(event.getId()));
                event.setMaybes(getMaybes(event.getId()));
                event.setTags(getTags(event.getId()));
                //add the event to the list of event objects which we plan to return
                listEvents.add(event);
            }
            while (cursor.moveToNext());
        }
        return listEvents;
    }

    /**
     * Set Event from Cursor
     * @param cursor cursor in DB
     * @return event
     */
    private Event setBasicEventFromCursor(Cursor cursor) {
        Event event = new Event();

        event.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_EVENT_ID)));
        event.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
        event.setDescription(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DESCRIPTION)));
        event.setExactLocation(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EXACT_LOCATION)));
        event.setDisplayLocation(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DISPLAY_LOCATION)));
        event.setStartEvent(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATE_EVENT_START)));
        event.setEndEvent(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATE_EVENT_END)));
        event.setDateCreated(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATE_CREATED)));
        //event.setDiscoverable(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DISCOVERABLE)));
        event.setPast(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PAST)));
        event.setHostId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_HOST_ID)));

        return event;
    }

    /**
     * Set Feed from Cursor
     * @param cursor cursor in DB
     * @return feed
     */
    private Feed setFeedFromCursor(Cursor cursor) {
        Feed feed = new Feed();
        //each step is a 2 part process, find the index of the column first, find the data of that column using
        //that index and finally set our blank user object to contain our data
        feed.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FEED_ID)));
        feed.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
        feed.setExactLocation(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EXACT_LOCATION)));
        feed.setDisplayLocation(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DISPLAY_LOCATION)));
        feed.setRadius(cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_RADIUS)));
        feed.setStartTime(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_START_TIME)));
        feed.setEndTime(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_END_TIME)));

        return feed;
    }

    /**
     * Get attendees for an event
     * @param eventId event id to get attendees
     * @return attendees
     */
    private ArrayList<Integer> getAttendees(int eventId) {
        String[] attendeesColumns = {
                DBHelper.COLUMN_EVENT_ID,
                DBHelper.COLUMN_ATTENDEE_ID
        };
        String queryAttendees = DBHelper.SELECT_ALL + DBHelper.TABLE_ATTENDEES + DBHelper.WHERE +
                DBHelper.COLUMN_EVENT_ID + DBHelper.EQUALS + eventId + DBHelper.END;
        Cursor cursor = mDatabase.rawQuery(queryAttendees, null);

        ArrayList<Integer> attendees = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                attendees.add(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ATTENDEE_ID)));
            }
            while (cursor.moveToNext());
        }
        return attendees;
    }

    /**
     * Get maybes for an event
     * @param eventId event id to get maybes
     * @return maybes
     */
    private ArrayList<Integer> getMaybes(int eventId) {
        String[] maybesColumns = {
                DBHelper.COLUMN_EVENT_ID,
                DBHelper.COLUMN_MAYBE_ID
        };
        String queryMaybes = DBHelper.SELECT_ALL + DBHelper.TABLE_MAYBES + DBHelper.WHERE +
                DBHelper.COLUMN_EVENT_ID + DBHelper.EQUALS + eventId + DBHelper.END;
        Cursor cursor = mDatabase.rawQuery(queryMaybes, null);

        ArrayList<Integer> maybes = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                maybes.add(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_MAYBE_ID)));
            }
            while (cursor.moveToNext());
        }
        return maybes;
    }

    /**
     * Get tags for an event
     * @param eventId event id to get tags for
     * @return tags
     */
    private ArrayList<String> getTags(int eventId) {
        String[] tagsColumns = {
                DBHelper.COLUMN_EVENT_ID,
                DBHelper.COLUMN_TAG
        };
        String queryTags = DBHelper.SELECT_ALL + DBHelper.TABLE_EVENT_TAGS + DBHelper.WHERE +
                DBHelper.COLUMN_EVENT_ID + DBHelper.EQUALS + eventId + DBHelper.END;
        Cursor cursor = mDatabase.rawQuery(queryTags, null);

        ArrayList<String> tags = new ArrayList<>();
        if(cursor.moveToFirst()) {
            do {
                tags.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TAG)));
            }
            while (cursor.moveToNext());
        }
        return null;
    }

    /**
     * Get Event by ID
     * @param ID id of event
     */
    public Event getEvent(int ID) {
        Event event = null;

        String queryEvent = DBHelper.SELECT_ALL + DBHelper.TABLE_EVENTS + DBHelper.WHERE +
                DBHelper.COLUMN_EVENT_ID + DBHelper.EQUALS + ID + DBHelper.END;
        Cursor cursor = mDatabase.rawQuery(queryEvent, null);
        // make the assumption that the database stores events with unique ids, so only use the first row
        if(cursor.moveToFirst()) {
            event = setBasicEventFromCursor(cursor);
            event.setAttendees(getAttendees(ID));
            event.setMaybes(getMaybes(ID));
        }
        return event;
    }

    /**
     * Get Event by ID
     * @param ID id of event
     */
    public Feed getFeed(int ID) {
        Feed feed = null;

        String queryEvent = DBHelper.SELECT_ALL + DBHelper.TABLE_FEEDS + DBHelper.WHERE +
                DBHelper.COLUMN_FEED_ID + DBHelper.EQUALS + ID + DBHelper.END;
        Cursor cursor = mDatabase.rawQuery(queryEvent, null);
        // make the assumption that the database stores events with unique ids, so only use the first row
        if(cursor.moveToFirst()) {
            feed = setFeedFromCursor(cursor);
        }
        return feed;
    }

    /**
     * Insert Users
     * @param table table to add to
     * @param user user to add
     * @param clearPrevious whether or not to clear previous
     */
    public void insertUsers(int table, User user, boolean clearPrevious) {
        ArrayList<User> userList = new ArrayList<User>();
        userList.add(user);
        insertUsers(table, userList, clearPrevious);
    }

    /**
     * Inserts the given list of users into the database
     * @param table the table number to insert into
     * @param listUsers list of users
     * @param clearPrevious whether or not to clear previous
     */
    public void insertUsers(int table, ArrayList<User> listUsers, boolean clearPrevious) {
        if (clearPrevious) {
            deleteUsers();
        }

        //create a sql prepared statement
        Logger.debugLog("Cleared previous user and inserting new user");
        String sql = DBHelper.INSERT + DBHelper.TABLE_USERS + DBHelper.USER_VALUES;
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listUsers.size(); i++) {
            User currentUser = listUsers.get(i);
            Logger.debugLog("Current user being inserted into GrapevineDB: " + currentUser.toString());
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindLong(1, currentUser.getId());
            statement.bindString(2, currentUser.getUsername());
            statement.bindString(3, (currentUser.getName() != null ? currentUser.getName() : ""));
            statement.bindString(4, currentUser.getEmail());
            statement.bindString(5, (currentUser.getBio() != null ? currentUser.getBio() : ""));

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        Logger.debugLog("inserting entries " + listUsers.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    /**
     * Deletes all users in the users table
     */
    public void deleteUsers() {
        mDatabase.execSQL("delete from " + DBHelper.TABLE_USERS);
    }

    /**
     * Deletes all users in the users table
     */
    public void deleteFeeds() {
        mDatabase.execSQL("delete from " + DBHelper.TABLE_FEEDS);
    }

    /**
     * Returns the list of users from a given table
     * @param table
     * @return listEvents
     */
    public ArrayList<User> readUsers(int table) {
        ArrayList<User> listUsers = new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {DBHelper.COLUMN_USER_ID,
                DBHelper.COLUMN_USERNAME,
                DBHelper.COLUMN_NAME,
                DBHelper.COLUMN_EMAIL,
                DBHelper.COLUMN_BIO
        };
        Cursor cursor = mDatabase.query(DBHelper.TABLE_USERS, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Logger.debugLog("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {
                //create a new user object and retrieve the data from the cursor to be stored in this user object
                User user = new User();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank user object to contain our data
                user.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_USER_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_USERNAME)));
                user.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMAIL)));
                user.setBio(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BIO)));
                //add the user to the list of user objects which we plan to return
                listUsers.add(user);
            }
            while (cursor.moveToNext());
        }
        return listUsers;
    }

    /**
     * Get User by ID
     * @param id id of user
     */
    public User getUser(int id) {
        User user = null;

        String QUERY_USER = DBHelper.SELECT_ALL + DBHelper.TABLE_USERS + DBHelper.WHERE +
                DBHelper.COLUMN_USER_ID + DBHelper.EQUALS + id + DBHelper.END;
        Cursor cursor = mDatabase.rawQuery(QUERY_USER, null);
        // make the assumption that the database stores users with unique ids, so only use the first row
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_USER_ID)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_USERNAME)));
            user.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_EMAIL)));
            user.setBio(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_BIO)));
        } else {
            Logger.debugLog("Did not find user by that id");
        }
        return user;
    }

    /**
     * Inserts the given list of feeds into the database
     * @param table table to add to
     * @param feeds feeds to add to database
     * @param clearPrevious
     */
    public void insertFeeds(int table, ArrayList<Feed> feeds, boolean clearPrevious) {
        if (clearPrevious) {
            deleteFeeds();
        }
        Logger.debugLog("insertFeeds: " + feeds.toString());
        //create a sql prepared statement
        String sql = DBHelper.INSERT + DBHelper.TABLE_FEEDS + DBHelper.FEEDS_VALUES;
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < feeds.size(); i++) {
            Feed currentFeed = feeds.get(i);
            Logger.debugLog("Inserting feeds: " + currentFeed.getName());
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindLong(1, currentFeed.getId());
            statement.bindString(2, currentFeed.getName());
            statement.bindString(3, currentFeed.getExactLocation());
            statement.bindString(4, currentFeed.getDisplayLocation());
            statement.bindLong(5, (long) currentFeed.getRadius());
            statement.bindString(6, ISO8601.calendarToTimeString(currentFeed.getStartTime()));
            statement.bindString(7, ISO8601.calendarToTimeString(currentFeed.getEndTime()));

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        Logger.debugLog("inserting entries " + feeds.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }


    /**
     * Get the titles of each feet
     * @return return an array list of feeds
     */
    public ArrayList<Feed> getFeedTitles() {
        Logger.debugLog("readFeeds: " + readFeeds().toString());
        ArrayList<Feed> feedTitles = new ArrayList<>();
        String sql = DBHelper.SELECT + DBHelper.COLUMN_FEED_ID + ", " + DBHelper.COLUMN_NAME +
                DBHelper.FROM + DBHelper.TABLE_FEEDS + DBHelper.END;
        Logger.debugLog("SQL get feeds: " + sql);
        Cursor cursor = mDatabase.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            Logger.debugLog("getFeedTitles: found feeds");
            do {
                Feed feed = new Feed();
                feed.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_FEED_ID)));
                feed.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
                feedTitles.add(feed);
            } while (cursor.moveToNext());
        } else {
            Logger.debugLog("Did not find feeds");
        }
        return feedTitles;
    }


    /**
     * Read feeds from table
     * @return list of feeds
     */
    public ArrayList<Feed> readFeeds() {
        ArrayList<Feed> listFeeds= new ArrayList<>();
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {DBHelper.COLUMN_FEED_ID,
                DBHelper.COLUMN_NAME,
                DBHelper.COLUMN_EXACT_LOCATION,
                DBHelper.COLUMN_DISPLAY_LOCATION,
                DBHelper.COLUMN_RADIUS,
                DBHelper.COLUMN_START_TIME,
                DBHelper.COLUMN_END_TIME
        };
        Cursor cursor = mDatabase.query(DBHelper.TABLE_FEEDS, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Logger.debugLog("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {
                //create a new feed object and retrieve the data from the cursor to be stored in this user object
                Feed feed = setFeedFromCursor(cursor);
                //add the feed to the list of feed objects which we plan to return
                listFeeds.add(feed);
            }
            while (cursor.moveToNext());
        }
        return listFeeds;
    }

    // PRIVATE METHODS ********************************************

    private static class DBHelper extends SQLiteOpenHelper {
        public static final String TABLE_EVENTS = " events";
        public static final String COLUMN_EVENT_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_EXACT_LOCATION = "exact_location";
        public static final String COLUMN_DISPLAY_LOCATION = "display_location";
        public static final String COLUMN_DATE_EVENT_START = "date_event_start";
        public static final String COLUMN_DATE_EVENT_END = "date_event_end";
        public static final String COLUMN_DATE_CREATED = "date_created";
        //public static final String COLUMN_DISCOVERABLE = "discoverable";
        public static final String COLUMN_HOST_ID = "host";
        public static final String COLUMN_PAST = "past";
        public static final String COLUMN_ATTENDEE_ID = "attendees";
        public static final String COLUMN_MAYBE_ID = "maybes";
        public static final String COLUMN_TAG = "tag";
        public static final String COLUMN_KEYWORD = "keyword";

        public static final String TABLE_USERS = " users";
        public static final String COLUMN_USER_ID = "id";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_BIO = "bio";

        public static final String TABLE_ATTENDEES = " attendees";
        public static final String TABLE_MAYBES = " maybes";
        public static final String TABLE_EVENT_TAGS = " tags";
        public static final String TABLE_EVENT_KEYWORDS = " keywords";

        public static final String TABLE_FEEDS = " feeds";
        public static final String COLUMN_FEED_ID = "feed_id";
        public static final String COLUMN_RADIUS = "radius";
        public static final String COLUMN_START_TIME = "startTime";
        public static final String COLUMN_END_TIME = "endTime";

        private static final String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_EVENTS + " (" +
                COLUMN_EVENT_ID + " INTEGER," +
                COLUMN_NAME + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_EXACT_LOCATION + " TEXT," +
                COLUMN_DISPLAY_LOCATION + " TEXT," +
                COLUMN_DATE_EVENT_START + " TEXT," +
                COLUMN_DATE_EVENT_END + " TEXT," +
                COLUMN_DATE_CREATED + " TEXT," +
                COLUMN_HOST_ID + " TEXT," +
                COLUMN_PAST + " TEXT" +
                ");";
        private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER," +
                COLUMN_USERNAME + " TEXT," +
                COLUMN_NAME + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_BIO + " TEXT" +
                ");";
        private static final String CREATE_TABLE_ATTENDEES = "CREATE TABLE " + TABLE_ATTENDEES + " (" +
                COLUMN_EVENT_ID + " INTEGER," +
                COLUMN_ATTENDEE_ID + " TEXT," +
                "UNIQUE(" + COLUMN_EVENT_ID + ", " + COLUMN_ATTENDEE_ID + ")" +
                ");";
        private static final String CREATE_TABLE_MAYBES = "CREATE TABLE " + TABLE_MAYBES + " (" +
                COLUMN_EVENT_ID + " INTEGER," +
                COLUMN_MAYBE_ID + " TEXT," +
                "UNIQUE(" + COLUMN_EVENT_ID + ", " + COLUMN_MAYBE_ID + ")" +
                ");";
        private static final String CREATE_TABLE_EVENT_TAGS = "CREATE TABLE " + TABLE_EVENT_TAGS + " (" +
                COLUMN_EVENT_ID + " INTEGER," +
                COLUMN_TAG + " TEXT," +
                "UNIQUE(" + COLUMN_EVENT_ID + ", " + COLUMN_TAG + ")" +
                ");";
        private static final String CREATE_TABLE_EVENT_KEYWORDS = "CREATE TABLE " + TABLE_EVENT_KEYWORDS + " (" +
                COLUMN_EVENT_ID + " INTEGER," +
                COLUMN_KEYWORD + " TEXT," +
                "UNIQUE(" + COLUMN_EVENT_ID + ", " + COLUMN_KEYWORD + ")" +
                ");";
        private static final String CREATE_TABLE_FEEDS = "CREATE TABLE " + TABLE_FEEDS + " (" +
                COLUMN_FEED_ID + " INTEGER," +
                COLUMN_NAME + " TEXT," +
                COLUMN_EXACT_LOCATION + " TEXT," +
                COLUMN_DISPLAY_LOCATION + " TEXT," +
                COLUMN_RADIUS + " REAL," +
                COLUMN_START_TIME + " TEXT," +
                COLUMN_END_TIME + " TEXT," +
                "UNIQUE(" + COLUMN_FEED_ID + ")" +
                ");";

        private static final String INSERT = "INSERT OR IGNORE INTO ";
        private static final String SELECT_ALL = "SELECT * FROM ";
        private static final String SELECT = "SELECT ";
        private static final String FROM = " FROM";
        private static final String WHERE = " WHERE ";
        private static final String EQUALS = " = ";
        private static final String END = ";";

        private static final String EVENT_VALUES = " VALUES (?,?,?,?,?,?,?,?,?,?);";
        private static final String USER_VALUES = " VALUES (?,?,?,?,?);";
        private static final String ATTENDEES_VALUES = " VALUES (?,?);";
        private static final String MAYBES_VALUES = " VALUES (?,?);";
        private static final String FEEDS_VALUES = " VALUES (?,?,?,?,?,?,?);";

        private static final String DB_NAME = "grapevine_db";
        private static final int DB_VERSION = 1;
        private Context mContext;

        /**
         * Contructs the helper class that helps with sql statements
         * @param context
         */
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        @Override
        /**
         * Performs the query to create the table of events
         */
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_EVENTS);
                Logger.debugLog("create table events executed");
                db.execSQL(CREATE_TABLE_USERS);
                Logger.debugLog("create table users executed");
                db.execSQL(CREATE_TABLE_ATTENDEES);
                Logger.debugLog("create table attendees executed");
                db.execSQL(CREATE_TABLE_MAYBES);
                Logger.debugLog("create table maybes executed");
                db.execSQL(CREATE_TABLE_EVENT_TAGS);
                Logger.debugLog("create table event tags executed");
                db.execSQL(CREATE_TABLE_EVENT_KEYWORDS);
                Logger.debugLog("create table event keywords executed");
                db.execSQL(CREATE_TABLE_FEEDS);
                Logger.debugLog("create table feeds executed");
            } catch (SQLiteException exception) {
                Logger.shortToast(exception + "");
            }
        }

        @Override
        /**
         * Performs the query to update the table of events
         */
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Logger.debugLog("upgrade table events executed");
                db.execSQL(" DROP TABLE " + TABLE_EVENTS + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_USERS + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_ATTENDEES + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_MAYBES + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_EVENT_TAGS + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_EVENT_KEYWORDS + " IF EXISTS;");
                db.execSQL(" DROP TABLE " + TABLE_FEEDS + " IF EXISTS;");
                onCreate(db);
            } catch (SQLiteException exception) {
                Logger.shortToast(exception + "");
            }
        }
    }
}