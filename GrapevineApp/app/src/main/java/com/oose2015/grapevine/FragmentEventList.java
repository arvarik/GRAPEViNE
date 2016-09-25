package com.oose2015.grapevine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by venussoontornprueksa on 12/4/15.
 */
public class FragmentEventList extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AllEventsLoadedListener {

    private static final String STATE_EVENTS = "state_events";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final java.lang.String ARG_PAGE = "arg_page";
    public static final java.lang.String FEED_ID = "feedId";
    private ArrayList<Event> mListEvents = new ArrayList<Event>();
    private EventListAdapter mAdapterToday;
    private EventListAdapter mAdapterTomorrow;
    private EventListAdapter mAdapterThisWeek;
    private EventListAdapter mAdapterFuture;
    private RecyclerView mRecyclerViewToday;
    private RecyclerView mRecyclerViewTomorrow;
    private RecyclerView mRecyclerViewThisWeek;
    private RecyclerView mRecyclerViewFuture;
    private SwipeRefreshLayout mSwipeLayout;
    private ArrayList<Event> mToday;
    private ArrayList<Event> mTomorrow;
    private ArrayList<Event> mThisWeek;
    private ArrayList<Event> mFuture;
    private int mFeedId;

    /**
     * Creates a FragmentEventList object.
     */
    public FragmentEventList() {
    }

    /**
     * Create a new instance of FragmentEventList, initialized to show the text at 'pageNumber'.
     * @params pageNumber to show text
     * @return FragmentEventList new instance
     */
    public static FragmentEventList newInstance(int pageNumber, int feedId) {
        FragmentEventList fragmentEventList = new FragmentEventList();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_PAGE, pageNumber);
        arguments.putInt(FEED_ID, feedId);
        fragmentEventList.setArguments(arguments);
        return fragmentEventList;
    }

    public int getmFeedId() {
        return getArguments().getInt(FEED_ID);
    }

    @Override
    /**
     * Called to have the fragment instantiate its user interface view.
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_event_list, container, false);
        mSwipeLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);

        mRecyclerViewToday = (RecyclerView) layout.findViewById(R.id.recycler_view_today);
        mRecyclerViewTomorrow = (RecyclerView) layout.findViewById(R.id.recycler_view_tomorrow);
        mRecyclerViewThisWeek = (RecyclerView) layout.findViewById(R.id.recycler_view_this_week);
        mRecyclerViewFuture = (RecyclerView) layout.findViewById(R.id.recycler_view_future);

        mRecyclerViewToday.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerViewToday.setHasFixedSize(false);
        mRecyclerViewToday.setLayoutManager(new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerViewToday.setItemAnimator(new DefaultItemAnimator());

        mRecyclerViewTomorrow.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerViewTomorrow.setHasFixedSize(false);
        mRecyclerViewTomorrow.setLayoutManager(new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerViewTomorrow.setItemAnimator(new DefaultItemAnimator());

        mRecyclerViewThisWeek.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerViewThisWeek.setHasFixedSize(false);
        mRecyclerViewThisWeek.setLayoutManager(new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerViewThisWeek.setItemAnimator(new DefaultItemAnimator());

        mRecyclerViewFuture.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mRecyclerViewFuture.setHasFixedSize(false);
        mRecyclerViewFuture.setLayoutManager(new MyLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerViewFuture.setItemAnimator(new DefaultItemAnimator());

        new TaskLoadAllEvents(this, getmFeedId()).execute();
        mListEvents = Grapevine.getWritableDatabase().readEvents();
        setLists(mListEvents);

        return layout;
    }

    /**
     * Method run when resuming this activity when coming back from another activity
     */
    @Override
    public void onResume() {
        super.onResume();
        Logger.debugLog("Fragment: executing task from fragment");
        new TaskLoadAllEvents(this, getmFeedId()).execute();
        setLists(mListEvents);
    }

    /**
     * Sort what events go into My Events or Events Attending
     * @param list list of events to be sorted
     */
    public void setLists(ArrayList<Event> list) {
        mToday = new ArrayList<>();
        mTomorrow = new ArrayList<>();
        mThisWeek = new ArrayList<>();
        mFuture = new ArrayList<>();

        for(Event e : list) {
            e.getStartEvent();
            Calendar now = Calendar.getInstance();
            Calendar event = e.getStartEvent();

            if(event.getTimeInMillis() - now.getTimeInMillis() <= DateUtils.DAY_IN_MILLIS) {
                mToday.add(e);
            } else if(event.getTimeInMillis() - now.getTimeInMillis() <= DateUtils.DAY_IN_MILLIS*2) {
                mTomorrow.add(e);
            } else if(event.getTimeInMillis() - now.getTimeInMillis() <= DateUtils.WEEK_IN_MILLIS) {
                mThisWeek.add(e);
            } else {
                mFuture.add(e);
            }
        }

        LinearLayout todayLabel = (LinearLayout) mSwipeLayout.findViewById(R.id.today_label);
        LinearLayout tomorrowLabel = (LinearLayout) mSwipeLayout.findViewById(R.id.tomorrow_label);
        LinearLayout thisWeekLabel = (LinearLayout) mSwipeLayout.findViewById(R.id.this_week_label);
        LinearLayout futureLabel = (LinearLayout) mSwipeLayout.findViewById(R.id.future_label);

        if(mToday.size() == 0) {
            todayLabel.setVisibility(LinearLayout.GONE);
        } else {
            todayLabel.setVisibility(LinearLayout.VISIBLE);
        }
        if(mTomorrow.size() == 0) {
            tomorrowLabel.setVisibility(LinearLayout.GONE);
        } else {
            tomorrowLabel.setVisibility(LinearLayout.VISIBLE);
        }
        if(mThisWeek.size() == 0) {
            thisWeekLabel.setVisibility(LinearLayout.GONE);
        } else {
            thisWeekLabel.setVisibility(LinearLayout.VISIBLE);
        }
        if(mFuture.size() == 0) {
            futureLabel.setVisibility(LinearLayout.GONE);
        } else {
            futureLabel.setVisibility(LinearLayout.VISIBLE);
        }

        mAdapterToday = new EventListAdapter(getActivity(), mToday);
        mAdapterTomorrow = new EventListAdapter(getActivity(), mTomorrow);
        mAdapterThisWeek = new EventListAdapter(getActivity(), mThisWeek);
        mAdapterFuture = new EventListAdapter(getActivity(), mFuture);

        mRecyclerViewToday.setAdapter(mAdapterToday);
        mRecyclerViewTomorrow.setAdapter(mAdapterTomorrow);
        mRecyclerViewThisWeek.setAdapter(mAdapterThisWeek);
        mRecyclerViewFuture.setAdapter(mAdapterFuture);

        LinearLayout.LayoutParams paramsToday = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramsTomorrow = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramsThisWeek = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramsFuture = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // calculate height of RecyclerView based on child count
        paramsToday.height=73* mToday.size();
        // set height of RecyclerView
        mRecyclerViewToday.setLayoutParams(paramsToday);
        paramsTomorrow.height=73* mTomorrow.size();
        mRecyclerViewTomorrow.setLayoutParams(paramsTomorrow);
        paramsThisWeek.height=73* mThisWeek.size();
        mRecyclerViewThisWeek.setLayoutParams(paramsThisWeek);
        paramsFuture.height=73* mFuture.size();
        mRecyclerViewFuture.setLayoutParams(paramsFuture);
    }

    /**
     * Sets the events for the adapter once all events are loaded
     * @param listEvents
     */
    @Override
    public void onAllEventsLoaded(ArrayList<Event> listEvents) {
        Logger.debugLog("MainScreen Fragment: onAllEventsLoaded Fragment");
        if (mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
        setLists(listEvents);
    }

    /**
     * save the event list to a parcelable prior to rotation or configuration change
     * @param outState outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_EVENTS, mListEvents);
    }

    /**
     * Run when user swipes down to refresh list
     */
    @Override
    public void onRefresh() {
        //load the whole feed again on refresh
        new TaskLoadAllEvents(this, getmFeedId()).execute();
    }
}