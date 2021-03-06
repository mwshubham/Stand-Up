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

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Keval on 15/12/17.
 * List of the end points for communicating with the server for "Core" module.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 */
internal interface CoreApiService {

    @POST("/saveActivity")
    @Headers("Add-Auth: true")
    fun saveActivity(@Body saveActivityRequest: SaveActivityRequest): Call<SaveActivityResponse>


    @POST("/getActivity")
    @Headers("Add-Auth: true")
    fun getActivities(@Body getActivityRequest: GetActivityRequest): Call<GetActivityResponse>
}
