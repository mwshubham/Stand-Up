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

package com.kevalpatel2106.standup.constants

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.kevalpatel2106.utils.UserSessionManager
import com.kevalpatel2106.utils.Utils

/**
 * Created by Keval on 08/12/17.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
internal object AnalyticsEvents {

    //Analytics events
    const val EVENT_LOGOUT = "logout"
    const val EVENT_DEVICE_REGISTERED = "device_registered"
    const val EVENT_RESEND_VERIFICATION_MAIL = "resend_verification_mail"
    const val EVENT_FORGOT_PASSWORD = "forgot_password_req"
    const val EVENT_PROFILE_UPDATED = "profile_updated"
    const val EVENT_OPEN_MAIL_BUTTON_FEATURE = "open_mail_button_feature"
    const val EVENT_UNAUTHORIZED_FORCE_LOGOUT = "unauthorized_force_logout"

    //About page actions
    const val EVENT_APP_FORK_ON_GITHUB = "app_fork_on_github"
    const val EVENT_JOIN_SLACK_CHANNEL = "join_slack_channel"
    const val EVENT_OPEN_GITHUB_PAGE = "open_github_page"
    const val EVENT_CHECK_UPDATE_MANUALLY = "check_update_manually"
    const val EVENT_RATE_APP_ON_PLAY_STORE = "rate_app_on_play_store"
    const val EVENT_APP_INVITE_SUCCESS = "app_invite_success"
    const val EVENT_APP_INVITE_CANCEL = "app_invite_cancel"


    //Bundle keys
    internal const val KEY_USER_ID = "user_id"
    internal const val KEY_DEVICE_ID = "device_id"
    internal const val KEY_EMAIL = "email"
}

fun Context.logEvent(event: String,
                     bundle: Bundle? = null) {

    val param = bundle ?: Bundle()

    if (UserSessionManager.isUserLoggedIn) {
        param.putLong(AnalyticsEvents.KEY_USER_ID, UserSessionManager.userId)
        param.putString(AnalyticsEvents.KEY_DEVICE_ID, Utils.getDeviceId(applicationContext))
    }

    FirebaseAnalytics.getInstance(applicationContext).logEvent(event, param)
}
