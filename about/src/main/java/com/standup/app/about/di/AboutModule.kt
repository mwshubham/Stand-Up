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

package com.standup.app.about.di

import com.kevalpatel2106.common.application.di.AppModule
import com.kevalpatel2106.common.application.di.ApplicationScope
import com.kevalpatel2106.common.prefs.UserSessionManager
import com.standup.app.about.repo.AboutRepository
import com.standup.app.about.repo.AboutRepositoryImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

/**
 * Created by Kevalpatel2106 on 09-Jan-18.
 * A dagger [Module] for about package.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
@Module
internal class AboutModule {

    /**
     * Get the instance of [com.kevalpatel2106.standup.authentication.repo.UserAuthRepository].
     *
     * @see Provides
     */
    @Provides
    @ApplicationScope
    fun provideUserAuthRepo(@Named(AppModule.WITH_TOKEN) retrofit: Retrofit,
                            userSessionManager: UserSessionManager): AboutRepository = AboutRepositoryImpl(retrofit, userSessionManager)
}
