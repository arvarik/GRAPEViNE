package com.oose2015.grapevine;

import android.annotation.TargetApi;

import java.util.ArrayList;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by venussoontornprueksa on 12/15/15.
 */
@TargetApi(21)
public class ServiceFeeds extends JobService implements AllFeedsLoadedListener{
    private JobParameters jobParameters;

    /**
     * Sets the job parameters and executes the task to load feeds
     * @param jobParameters
     * @return
     */
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Logger.debugLog("onStartJob: Feeds");
        this.jobParameters = jobParameters;
        new TaskLoadAllFeeds(this).execute();
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
     * Indicates when all feeds are loaded
     * @param listFeeds
     */
    @Override
    public void onAllFeedsLoaded(ArrayList<Feed> listFeeds) {
        Logger.debugLog("onAllFeedsLoaded");
        jobFinished(jobParameters, false);
    }
}
