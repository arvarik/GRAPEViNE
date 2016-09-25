package com.oose2015.grapevine;

import java.util.ArrayList;

/**
 * Interface for defining what the application should do when all events are loaded from the server
 * Created by venussoontornprueksa on 12/2/15.
 */
public interface AllEventsLoadedListener {

    /**
     * Defines what needs to be done when all events are loaded given the list of events
     * @param listEvents
     */
    public void onAllEventsLoaded(ArrayList<Event> listEvents);
}
