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

package com.standup.app.profile.di

import com.kevalpatel2106.common.di.AppModule
import com.kevalpatel2106.common.prefs.UserSessionManager
import com.kevalpatel2106.utils.annotations.ApplicationScope
import com.standup.app.profile.ProfileApi
import com.standup.app.profile.ProfileHook
import com.standup.app.profile.repo.UserProfileRepo
import com.standup.app.profile.repo.UserProfileRepoImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

/**
 * Created by Keval on 10/01/18.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
@Module
internal class ProfileDaggerModule {

    @Provides
    @ApplicationScope
    fun provideUserProfileRepo(@Named(AppModule.WITH_TOKEN) retrofit: Retrofit,
                               userSessionManager: UserSessionManager): UserProfileRepo = UserProfileRepoImpl(retrofit, userSessionManager)

    @Provides
    @ApplicationScope
    fun provideProfileHook(): ProfileHook = ProfileApi.profileHook!!
}
