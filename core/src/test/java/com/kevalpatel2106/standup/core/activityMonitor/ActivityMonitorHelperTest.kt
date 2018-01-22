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

package com.kevalpatel2106.standup.core.activityMonitor

import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito

/**
 * Created by Keval on 21/01/18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class ActivityMonitorHelperTest {


    @Test
    @Throws(Exception::class)
    fun checkIsAnyJobSchedulePositive() {
        val jobManager = Mockito.mock(JobManager::class.java)
        Mockito.`when`(JobManager.instance()).thenReturn(jobManager)

        val set = HashSet<JobRequest>()
        set.add(Mockito.mock(JobRequest::class.java))

        Mockito.`when`(jobManager.getAllJobRequestsForTag(anyString())).thenReturn(set)

        Assert.assertTrue(ActivityMonitorHelper.isAnyJobScheduled())
    }

    @Test
    @Throws(Exception::class)
    fun checkIsAnyJobScheduleNegative() {
        val jobManager = Mockito.mock(JobManager::class.java)
        Mockito.`when`(JobManager.instance()).thenReturn(jobManager)

        val set = HashSet<JobRequest>()
        Mockito.`when`(jobManager.getAllJobRequestsForTag(anyString())).thenReturn(set)

        Assert.assertFalse(ActivityMonitorHelper.isAnyJobScheduled())
    }
}