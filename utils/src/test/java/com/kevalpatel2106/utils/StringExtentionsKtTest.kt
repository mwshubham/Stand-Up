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

package com.kevalpatel2106.utils

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Created by Keval on 04/01/18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class StringExtensionsTest {

    @Test
    fun checkSafeFloatWithEmpty() {
        Assert.assertEquals(0F, "".toFloatSafe())
    }

    @Test
    fun checkSafeFloatWithString() {
        Assert.assertEquals(100F, "100".toFloatSafe())
    }

    @Test
    fun checkSafeIntWithEmpty() {
        Assert.assertEquals(0, "".toIntSafe())
    }

    @Test
    fun checkSafeIntWithString() {
        Assert.assertEquals(100, "100".toIntSafe())
    }
}