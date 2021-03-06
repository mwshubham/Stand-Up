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

package com.standup.app.profile.repo

import com.google.gson.annotations.SerializedName
import com.kevalpatel2106.common.misc.AppConfig
import com.kevalpatel2106.utils.toFloatSafe

/**
 * Created by Keval on 28/11/17.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
internal data class GetProfileResponse(

        @SerializedName("uid")
        val userId: Long,

        @SerializedName("name")
        val name: String,

        @SerializedName("email")
        val email: String,

        @SerializedName("photo")
        val photo: String?,

        @SerializedName("height")
        var height: String,

        @SerializedName("weight")
        var weight: String,

        @SerializedName("gender")
        var gender: String = AppConfig.GENDER_MALE,

        @SerializedName("isVerified")
        val isVerified: Boolean
) {

    fun heightFloat() = height.toFloatSafe()

    fun weightFloat() = weight.toFloatSafe()
}
