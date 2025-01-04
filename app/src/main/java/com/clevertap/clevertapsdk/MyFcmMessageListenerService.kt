package com.clevertap.clevertapsdk
import com.clevertap.android.sdk.CleverTapAPI

import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFcmMessageListenerService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CleverTapAPI.getDefaultInstance(this)?.apply {
            pushFcmRegistrationId(token, true)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        // Handle notification creation using CleverTap
        CTFcmMessageHandler().createNotification(applicationContext, message)
    }

}