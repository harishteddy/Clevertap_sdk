package com.clevertap.clevertapsdk

import android.annotation.SuppressLint
import android.os.Bundle
import com.clevertap.android.sdk.CleverTapAPI

import com.clevertap.android.sdk.pushnotification.fcm.CTFcmMessageHandler
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFcmMessageListenerService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)


    }

    override fun onMessageReceived(message: RemoteMessage) {
        // Handle notification creation using CleverTap
        CTFcmMessageHandler().createNotification(applicationContext, message)

    }



}