/*
 *  Copyright 2018 Keval Patel.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.kevalpatel2106.standup.core.sleepManager

import com.evernote.android.job.Job
import com.evernote.android.job.JobRequest
import com.kevalpatel2106.common.UserSessionManager
import com.kevalpatel2106.common.UserSettingsManager
import com.kevalpatel2106.common.application.BaseApplication
import com.kevalpatel2106.standup.core.Core
import com.kevalpatel2106.standup.core.activityMonitor.ActivityMonitorJob
import com.kevalpatel2106.standup.core.di.DaggerCoreComponent
import com.kevalpatel2106.standup.core.reminder.NotificationSchedulerJob
import com.kevalpatel2106.utils.SharedPrefsProvider
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Keval on 19/01/18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
internal class SleepModeMonitoringJob : Job() {

    companion object {
        /**
         * Unique tag for the job which notifies when the sleep mode starts.
         */
        const val SLEEP_MODE_START_JOB_TAG = "sleep_mode_start_job_tag"

        /**
         * Unique tag for the job which notifies when the sleep mode ends.
         */
        const val SLEEP_MODE_END_JOB_TAG = "sleep_mode_end_job_tag"

        /**
         * Schedule the job to notify when the sleep mode starts and when the sleep mode ends.
         *
         * Here two jobs will be scheduled:
         * - Job with the tag [SLEEP_MODE_START_JOB_TAG] will run this job whenever the sleep mode
         * starts. This job is scheduled to run on [SleepModeMonitoringHelper.getSleepStartTiming]
         * unix milliseconds. At this time, job will cancel the [NotificationSchedulerJob] to prevent
         * stand up reminder notifications and [ActivityMonitorJob] will stop monitoring user's
         * activity. This will set [UserSettingsManager.isCurrentlyInSleepMode].
         * - Job with the tag [SLEEP_MODE_END_JOB_TAG] will run this job whenever the sleep mode
         * ends. This job is scheduled to run on [SleepModeMonitoringHelper.getSleepEndTiming]
         * unix milliseconds. At this time, job will reschedule the [NotificationSchedulerJob] to
         * display reminder notifications and [ActivityMonitorJob] to start monitoring user's activity.
         * This will reset [UserSettingsManager.isCurrentlyInSleepMode].
         *
         * This job is one-shot job not periodic. At the end of [SLEEP_MODE_END_JOB_TAG] job, it
         * will reschedule this job for the next day.
         *
         * @return True if both the jobs are scheduled successfully else false.
         * @see SleepModeMonitoringHelper.getSleepStartTiming
         * @see SleepModeMonitoringHelper.getSleepEndTiming
         */
        @JvmStatic
        internal fun scheduleJob(userSettingsManager: UserSettingsManager): Boolean {

            return synchronized(ActivityMonitorJob::class) {

                //Arrange the DND start job
                val startSleepJobTime = SleepModeMonitoringHelper.getSleepStartTiming(userSettingsManager)
                val startJobId = JobRequest.Builder(SLEEP_MODE_START_JOB_TAG)
                        .setUpdateCurrent(true)
                        .setExact(startSleepJobTime - System.currentTimeMillis())
                        .build()
                        .schedule()

                Timber.i("`Sleep mode begin` monitoring job with id $startJobId scheduled at $startSleepJobTime milliseconds.")

                //Arrange the DND end job
                val endSleepJobTime = SleepModeMonitoringHelper.getSleepEndTiming(userSettingsManager)
                val endJobId = JobRequest.Builder(SLEEP_MODE_END_JOB_TAG)
                        .setUpdateCurrent(true)
                        .setExact(endSleepJobTime - System.currentTimeMillis())
                        .build()
                        .schedule()

                Timber.i("`Sleep mode end` monitoring job with id $endJobId scheduled after $endSleepJobTime milliseconds.")

                //Check if while scheduling this job, is sleep mode should turn on?
                userSettingsManager.isCurrentlyInSleepMode = SleepModeMonitoringHelper.isCurrentlyInSleepMode(userSettingsManager)

                return@synchronized true
            }
        }
    }

    @Inject
    lateinit var userSettingsManager: UserSettingsManager

    @Inject
    lateinit var sharedPrefsProvider: SharedPrefsProvider

    @Inject
    lateinit var userSessionManager: UserSessionManager

    override fun onRunJob(params: Params): Result {
        //Inject dependencies.
        DaggerCoreComponent.builder()
                .appComponent(BaseApplication.getApplicationComponent())
                .build()
                .inject(this@SleepModeMonitoringJob)

        when (params.tag) {
            SLEEP_MODE_START_JOB_TAG -> sleepModeStarted()
            SLEEP_MODE_END_JOB_TAG -> sleepModeEnded()
            else -> throw IllegalStateException("Invalid tag: ${params.tag}")
        }

        return Result.SUCCESS
    }

    private fun sleepModeStarted() {
        if (userSettingsManager.isAutoDndEnable) {

            //Turn on sleep mode
            userSettingsManager.isCurrentlyInSleepMode = true

            Core(userSessionManager = userSessionManager,
                    userSettingsManager = userSettingsManager,
                    prefsProvider = sharedPrefsProvider)
                    .refresh()

            SleepModeStartNotification.notify(context)
        }
    }

    private fun sleepModeEnded() {
        //Turn off sleep mode
        userSettingsManager.isCurrentlyInSleepMode = false

        Core(userSessionManager = userSessionManager,
                userSettingsManager = userSettingsManager,
                prefsProvider = sharedPrefsProvider)
                .refresh()

        //Schedule the next sleep monitoring job
        scheduleJob(userSettingsManager)

        SleepModeStartNotification.cancel(context)
    }
}
