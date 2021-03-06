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

package com.kevalpatel2106.common.view

import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.kevalpatel2106.testutils.BaseTestClass
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Keval on 20-Jul-17.
 */
@RunWith(AndroidJUnit4::class)
class BaseViewPagerTest : BaseTestClass() {

    @Test
    @Throws(Exception::class)
    fun setSwipeGestureEnable() {
        val baseViewPager = BaseViewPager(InstrumentationRegistry.getTargetContext())
        baseViewPager.setSwipeGestureEnable(false)
        assertTrue(!baseViewPager.onInterceptTouchEvent(null))
        assertTrue(!baseViewPager.onTouchEvent(null))
    }

    override fun getActivity(): Activity? {
        return null
    }
}
