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

package com.kevalpatel2106.utils.timeUtilsTest

import com.kevalpatel2106.utils.TimeUtils
import org.junit.Assert
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.runners.Parameterized
import java.io.IOException

/**
 * Created by Kevalpatel2106 on 01-Mar-18.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
@RunWith(Enclosed::class)
class GetMilliSecFrom12AMHourMinsTest {

    @RunWith(Parameterized::class)
    class GetMilliSecFrom12AMParameterizeTest(private val hour: Int,
                                              private val mins: Int,
                                              private val expected: Long) {

        companion object {

            @JvmStatic
            @Parameterized.Parameters
            fun data(): ArrayList<Array<out Any?>> {
                return arrayListOf(
                        arrayOf(0, 0, 0),                                       // 00:00 hours
                        arrayOf(0, 1, 60_000),                                  // 00:01 hours
                        arrayOf(1, 1, 3600_000 + 60_000),                       // 01:01 hours
                        arrayOf(23, 0, 23 * 3600_000),                          // 23:00 hours
                        arrayOf(23, 59, 23 * 3600_000 + 59 * 60_000)            // 23:59 hours
                )
            }
        }

        @Test
        @Throws(IOException::class)
        fun testGetMilliSecFrom12AM() {
            Assert.assertEquals(expected, TimeUtils.getMilliSecFrom12AM(hour, mins))
        }
    }

    @RunWith(JUnit4::class)
    class GetMilliSecFrom12AMNonParameterizeTest {

        @Test
        @Throws(IOException::class)
        fun testGetMilliSecFrom12AM_NegativeHours() {
            try {
                TimeUtils.getMilliSecFrom12AM(-1, 0)
                Assert.fail()
            } catch (e: IllegalArgumentException) {
                //Test passed
            }
        }

        @Test
        @Throws(IOException::class)
        fun testGetMilliSecFrom12AM_24Hours() {
            try {
                TimeUtils.getMilliSecFrom12AM(24, 0)
                Assert.fail()
            } catch (e: IllegalArgumentException) {
                //Test passed
            }
        }

        @Test
        @Throws(IOException::class)
        fun testGetMilliSecFrom12AM_ZeroMins() {
            try {
                TimeUtils.getMilliSecFrom12AM(1, -1)
                Assert.fail()
            } catch (e: IllegalArgumentException) {
                //Test passed
            }
        }

        @Test
        @Throws(IOException::class)
        fun testGetMilliSecFrom12AM_60Mins() {
            try {
                TimeUtils.getMilliSecFrom12AM(1, 60)
                Assert.fail()
            } catch (e: IllegalArgumentException) {
                //Test passed
            }
        }
    }
}
