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

package com.kevalpatel2106.standup.misc

import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import com.kevalpatel2106.standup.constants.SharedPreferenceKeys
import com.kevalpatel2106.utils.SharedPrefsProvider

/**
 * Created by Keval on 14/01/18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */

class UserSettingsManager(private val sharedPrefProvider: SharedPrefsProvider) {

    private val DEFAULT_LED_COLOR = "red"
    private val DEFAULT_RINGTONE_NAME = "Default"
    private val DEFAULT_ENABLE_BACKGROUND_SYNC = true
    private val DEFAULT_SHOULD_VIBRATE = true
    private val DEFAULT_SIELENT_MODE = "do_not_play"
    private val DEFAULT_SHOULD_DISPLAY_POPUP = false
    private val DEFAULT_SHOULD_DISPLAY_UPDATE_NOTIFICATION = true
    private val DEFAULT_SHOULD_DISPLAY_PROMOTIONAL_NOTIFICATION = true

    val enableBackgroundSync: Boolean
        get() = sharedPrefProvider.getBoolFromPreferences(SharedPreferenceKeys.PREF_KEY_ALLOW_BACKGROUND_SYNC,
                DEFAULT_ENABLE_BACKGROUND_SYNC)

    val getReminderToneName: String?
        get() = sharedPrefProvider.getStringFromPreferences(SharedPreferenceKeys.PREF_KEY_RINGTONE_NAME,
                DEFAULT_RINGTONE_NAME)

    val getReminderToneUri: Uri
        get() {
            val uriString = sharedPrefProvider.getStringFromPreferences(SharedPreferenceKeys.PREF_KEY_RINGTONE_URI,
                    null)

            return if (uriString == null) {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            } else {
                Uri.parse(uriString)
            }
        }

    val shouldVibrate: Boolean
        get() = sharedPrefProvider.getBoolFromPreferences(SharedPreferenceKeys.PREF_KEY_REMINDER_VIBRATE,
                DEFAULT_SHOULD_VIBRATE)

    val ledColorValue: String
        get() = sharedPrefProvider.getStringFromPreferences(SharedPreferenceKeys.PREF_KEY_REMINDER_LED_COLOR,
                DEFAULT_LED_COLOR)!!

    val silentModeRawValue: String
        get() = sharedPrefProvider.getStringFromPreferences(SharedPreferenceKeys.PREF_KEY_SILENT_MODE,
                DEFAULT_SIELENT_MODE)!!

    val shouldPlayReminderSoundInSilent: Boolean
        get() = sharedPrefProvider.getStringFromPreferences(SharedPreferenceKeys.PREF_KEY_SILENT_MODE,
                DEFAULT_SIELENT_MODE)!! == "do_not_play"

    val shouldPlayReminderSoundWithHeadphones: Boolean
        get() = sharedPrefProvider.getStringFromPreferences(SharedPreferenceKeys.PREF_KEY_SILENT_MODE,
                DEFAULT_SIELENT_MODE)!! == "only_with_headphone"

    val shouldPlayReminderSoundAlways: Boolean
        get() = sharedPrefProvider.getStringFromPreferences(SharedPreferenceKeys.PREF_KEY_SILENT_MODE,
                DEFAULT_SIELENT_MODE)!! == "always_play"

    val shouldDisplayPopUp: Boolean
        get() = sharedPrefProvider.getBoolFromPreferences(SharedPreferenceKeys.PREF_KEY_IS_TO_DISPLAY_POPUP,
                DEFAULT_SHOULD_DISPLAY_POPUP)

    val shouldDisplayUpdateNotification: Boolean
        get() = sharedPrefProvider.getBoolFromPreferences(SharedPreferenceKeys.PREF_KEY_IS_TO_SHOW_UPDATE_NOTIFICATION,
                DEFAULT_SHOULD_DISPLAY_UPDATE_NOTIFICATION)

    val shouldDisplayPromotionalNotification: Boolean
        get() = sharedPrefProvider.getBoolFromPreferences(SharedPreferenceKeys.PREF_KEY_IS_TO_SHOW_PROMOTIONAL_NOTIFICATION,
                DEFAULT_SHOULD_DISPLAY_PROMOTIONAL_NOTIFICATION)


    val ledColor: Int
        get() = when (ledColorValue) {
            "none" -> Color.TRANSPARENT
            "white" -> Color.WHITE
            "red" -> Color.RED
            "yellow" -> Color.YELLOW
            "green" -> Color.GREEN
            "cyan" -> Color.CYAN
            "blue" -> Color.BLUE
            else -> Color.TRANSPARENT
        }


    fun setReminderTone(name: String, uri: Uri) {
        sharedPrefProvider.savePreferences(SharedPreferenceKeys.PREF_KEY_RINGTONE_NAME, name)
        sharedPrefProvider.savePreferences(SharedPreferenceKeys.PREF_KEY_RINGTONE_URI, uri.toString())
    }
}
