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

package com.kevalpatel2106.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kevalpatel2106.network.retrofit.NWInterceptor
import com.kevalpatel2106.network.retrofit.NWResponseConverter
import com.kevalpatel2106.network.retrofit.NetworkConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
/**
 * Created by Keval Patel on 10/09/17.
 * This class deals with the API and network calls using the RxJava and retrofit.
 *
 * @author 'https://github.com/kevalpatel2106'
 */
class NetworkApi(private val userId: String? = null,
                 private val token: String? = null) {

    companion object {
        private var networkHook: NetworkHook? = null

        fun init(networkHook: NetworkHook) {
            this.networkHook = networkHook
        }
    }

    /**
     * OkHttp instance. New instances will be shallow copy of this instance.
     *
     * @see .getOkHttpClientBuilder
     */
    internal val okHttpClient: OkHttpClient

    /**
     * Gson instance with custom gson deserializers.
     */
    private val sGson: Gson = GsonBuilder()
            .setLenient()
            .create()

    internal fun getOkHttpClient(networkHook: NetworkHook?): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
                .readTimeout(NetworkConfig.READ_TIMEOUT, TimeUnit.MINUTES)
                .writeTimeout(NetworkConfig.WRITE_TIMEOUT, TimeUnit.MINUTES)
                .connectTimeout(NetworkConfig.CONNECTION_TIMEOUT, TimeUnit.MINUTES)

        //Add debug interceptors
        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(HttpLoggingInterceptor()
                    .apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
            )
        }

        return if (networkHook == null) {
            httpClientBuilder
                    .addInterceptor(NWInterceptor(null, userId, token))  /* Add the interceptor. */
                    .build()
        } else {
            httpClientBuilder
                    .cache(NWInterceptor.getCache(networkHook.getContext())) /* Add caching */
                    .addInterceptor(NWInterceptor(networkHook, userId, token))  /* Add the interceptor. */
                    .build()
        }
    }

    init {
        okHttpClient = getOkHttpClient(networkHook)
    }

    /**
     * Get the retrofit client instance for given base URL.
     *
     * @param baseUrl Base url of the api.
     */
    fun getRetrofitClient(baseUrl: String): Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(NWResponseConverter.create(sGson))
            .build()
}
