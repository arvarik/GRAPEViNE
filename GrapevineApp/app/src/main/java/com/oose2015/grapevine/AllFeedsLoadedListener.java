package com.oose2015.grapevine;

import java.util.ArrayList;

/**
 * Created by venussoontornprueksa on 12/15/15.
 */
public interface AllFeedsLoadedListener {
    /**
     * Defines what needs to be done when all events are loaded given the list of events
     * @param listFeeds
     */
    public void onAllFeedsLoaded(ArrayList<Feed> listFeeds);
}
