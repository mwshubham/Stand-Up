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

package com.standup.core.dndManager

import com.kevalpatel2106.common.prefs.UserSessionManager
import com.kevalpatel2106.common.prefs.UserSettingsManager
import com.kevalpatel2106.utils.TimeUtils
import com.kevalpatel2106.utils.annotations.Helper

/**
 * Created by Keval on 19/01/18.
 * Helper class for [AutoDndMonitoringJob].
 *
 * @see AutoDndMonitoringJob
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@Helper(AutoDndMonitoringJob::class)
internal object AutoDndMonitoringHelper {

    /**
     * Get the next DND mode start time in unix milliseconds. It will convert the
     * [UserSettingsManager.autoDndStartTime] which is in the milliseconds from the 12AM into the unix
     * time of the current day if the the time is not passed away or it will give start time for the
     * next day.
     *
     * @see UserSettingsManager.autoDndStartTime
     * @see TimeUtils.todayMidnightCal
     */
    @JvmStatic
    internal fun getAutoDndStartTiming(userSettingsManager: UserSettingsManager): Long {
        return with(TimeUtils.todayMidnightMills() + userSettingsManager.autoDndStartTime) {

            if (this < System.currentTimeMillis()) {    //Auto dnd start time is already passed.

                //Schedule the job tomorrow.
                return@with this + TimeUtils.ONE_DAY_MILLISECONDS
            } else {    //Auto dnd start time is yet to pass.
                //Schedule the job today itself.
                return@with this
            }
        }
    }

    /**
     * Get the next DND mode end time in unix milliseconds. It will convert the
     * [UserSettingsManager.autoDndEndTime] which is in the milliseconds from the 12AM into the unix
     * time of the current day if the the time is not passed away or it will give start time for the
     * next day.
     *
     * @see UserSettingsManager.autoDndEndTime
     * @see TimeUtils.todayMidnightCal
     */
    @JvmStatic
    internal fun getAutoDndEndTiming(userSettingsManager: UserSettingsManager): Long {
        return with(TimeUtils.todayMidnightMills() + userSettingsManager.autoDndEndTime) {

            if (this < System.currentTimeMillis()) {    //Auto dnd start time is already passed.

                //Schedule the job tomorrow.
                return@with this + TimeUtils.ONE_DAY_MILLISECONDS
            } else {    //Auto dnd start time is yet to pass.
                //Schedule the job today itself.
                return@with this
            }
        }
    }

    /**
     * Is it okay to run/schedule the [AutoDndMonitoringJob]?
     *
     * [AutoDndMonitoringJob] should only be running if auto dnd is turned on (i.e.
     * [UserSettingsManager.isAutoDndEnable] is true) and user is logged in (i.e.
     * [UserSessionManager.isUserLoggedIn] is true.).
     *
     * @return True if application is good to schedule/run the [AutoDndMonitoringJob] or else false.
     */
    internal fun shouldRunningThisJob(userSettingsManager: UserSettingsManager,
                                      userSessionManager: UserSessionManager): Boolean {
        return userSessionManager.isUserLoggedIn && userSettingsManager.isAutoDndEnable
    }
}
