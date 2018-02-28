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

package com.standup.app.fcm

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.kevalpatel2106.common.prefs.UserSessionManager
import com.kevalpatel2106.common.prefs.UserSettingsManager
import com.kevalpatel2106.testutils.MockSharedPreference
import com.kevalpatel2106.utils.SharedPrefsProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException

/**
 * Created by Kevalpatel2106 on 25-Dec-17.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class FcmMessagingServiceHelperTest {

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var fcmMessagingServiceHelper: FcmMessagingServiceHelper

    private lateinit var userSessionManager: UserSessionManager
    private lateinit var userSettingsManager: UserSettingsManager

    @Before
    fun setUp() {
        val mockSharedPreference = MockSharedPreference()

        userSessionManager = UserSessionManager(SharedPrefsProvider(mockSharedPreference))
        userSessionManager.setNewSession(
                1234567,
                "Test",
                "test@gmail.com",
                "test_token",
                null,
                true
        )
        userSettingsManager = UserSettingsManager(SharedPrefsProvider(mockSharedPreference))

        fcmMessagingServiceHelper = FcmMessagingServiceHelper(
                userSessionManager,
                userSettingsManager
        )
    }

    @Test
    @Throws(IOException::class)
    fun checkShouldProcessNotification_WithNullRemoteData() {
        assertFalse(fcmMessagingServiceHelper.shouldProcessNotification(null))
    }

    @Test
    @Throws(IOException::class)
    fun checkShouldProcessNotification_WithEmptyRemoteData() {
        assertFalse(fcmMessagingServiceHelper.shouldProcessNotification(HashMap()))
    }

    @Test
    @Throws(IOException::class)
    fun checkShouldProcessNotification_UserNotLoggedIn() {
        userSessionManager.token = null
        assertFalse(fcmMessagingServiceHelper.shouldProcessNotification(HashMap()))
    }

    @Test
    @Throws(IOException::class)
    fun checkShouldProcessNotification_WithoutType() {
        val hashMap = HashMap<String, String>()
        hashMap["xyz"] = "abc"
        assertFalse(fcmMessagingServiceHelper.shouldProcessNotification(hashMap))
    }

    @Test
    @Throws(IOException::class)
    fun checkShouldProcessNotification_AllGood() {
        val data = HashMap<String, String>()
        data["type"] = "xyz"
        assertTrue(fcmMessagingServiceHelper.shouldProcessNotification(data))
    }

    @Test
    @Throws(IOException::class)
    fun checkHandleEmailVerifiedType_WithMessage() {
        val testMessage = "Test notification message."

        val data = HashMap<String, String>()
        data["type"] = NotificationType.TYPE_EMAIL_VERIFIED
        data["message"] = testMessage

        assertNull(fcmMessagingServiceHelper.fireAuthenticationNotification.value)

        fcmMessagingServiceHelper.handleMessage(data)

        assertTrue(userSessionManager.isUserVerified)
        assertEquals(fcmMessagingServiceHelper.fireAuthenticationNotification.value, testMessage)
    }

    @Test
    @Throws(IOException::class)
    fun checkHandleEmailVerifiedType_WithoutMessage() {
        val data = HashMap<String, String>()
        data["type"] = NotificationType.TYPE_EMAIL_VERIFIED

        assertNull(fcmMessagingServiceHelper.fireAuthenticationNotification.value)

        fcmMessagingServiceHelper.handleMessage(data)

        assertTrue(userSessionManager.isUserVerified)
        assertNull(fcmMessagingServiceHelper.fireAuthenticationNotification.value)
    }

    @Test
    @Throws(IOException::class)
    fun checkHandlePromotionalType_WithMessage() {
        val testTitle = "Test notification title."
        val testMessage = "Test notification message."

        val data = HashMap<String, String>()
        data["type"] = NotificationType.TYPE_PROMOTIONAL
        data["title"] = testTitle
        data["message"] = testMessage

        assertNull(fcmMessagingServiceHelper.firePromotionalNotification.value)

        fcmMessagingServiceHelper.handleMessage(data)

        assertEquals(fcmMessagingServiceHelper.firePromotionalNotification.value!![0], testTitle)
        assertEquals(fcmMessagingServiceHelper.firePromotionalNotification.value!![1], testMessage)
    }

    @Test
    @Throws(IOException::class)
    fun checkHandlePromotionalType_WithoutTitleMessage() {
        val data = HashMap<String, String>()
        data["type"] = NotificationType.TYPE_PROMOTIONAL

        assertNull(fcmMessagingServiceHelper.firePromotionalNotification.value)

        fcmMessagingServiceHelper.handleMessage(data)

        assertTrue(userSessionManager.isUserVerified)
        assertNull(fcmMessagingServiceHelper.firePromotionalNotification.value)
    }
}
