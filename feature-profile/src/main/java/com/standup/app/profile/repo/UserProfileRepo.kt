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

import com.kevalpatel2106.utils.annotations.Repository
import io.reactivex.Flowable

/**
 * Created by Keval on 28/11/17.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
@Repository
internal interface UserProfileRepo {

    fun getUserProfile(userId: Long): Flowable<GetProfileResponse>

    fun saveUserProfile(name: String,
                        photo: String?,
                        height: Float,
                        weight: Float,
                        isMale: Boolean): Flowable<SaveProfileResponse>
}
