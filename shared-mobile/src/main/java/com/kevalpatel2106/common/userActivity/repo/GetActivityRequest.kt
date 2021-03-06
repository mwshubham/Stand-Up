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

package com.kevalpatel2106.common.userActivity.repo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.kevalpatel2106.utils.TimeUtils

/**
 * Created by Keval on 30/01/18.
 *
 * @author [kevalpatel2106](https://github.com/kevalpatel2106)
 */
@Suppress("MemberVisibilityCanBePrivate")
internal data class GetActivityRequest(

        @SerializedName("oldestTime")
        @Expose
        val oldestTimeStampNano: Long
) {

    init {
        if (oldestTimeStampNano < System.currentTimeMillis()) { //TODO Think how to detect milli seconds
            throw IllegalArgumentException("Oldest time must be in nano seconds")
        }

        if (oldestTimeStampNano > TimeUtils.convertToNano(System.currentTimeMillis())) {
            throw IllegalArgumentException("Oldest time cannot more than current time.")
        }
    }
}
