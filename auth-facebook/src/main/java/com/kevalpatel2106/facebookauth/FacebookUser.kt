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

package com.kevalpatel2106.facebookauth

/**
 * Created by multidots on 6/16/2016.
 * This class represents facebook user profile.
 *
 * @author [&#39;https://github.com/kevalpatel2106&#39;]['https://github.com/kevalpatel2106']
 */
data class FacebookUser(val facebookID: String) {

    var name: String? = null
    var email: String? = null
    var gender: String? = null
    var about: String? = null
    var bio: String? = null
    var coverPicUrl: String? = null
    var profilePic: String? = null

    override fun hashCode(): Int = facebookID.hashCode()

    override fun equals(other: Any?): Boolean {
        other?.let {
            return (other is FacebookUser && other.facebookID == facebookID)
        }
        return false
    }
}
