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

package com.standup.core.reminder

import android.annotation.SuppressLint
import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import com.kevalpatel2106.common.base.BaseApplication
import com.kevalpatel2106.common.prefs.UserSessionManager
import com.kevalpatel2106.common.prefs.UserSettingsManager
import com.kevalpatel2106.utils.SharedPrefsProvider
import com.standup.core.CoreConfig
import com.standup.core.CorePrefsProvider
import com.standup.core.di.DaggerCoreComponent
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Kevalpatel2106 on 13-Dec-17.
 * Service to fire reminder notification. This job will run after given duration and display the
 * [ReminderNotification] if user is logged in. This service will automatically schedule the next
 * job after [CoreConfig.STAND_UP_REMINDER_INTERVAL].
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
internal class NotificationSchedulerJob : Job() {

    companion object {

        /**
         * Unique tag to identify [NotificationSchedulerJob].
         */
        internal const val REMINDER_NOTIFICATION_JOB_TAG = "reminder_notification_job"

        /**
         * Schedule the [NotificationSchedulerJob] job after [scheduleInterval] milliseconds.
         * This will override all the previous scheduled [NotificationSchedulerJob] job. This job is
         * recurring and having interval of [scheduleInterval].
         *
         * THIS METHOD IS FOR INTERNAL USE. USE [com.standup.core.Core.setUpReminderNotification]
         * FOR SCHEDULING OR CANCELING THE JOB BASED ON THE USER SETTINGS.
         *
         * @see com.standup.core.Core.setUpReminderNotification
         */
        @SuppressLint("VisibleForTests")
        @JvmStatic
        internal fun scheduleNotification(sharedPrefsProvider: SharedPrefsProvider,
                                          scheduleInterval: Long = CoreConfig.STAND_UP_REMINDER_INTERVAL) {

            synchronized(NotificationSchedulerJob::class) {

                //Save the time of upcoming notification.
                CorePrefsProvider(sharedPrefsProvider)
                        .saveNextNotificationTime(System.currentTimeMillis() + scheduleInterval)

                //Schedule the job
                val id = JobRequest.Builder(REMINDER_NOTIFICATION_JOB_TAG)
                        .setUpdateCurrent(true)
                        .setPeriodic(scheduleInterval, CoreConfig.NOTIFICATION_SERVICE_PERIOD_TOLERANCE)
                        .build()
                        .schedule()

                Timber.i("Next notification job with id $id scheduled after $scheduleInterval milliseconds.")
            }
        }

        /**
         * Cancel all the [NotificationSchedulerJob] jobs if any job is scheduled with
         * [REMINDER_NOTIFICATION_JOB_TAG].
         *
         * THIS METHOD IS FOR INTERNAL USE. USE [com.standup.core.Core.setUpReminderNotification]
         * FOR SCHEDULING OR CANCELING THE JOB BASED ON THE USER SETTINGS.
         */
        @JvmStatic
        fun cancelJob(sharedPrefsProvider: SharedPrefsProvider) {
            JobManager.instance().cancelAllForTag(REMINDER_NOTIFICATION_JOB_TAG)

            //Remove the next reminder time. We are not having any next reminder scheduled.
            //Save the time of upcoming notification.
            CorePrefsProvider(sharedPrefsProvider).saveNextNotificationTime(0)

            Timber.i("All the notification job canceled.")
        }

        /**
         * Get new instance of [NotificationSchedulerJob].
         */
        internal fun getInstance() = NotificationSchedulerJob()

    }

    /**
     * [SharedPrefsProvider] for accessing the preferences.
     */
    @Inject
    lateinit var sharedPrefsProvider: SharedPrefsProvider

    /**
     * [UserSessionManager] for getting the user session details.
     */
    @Inject
    lateinit var userSessionManager: UserSessionManager

    /**
     * [UserSettingsManager] for getting the user settings details.
     */
    @Inject
    lateinit var userSettingsManager: UserSettingsManager

    /**
     * When the job starts, programme will first check if the user is logged in or not?
     * </p>
     * If the user is logged in, it will display the notification and schedule the next job
     * after [CoreConfig.STAND_UP_REMINDER_INTERVAL].
     */
    override fun onRunJob(params: Params): Result {
        //Dagger injection
        DaggerCoreComponent.builder()
                .appComponent(BaseApplication.getApplicationComponent())
                .build()
                .inject(this@NotificationSchedulerJob)

        if (NotificationSchedulerHelper.shouldDisplayNotification(userSessionManager, userSettingsManager)) {

            if (NotificationSchedulerHelper.shouldDisplayPopUp(userSettingsManager, context)) {
                PopUpActivity.launch(context)
            } else {
                //Fire reminder notification
                ReminderNotification().notify(context)
            }
            Timber.i("Reminder notification fired. Check your status bar.")

            //Schedule the next notification
            scheduleNotification(sharedPrefsProvider)
        } else {
            Timber.i("Not right time to schedule notifications. Canceling the job.")
            cancelJob(sharedPrefsProvider)
        }

        return Result.SUCCESS
    }
}
