package com.oose2015.grapevine;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * Created by venussoontornprueksa on 12/16/15.
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Feed> feeds;
    Set<Map.Entry<Integer, String>> feedsMap;

    /**
     * Creates a MyPageAdapter object.
     * @param fm FragmentManager object
     */
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        this.feeds = Grapevine.getWritableDatabase().getFeedTitles();
    }

    @Override
    /**
     * Gets the Fragment at 'position'.
     * @return the Fragment at that position
     */
    public Fragment getItem(int position) {
        FragmentEventList myFragment;
        if (feeds.size() == 0 || position == 0)
            myFragment = FragmentEventList.newInstance(position, 0);
        else
            myFragment = FragmentEventList.newInstance(position, feeds.get(position-1).getId());
        return myFragment;
    }

    @Override
    /**
     * Get the count of tabs.
     * @return the number of tabs
     */
    public int getCount() {
        // extra feed is default feed
        return feeds.size() + 1;
    }

    @Override
    /**
     * Gets the title of the tab.
     * @return name of the tab
     */
    public CharSequence getPageTitle(int position) {
        if (feeds.size() == 0 || position == 0) {
            return "Default";
        } else {
            Logger.debugLog("Position: " + position);
            return feeds.get(position - 1).getName();
        }
    }
}
