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

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import com.kevalpatel2106.common.ReminderMessageProvider
import com.kevalpatel2106.common.notifications.NotificationChannelType
import com.kevalpatel2106.common.prefs.UserSettingsManager
import com.kevalpatel2106.testutils.MockSharedPreference
import com.kevalpatel2106.utils.SharedPrefsProvider
import com.standup.core.R
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import java.io.IOException


/**
 * Created by Kevalpatel2106 on 25-Dec-17.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class ReminderNotificationTest {
    private val TEST_TITLE_STRING = "This is test notification title."
    private lateinit var notification: NotificationCompat.Builder

    @Before
    fun setUp() {
        val context = Mockito.mock(Context::class.java)
        val resources = Mockito.mock(Resources::class.java)
        Mockito.`when`(context.getString(R.string.reminder_notification_title)).thenReturn(TEST_TITLE_STRING)
        Mockito.`when`(context.getColor(anyInt())).thenReturn(Color.RED)
        Mockito.`when`(context.resources).thenReturn(resources)
        Mockito.`when`(context.resources.getColor(anyInt())).thenReturn(Color.RED)

        val userSettingsManager = UserSettingsManager(SharedPrefsProvider(MockSharedPreference()))

        notification = ReminderNotification(userSettingsManager, ReminderMessageProvider())
                .buildNotification(context, 0)
    }

    @Test
    @Throws(IOException::class)
    fun testNotificationPriority() {
        assertEquals(notification.priority, NotificationCompat.PRIORITY_HIGH)
    }

    @Test
    @Throws(IOException::class)
    fun testNotificationColor() {
        assertEquals(notification.color, Color.RED)
    }

    @Test
    @Throws(IOException::class)
    fun testNotificationChannel() {
        //Channel id
        val channelId = notification.javaClass.getDeclaredField("mChannelId")
        channelId.isAccessible = true
        Assert.assertEquals(channelId.get(notification) as String, NotificationChannelType.REMINDER_NOTIFICATION_CHANNEL)
    }

    @Test
    @Throws(IOException::class)
    fun testNotificationTitle() {
        //notification title
        val title = notification.javaClass.getDeclaredField("mContentTitle")
        title.isAccessible = true
        Assert.assertEquals(title.get(notification) as String, TEST_TITLE_STRING)
    }

    @Test
    @Throws(IOException::class)
    fun testNotificationMessage() {
        //notification message
        val message = notification.javaClass.getDeclaredField("mContentText")
        message.isAccessible = true
        Assert.assertEquals(message.get(notification) as String, "It's time to stretch your legs. Go ahead and take a walk.")
    }

    @Test
    @Throws(IOException::class)
    fun testNotificationStyle() {
        //notification style
        val style = notification.javaClass.getDeclaredField("mStyle")
        style.isAccessible = true
        Assert.assertTrue(style.get(notification) is NotificationCompat.BigTextStyle)
    }
}
