package com.oose2015.grapevine;

import android.annotation.TargetApi;

import java.util.ArrayList;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by venussoontornprueksa on 12/1/15.
 */
@TargetApi(21)
public class ServiceAllEvents extends JobService implements AllEventsLoadedListener {
    private JobParameters jobParameters;

    /**
     * Sets the job parameters and executes the task to load events
     * @param jobParameters
     * @return
     */
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Logger.debugLog("onStartJob");
        this.jobParameters = jobParameters;
        new TaskLoadAllEvents(this, 0).execute();
        return true;
    }

    /**
     * Indicates when the job is stopped
     * @param jobParameters
     * @return false
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Logger.debugLog("onStopJob");
        return false;
    }

    /**
     * Indicates when all events are loaded
     * @param listEvents
     */
    @Override
    public void onAllEventsLoaded(ArrayList<Event> listEvents) {
        Logger.debugLog("onAllEventsLoaded");
        jobFinished(jobParameters, false);
    }
}