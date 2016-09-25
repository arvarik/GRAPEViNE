/*
For these test cases, since we created local objects but then also sent these objects to the
Database class, we had to delete the Database instances and then create new ones with the
updated information from the local objects. We know that once we hook up the back end, we can
use SQL's update command to avoid this issue.
 */

package com.oose2015.grapevine;

import android.app.Application;
import android.test.ApplicationTestCase;
import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    //private User user;
    private Database database;
    //private UserCreationController userCreationController;
    private RequestQueue requestQueue;
    private VolleySingleton volleySingleton;


    public ApplicationTest() {
        super(Application.class);
    }


    //------------------------------------------------------------------------//
    // Tests
    //------------------------------------------------------------------------//

    public void testUserCreation() throws Exception {
        database = new Database();
        User u = new User("VenusForeverxoxo", "Venus Soontornprueksa", "venusPassword",
                "venus@gmail.com");
        User v = new User("RuchiForeverxoxo", "Ruchi Gondalia", "ruchiPassword", "ruchi@gmail.com");
        database.createUser(u);

        assertTrue(database.contains(u));

        database.createUser(v);
        assertTrue(database.contains(u));
        assertTrue(database.contains(v));
    }

    public void testEventCreation() throws Exception {
        database = new Database();
        User u = new User("VenusForeverxoxo", "Venus Soontornprueksa", "venusPassword",
                "venus@gmail.com");
        u.setId(0);
        database.createUser(u);

        Event  e = new Event("Venus's Party For 1", "Venus is partying alone", "some-coordinates",
                "Carlton", new GregorianCalendar(2015, 8, 12, 12, 12),new GregorianCalendar(2015, 12, 12, 12, 12),
                new Date(), null, u.getId());

        database.createEvent(e);
        database.deleteUser(u);
        u.addEvent(e);
        database.createUser(u);
        e.setId(1);
        assertTrue(database.contains(e));
    }

    public void testGetEventById() throws Exception {
        database = new Database();

        User u = new User("VenusForeverxoxo", "Venus Soontornprueksa", "venusPassword",
                "venus@gmail.com");
        u.setId(0);
        database.createUser(u);
        Event  e = new Event("Venus's Party For 1", "Venus is partying alone", "some-coordinates",
                "Carlton", new GregorianCalendar(2015, 8, 12, 12, 12) ,new GregorianCalendar(2015, 12, 12, 12, 12),
                new Date(), null, u.getId());
        database.createEvent(e);
        e.setId(1);

        assertEquals(e, database.searchEvents(1));

        Event f = new Event("Venus's Party For 2", "Venus is partying with 1 friend",
                "some-coordinates", "Carlton T-101", new GregorianCalendar(2015, 8, 11, 11, 00),new GregorianCalendar(2015, 11, 11, 11, 00),
                new Date(), null, u.getId());
        database.createEvent(f);
        f.setId(2);

        assertEquals(f, database.searchEvents(2));

        database.deleteEvent(f);

        Event g = new Event("Venus's Party For 3", "Venus is partying with 2 friends",
                "some-coordinates", "TBH", new GregorianCalendar(2015, 8, 10, 10, 00) ,new GregorianCalendar(2015, 10, 10, 10, 00),
                new Date(), null, u.getId());
        database.createEvent(g);
        g.setId(3);

        assertEquals(g, database.searchEvents(3));
    }

    public void testEditEvent() throws Exception {
        database = new Database();

        User u = new User("VenusForeverxoxo", "Venus Soontornprueksa", "venusPassword",
                "venus@gmail.com");
        u.setId(0);
        database.createUser(u);

        Event  e = new Event("Venus's Party For 1", "Venus is partying alone", "some-coordinates",
                "Carlton", new GregorianCalendar(2015, 8, 12, 12, 12), new GregorianCalendar(2015, 12, 12, 12, 12),
                new Date(), null, u.getId());
        database.createEvent(e);
        u.addEvent(e);

        Event updateE = database.searchEvents(1);

        updateE.editEvent(updateE.getName(), "Please come. Please.", updateE.getExactLocation(),
                updateE.getDisplayLocation(), updateE.getStartEvent(), updateE.getDateCreated(),
                updateE.getTags(), updateE.getHostId());
        updateE.setId(1);

        database.updateEvent(database.searchEvents(1), updateE);
        assertEquals(updateE, database.searchEvents(1));
    }

    public void testAddAttendee() throws Exception {
        database = new Database();

        User u = new User("VenusForeverxoxo", "Venus Soontornprueksa", "venusPassword",
                "venus@gmail.com");
        u.setId(0);
        database.createUser(u);

        Event  e = new Event("Venus's Party For 1", "Venus is partying alone", "some-coordinates",
                "Carlton", new GregorianCalendar(2015, 8, 12, 12, 12),new GregorianCalendar(2015, 12, 12, 12, 12),
                new Date(), null, u.getId());
        database.createEvent(e);
        database.deleteUser(u);
        u.addEvent(e);
        database.createUser(u);
        e.addAttendee(u.getId());
        database.updateEvent(database.searchEvents(1), e);
        User v = new User("RuchiForeverxoxo", "Ruchi Gondalia", "ruchiPassword", "ruchi@gmail.com");
        v.setId(0);
        database.createUser(v);
        database.deleteUser(v);
        v.addEvent(e);
        database.createUser(v);
        e.setId(1);
        e.addAttendee(v.getId());
        database.updateEvent(database.searchEvents(1), e);
        assertTrue(database.searchEvents(1).isAttending(u.getId()));
    }

    public void testAddMaybe() throws Exception {
        database = new Database();

        User u = new User("VenusForeverxoxo", "Venus Soontornprueksa", "venusPassword",
                "venus@gmail.com");
        u.setId(0);
        database.createUser(u);

        Event  e = new Event("Venus's Party For 1", "Venus is partying alone", "some-coordinates",
                "Carlton", new GregorianCalendar(2015, 8, 12, 12, 12), new GregorianCalendar(2015, 12, 12, 12, 12),
                new Date(), null, u.getId());
        database.createEvent(e);
        database.deleteUser(u);
        u.addEvent(e);
        database.createUser(u);
        e.addMaybe(u.getId());
        database.updateEvent(database.searchEvents(1), e);
        User v = new User("RuchiForeverxoxo", "Ruchi Gondalia", "ruchiPassword", "ruchi@gmail.com");
        v.setId(0);
        database.createUser(v);
        database.deleteUser(v);
        v.addEvent(e);
        database.createUser(v);
        e.setId(1);
        e.addMaybe(v.getId());
        database.updateEvent(database.searchEvents(1), e);
        assertTrue(database.searchEvents(1).isMaybe(u.getId()));
    }

    public void testRemoveEvent() throws Exception {
        database = new Database();

        User u = new User("VenusForeverxoxo", "Venus Soontornprueksa", "venusPassword",
                "venus@gmail.com");
        u.setId(0);
        database.createUser(u);
        User v = new User("RuchiForeverxoxo", "Ruchi Gondalia", "ruchiPassword", "ruchi@gmail.com");
        database.createUser(v);
        v.setId(0);

        Event  e = new Event("Venus's Party For 1", "Venus is partying alone", "some-coordinates",
                "Carlton", new GregorianCalendar(2015, 8, 12, 12, 12), new GregorianCalendar(2015, 12, 12, 12, 12),
                new Date(), null, u.getId());
        database.createEvent(e);
        database.deleteUser(u);
        u.addEvent(e);
        database.createUser(u);
        database.deleteUser(v);
        v.addEvent(e);
        database.createUser(v);
        e.setId(1);
        e.addAttendee(v.getId());
        database.updateEvent(database.searchEvents(1), e);

        database.deleteUser(u);
        u.removeEvent(e);
        database.createUser(u);

        database.deleteUser(v);
        v.removeEvent(e);
        database.createUser(v);

        assertFalse(database.getUser(u).contains(database.getUser(u).getEvent(1)));
    }

    public void testSearchForEvent() throws Exception {
        database = new Database();

        User u = new User("VenusForeverxoxo", "Venus Soontornprueksa", "venusPassword",
                "venus@gmail.com");
        u.setId(0);
        database.createUser(u);

        Event  e = new Event("Venus's Party For 1", "Venus is partying alone", "some-coordinates",
                "Carlton", new GregorianCalendar(2015, 8, 12, 12, 30),new GregorianCalendar(2015, 12, 12, 12, 30),
                new Date(), null,u.getId());
        Event f = new Event("Venus's Party For 2", "Venus is partying with 1 friend",
                "some-coordinates", "Carlton T-101", new GregorianCalendar(2015, 8, 12, 12, 30),new GregorianCalendar(2015, 11, 11, 11, 00),
                new Date(), null, u.getId());
        Event g = new Event("Venus's Party For 3", "Venus is partying with 2 friends",
                "some-coordinates", "TBH", new GregorianCalendar(2015, 8, 12, 12, 30),new GregorianCalendar(2015, 10, 10, 10, 00),
                new Date(), null, u.getId());
        database.createEvent(e);
        database.createEvent(f);
        database.createEvent(g);

        ArrayList<Event> searchResult = database.searchEvents("Carlton");

        for(int i = 0; i < searchResult.size(); i++){
            assertTrue(searchResult.get(i).toString().contains("Carlton"));
        }
    }

    public void testDeleteEvent() throws Exception {
        database = new Database();

        User u = new User("VenusForeverxoxo", "Venus Soontornprueksa", "venusPassword",
                "venus@gmail.com");
        u.setId(0);
        database.createUser(u);

        Event  e = new Event("Venus's Party For 1", "Venus is partying alone", "some-coordinates",
                "Carlton", new GregorianCalendar(2015, 8, 12, 12, 30), new GregorianCalendar(2015, 12, 12, 12, 12),
                new Date(), null, u.getId());

        database.createEvent(e);
        database.deleteEvent(e);

        assertFalse(database.contains(e));
    }

    public void testAddFeed() throws Exception {
        database = new Database();
        User u = new User("VenusForeverxoxo", "Venus Soontornprueksa", "venusPassword",
                "venus@gmail.com");
        u.setId(0);
        database.createUser(u);

        List<String> tags = new ArrayList<String>();
        tags.add("OOSE");
        tags.add("Study");
        Feed f = new Feed("OOSE", "Malone", "some-coordinates", 10, tags, null, null);
        database.deleteUser(u);
        u.addFeed(f);
        database.createUser(u);
        database.createFeed(f);

        assertTrue(database.contains(f));
    }

    public void testRemoveFeed() throws Exception {
        database = new Database();
        User u = new User("VenusForeverxoxo", "Venus Soontornprueksa", "venusPassword",
                "venus@gmail.com");
        u.setId(0);
        database.createUser(u);

        List<String> tags = new ArrayList<String>();
        tags.add("OOSE");
        tags.add("Study");
        Feed f = new Feed("OOSE", "Malone", "some-coordinates", 10, tags, null, null);
        database.deleteUser(u);
        u.addFeed(f);
        database.createUser(u);
        database.createFeed(f);
        database.deleteUser(u);
        u.removeFeed(f);
        database.createUser(u);

        database.removeFeed(f);

        assertFalse(database.contains(f));
    }
}