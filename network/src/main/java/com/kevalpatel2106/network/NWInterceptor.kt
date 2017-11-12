/*
 *  Copyright 2017 Keval Patel.
 *
 *  Licensed under the GNU General Public License, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  https://www.gnu.org/licenses/gpl-3.0.en.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.kevalpatel2106.network

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v4.content.LocalBroadcastManager
import android.util.Base64
import okhttp3.*
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection

/**
 * Created by Keval on 16-Jun-17.
 *
 * @author [&#39;https://github.com/kevalpatel2106&#39;]['https://github.com/kevalpatel2106']
 */
internal class NWInterceptor(private val mContext: Context,
                             cacheTimeMins: Long,
                             private val username: String?,
                             private val token: String?) : Interceptor {
    private val mCacheTimeMills: Long = cacheTimeMins * 60000

    companion object {
        internal val CACHE_SIZE = 5242880L          //5 MB //Cache size.

        /**
         * Initialize the cache directory.
         *
         * @param context Instance of caller.
         * @return [Cache].
         */
        @JvmStatic
        fun getCache(context: Context): Cache {
            //Define mCache
            val httpCacheDirectory = File(context.cacheDir, "responses")
            return Cache(httpCacheDirectory, CACHE_SIZE.toLong())
        }
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()

        //Set the authorization header
        request = addAuthorisationHeader(request, username, token)

        var response = chain.proceed(request)

        //Enable caching if you need.
        response = addCachingHeaders(response, mCacheTimeMills)

        //Prepossess the response to find out errors based on the server response/status code.
        return preprocessedResponse(response)
    }

    private fun preprocessedResponse(response: Response): Response {
        return if (response.code() == HttpURLConnection.HTTP_OK) {   //HTTP OK

            if (response.header("Content-type").equals("application/json")) {   //Check if the json resposne.
                //Check for the error code other than the success.
                val responseStr = response.body()?.string()
                val status = JSONObject(responseStr).getJSONObject("s")

                val code = status.getInt("c")
                when {
                    code == APIStatusCodes.SUCCESS_CODE -> {
                        //Success
                        //Nothing to do. Go ahead.
                        response.newBuilder()
                                .body(ResponseBody.create(MediaType.parse("application/json"), responseStr))
                                .build()
                    }
                    code == APIStatusCodes.ERROR_CODE_UNAUTHORIZED -> {     //You are unauthorized.

                        //Broadcast to the app module that user is not authorized.
                        //Log out.
                        LocalBroadcastManager.getInstance(mContext)
                                .sendBroadcast(Intent(APIStatusCodes.BROADCAST_UNAUTHORIZED))
                        response
                    }
                    code < APIStatusCodes.SUCCESS_CODE -> {       //Exception occurred on the server
                        throw NWException(code, APIStatusCodes.ERROR_MESSAGE_SOMETHING_WRONG)
                    }
                    else -> {   //Some recoverable error occurred on the server
                        val message = status.getString("m")
                        throw NWException(code, message)
                    }
                }
            } else {
                //String response. Cannot do anything.
                response
            }
        } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED
                || response.code() == HttpURLConnection.HTTP_FORBIDDEN) {  //Unauthorized or forbidden

            //Broadcast to the app module that user is not authorized.
            //Log out.
            LocalBroadcastManager.getInstance(mContext)
                    .sendBroadcast(Intent(APIStatusCodes.BROADCAST_UNAUTHORIZED))

            throw NWException(response.code(), APIStatusCodes.ERROR_MESSAGE_SOMETHING_WRONG)
        } else if (response.code() == HttpURLConnection.HTTP_BAD_REQUEST
                || response.code() == HttpURLConnection.HTTP_BAD_METHOD) {  //Bad request

            throw NWException(response.code(), APIStatusCodes.ERROR_MESSAGE_BAD_REQUEST)
        } else if (response.code() == HttpURLConnection.HTTP_NOT_FOUND
                || response.code() == HttpURLConnection.HTTP_NOT_IMPLEMENTED) {  //404. Not found

            throw NWException(response.code(), APIStatusCodes.ERROR_MESSAGE_NOT_FOUND)
        } else if (response.code() == HttpURLConnection.HTTP_SERVER_ERROR
                || response.code() == HttpURLConnection.HTTP_UNAVAILABLE) {  //500. Server is busy.

            throw NWException(response.code(), APIStatusCodes.ERROR_MESSAGE_SERVER_BUSY)
        } else {
            //No specific error
            throw NWException(response.code(), APIStatusCodes.ERROR_MESSAGE_SOMETHING_WRONG)
        }
    }

    private fun addCachingHeaders(response: Response, cacheTimeMills: Long): Response {
        var response1 = response
        if (mCacheTimeMills > 0) {
            response1 = if (isNetworkAvailable(mContext)) {
                response1.newBuilder()
                        .header("Cache-Control", "public, max-age=" + cacheTimeMills)
                        .build()
            } else {
                val maxStale = 2419200 // tolerate 4-weeks stale
                response1.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build()
            }
        }
        return response1
    }

    private fun addAuthorisationHeader(request: Request, username: String?, token: String?): Request {
        var request1 = request
        if (username != null
                && token != null
                && request1.header("No-Authorization") != null) {

            request1 = request1
                    .newBuilder()
                    .header("Authorization",
                            "Basic " + Base64.encodeToString((username + ":" + token).toByteArray(),
                                    Base64.NO_WRAP))
                    .build()
        }
        return request1
    }

    /**
     * Check if the internet is available?
     *
     * @param context instance.
     * @return True if the internet is available else false.
     */
    private fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
