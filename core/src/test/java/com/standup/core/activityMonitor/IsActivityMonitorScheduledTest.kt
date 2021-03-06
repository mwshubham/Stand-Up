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

package com.standup.core.activityMonitor

import com.evernote.android.job.JobManager
import com.evernote.android.job.JobManagerRule
import com.evernote.android.job.JobRequest
import com.standup.core.CoreTestUtils
import com.standup.core.misc.CoreJobCreator
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


/**
 * Created by Keval on 21/01/18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class IsActivityMonitorScheduledTest {

    @Rule
    @JvmField
    var jobManagerRule: JobManagerRule = JobManagerRule(CoreJobCreator(),
            CoreTestUtils.createMockContext().applicationContext)

    @Test
    @Throws(Exception::class)
    fun checkIsAnyJobSchedulePositive() {
        jobManagerRule.jobManager.schedule(JobRequest.Builder(ActivityMonitorJob.ACTIVITY_MONITOR_JOB_TAG)
                .setPeriodic(1800_000L)
                .build())
        Assert.assertTrue(ActivityMonitorHelper.isAnyJobScheduled())
    }

    @Test
    @Throws(Exception::class)
    fun checkIsAnyJobScheduleNegative() {
        JobManager.create(CoreTestUtils.createMockContext())
        val jobManager = JobManager.instance()
        jobManager.cancelAll()
        Assert.assertFalse(ActivityMonitorHelper.isAnyJobScheduled())
    }
}
