package com.clevertap.clevertapsdk

import android.app.Application
import android.os.Bundle
import com.clevertap.android.sdk.ActivityLifecycleCallback
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.pushnotification.amp.CTPushAmpListener

class MainApplication : Application(){

    override fun onCreate() {
        ActivityLifecycleCallback.register(this)
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG)
        super.onCreate()
       val cleverTapAPI = CleverTapAPI.getDefaultInstance(this)
        cleverTapAPI?.promptForPushPermission(true)





    }


}
