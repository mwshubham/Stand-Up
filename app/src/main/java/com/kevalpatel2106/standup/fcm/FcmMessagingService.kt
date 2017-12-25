package com.kevalpatel2106.standup.fcm

import android.annotation.SuppressLint
import android.support.annotation.VisibleForTesting
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kevalpatel2106.standup.authentication.verification.EmailVerifiedNotification
import com.kevalpatel2106.utils.SharedPrefsProvider
import com.kevalpatel2106.utils.UserSessionManager

import timber.log.Timber


/**
 * Created by Keval on 01-Jun-17.
 * This service will handle the incoming FCM messages.
 *
 * @author <a href="https://github.com/kevalpatel2106">kevalpatel2106</a>
 * @see FcmMessagingService
 */

class FcmMessagingService : FirebaseMessagingService() {

    @SuppressLint("BinaryOperationInTimber", "VisibleForTests")
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        SharedPrefsProvider.init(this@FcmMessagingService)
        if (!shouldProcessNotification(remoteMessage?.data)) return

        Timber.d("onMessageReceived: " + remoteMessage!!.data.toString())

        //Handle based on type
        handleMessage(remoteMessage.data)
    }

    @VisibleForTesting
    internal fun handleMessage(data: Map<String, String>) {
        when (data["type"]) {
            NotificationType.TYPE_EMAIL_VERIFIED -> {

                //Change the flag to true.
                UserSessionManager.isUserVerified = true

                //Fire the notification
                EmailVerifiedNotification.notify(this.applicationContext, data["message"])
            }
        }
    }

    @VisibleForTesting
    internal fun shouldProcessNotification(data: Map<String, String>?): Boolean {
        if (data == null) {
            Timber.w("No message received in the FCM payload.")
            return false
        }

        //Check for the user logged in
        if (!UserSessionManager.isUserLoggedIn) {
            Timber.w("User is not registered. Skipping the message.")
            return false
        }

        //Handle for the type.
        if (!data.containsKey("type")) {
            Timber.w("Notification doesn't contain the type.")
            return false
        }

        return true
    }
}
