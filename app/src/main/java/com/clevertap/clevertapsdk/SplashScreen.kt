package com.clevertap.clevertapsdk

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.clevertap.android.sdk.CleverTapAPI


class SplashScreen : AppCompatActivity() {
    private val cleverTap by lazy { CleverTapAPI.getDefaultInstance(this) }
    private val splashScreenDelay: Long = 2000 // Duration of the splash screen in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        supportActionBar?.hide()

        cleverTap?.recordScreen("Product Viewed")

        // Make the activity fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Use a Handler to delay the transition to the Login screen
        Handler().postDelayed({
            navigateToLoginScreen()
        }, splashScreenDelay)
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
        finish()
    }


}

