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

/**
 * Created by Keval on 17/11/17.
 * Object class to hold the keys used for shared preferences.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */

internal object SharedPreferenceKeys {

    /**
     * This key holds to boolean to indicate if fcm token is synced or not?
     */
    const val IS_DEVICE_REGISTERED: String = "is_device_registered"


    /**
     * This key holds to boolean to indicate if navigation drawer has been displayed to the user?
     */
    const val IS_NAVIGATION_DRAWER_DISPLAYED: String = "is_navigation_drawer_displayed"
}
