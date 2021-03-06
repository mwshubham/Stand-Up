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

package com.kevalpatel2106.common.prefs

import com.kevalpatel2106.utils.SharedPrefsProvider
import com.kevalpatel2106.utils.TimeUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

/**
 * Created by Kevalpatel2106 on 06-Mar-18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@RunWith(JUnit4::class)
class IsCurrentlyInDndModeTest {

    @Test
    fun checkIsCurrentlyInAutoDndMode_WhenAutoDndDisable() {
        val sharedPrefsProvider = Mockito.mock(SharedPrefsProvider::class.java)
        Mockito.`when`(sharedPrefsProvider.getBoolFromPreferences(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyBoolean())).thenReturn(false)

        Assert.assertFalse(UserSettingsManager(sharedPrefsProvider).isCurrentlyInAutoDndMode())
    }

    @Test
    fun checkIsCurrentlyInAutoDndMode_WhenCurrentTimeBeforeDndStartTime() {
        val sharedPrefsProvider = Mockito.mock(SharedPrefsProvider::class.java)
        //Make auto dnd enable
        Mockito.`when`(sharedPrefsProvider.getBoolFromPreferences(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyBoolean())).thenReturn(true)

        //Half an hour before current time
        Mockito.`when`(sharedPrefsProvider.getLongFromPreference(ArgumentMatchers.anyString(), ArgumentMatchers.anyLong()))
                .thenReturn(TimeUtils.currentMillsFromMidnight() - 1800_000L)

        Assert.assertFalse(UserSettingsManager(sharedPrefsProvider).isCurrentlyInAutoDndMode())
    }

    @Test
    fun checkIsCurrentlyInAutoDndMode_WhenCurrentTimeAfterDndEndTime() {
        val sharedPrefsProvider = Mockito.mock(SharedPrefsProvider::class.java)
        //Make auto dnd enable
        Mockito.`when`(sharedPrefsProvider.getBoolFromPreferences(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyBoolean())).thenReturn(true)

        //Half an hour before current time
        Mockito.`when`(sharedPrefsProvider.getLongFromPreference(ArgumentMatchers
                .startsWith(SharedPreferenceKeys.PREF_KEY_AUTO_DND_START_TIME_FROM_12AM), ArgumentMatchers.anyLong()))
                .thenReturn(TimeUtils.currentMillsFromMidnight() - 1800_000L)

        //15 mins before current time
        Mockito.`when`(sharedPrefsProvider.getLongFromPreference(ArgumentMatchers
                .startsWith(SharedPreferenceKeys.PREF_KEY_AUTO_DND_END_TIME_FROM_12AM), ArgumentMatchers.anyLong()))
                .thenReturn(TimeUtils.currentMillsFromMidnight() - 900_000L)

        Assert.assertFalse(UserSettingsManager(sharedPrefsProvider).isCurrentlyInAutoDndMode())
    }

    @Test
    fun checkIsCurrentlyInAutoDndMode_Positive() {
        val sharedPrefsProvider = Mockito.mock(SharedPrefsProvider::class.java)
        //Make auto dnd enable
        Mockito.`when`(sharedPrefsProvider.getBoolFromPreferences(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyBoolean())).thenReturn(true)

        //15 mins before current time
        Mockito.`when`(sharedPrefsProvider.getLongFromPreference(ArgumentMatchers
                .startsWith(SharedPreferenceKeys.PREF_KEY_AUTO_DND_START_TIME_FROM_12AM), ArgumentMatchers.anyLong()))
                .thenReturn(TimeUtils.currentMillsFromMidnight() - 900_000L)

        //15 mins after current time
        Mockito.`when`(sharedPrefsProvider.getLongFromPreference(ArgumentMatchers
                .startsWith(SharedPreferenceKeys.PREF_KEY_AUTO_DND_END_TIME_FROM_12AM), ArgumentMatchers.anyLong()))
                .thenReturn(TimeUtils.currentMillsFromMidnight() + 900_000L)

        Assert.assertTrue(UserSettingsManager(sharedPrefsProvider).isCurrentlyInAutoDndMode())
    }

    @Test
    fun checkIsCurrentlyInAutoDndMode_WhenCurrentTimeEqualsDndStartTime() {
        val sharedPrefsProvider = Mockito.mock(SharedPrefsProvider::class.java)
        //Make auto dnd enable
        Mockito.`when`(sharedPrefsProvider.getBoolFromPreferences(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyBoolean())).thenReturn(true)

        val currentTime = TimeUtils.currentMillsFromMidnight()

        //Set the start time to the current time
        Mockito.`when`(sharedPrefsProvider.getLongFromPreference(ArgumentMatchers.startsWith(SharedPreferenceKeys
                .PREF_KEY_AUTO_DND_START_TIME_FROM_12AM), ArgumentMatchers.anyLong()))
                .thenReturn(currentTime)

        //Set the end time.
        Mockito.`when`(sharedPrefsProvider.getLongFromPreference(ArgumentMatchers.startsWith(SharedPreferenceKeys
                .PREF_KEY_AUTO_DND_END_TIME_FROM_12AM), ArgumentMatchers.anyLong()))
                .thenReturn(currentTime + 900_000L)

        Assert.assertTrue(UserSettingsManager(sharedPrefsProvider).isCurrentlyInAutoDndMode(currentTime))
    }

    @Test
    fun checkIsCurrentlyInAutoDndMode_WhenCurrentTimeEqualsDndEndTime() {
        val sharedPrefsProvider = Mockito.mock(SharedPrefsProvider::class.java)
        //Make auto dnd enable
        Mockito.`when`(sharedPrefsProvider.getBoolFromPreferences(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyBoolean())).thenReturn(true)

        val currentTime = TimeUtils.currentMillsFromMidnight()

        //Set the start time
        Mockito.`when`(sharedPrefsProvider.getLongFromPreference(ArgumentMatchers.startsWith(SharedPreferenceKeys
                .PREF_KEY_AUTO_DND_START_TIME_FROM_12AM), ArgumentMatchers.anyLong()))
                .thenReturn(currentTime - 900_000L)

        //Set the end time to current time
        Mockito.`when`(sharedPrefsProvider.getLongFromPreference(ArgumentMatchers.startsWith(SharedPreferenceKeys
                .PREF_KEY_AUTO_DND_END_TIME_FROM_12AM), ArgumentMatchers.anyLong()))
                .thenReturn(currentTime)

        Assert.assertTrue(UserSettingsManager(sharedPrefsProvider)
                .isCurrentlyInAutoDndMode(currentTime))
    }
}
