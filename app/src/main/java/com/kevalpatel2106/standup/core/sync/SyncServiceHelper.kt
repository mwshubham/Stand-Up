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

package com.kevalpatel2106.standup.core.sync

import com.kevalpatel2106.base.annotations.Helper
import com.kevalpatel2106.standup.misc.UserSessionManager

/**
 * Created by Keval on 05/01/18.
 * Helper class for [SyncService].
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 * @see SyncService
 */
@Helper(SyncService::class)
object SyncServiceHelper {

    /**
     * Check if the [SyncService] should sync the data with the server?
     *
     * @param userSessionManager [UserSessionManager] for getting the current session information.
     * @return True if sync should happen. False if sync should not perform. (Bad timing for syncing.)
     * @see SyncService.onRunJobAsync
     */
    @JvmStatic
    internal fun shouldSync(userSessionManager: UserSessionManager): Boolean {
        return userSessionManager.isUserLoggedIn
    }
}
