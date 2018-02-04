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

package com.standup.core.di

import com.kevalpatel2106.common.ApplicationScope
import com.kevalpatel2106.common.application.di.AppModule
import com.kevalpatel2106.common.db.DbModule
import com.kevalpatel2106.common.db.userActivity.UserActivityDao
import com.standup.core.repo.CoreRepo
import com.standup.core.repo.CoreRepoImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

/**
 * Created by Keval on 10/01/18.
 * Dagger [Module] to provide the dependency for the Core module.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
@Module(includes = [DbModule::class])
internal class CoreModule {

    /**
     * Get the instance of [CoreRepo].
     *
     * @see CoreRepoImpl
     */
    @Provides
    @ApplicationScope
    fun provideReminderRepo(userActivityDao: UserActivityDao,
                            @Named(AppModule.WITH_TOKEN) retrofit: Retrofit): CoreRepo = CoreRepoImpl(userActivityDao, retrofit)
}