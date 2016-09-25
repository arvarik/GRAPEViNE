package com.oose2015.grapevine;

import android.annotation.TargetApi;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by venussoontornprueksa on 12/10/15.
 */
@TargetApi(21)
public class ServiceProfile extends JobService implements UserProfileLoadedListener {
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
        new TaskLoadUserProfile(this).execute();
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
     * @param user
     */
    @Override
    public void onUserProfileLoaded(User user) {
        Logger.debugLog("onUserProfileLoaded");
        jobFinished(jobParameters, false);
    }
}