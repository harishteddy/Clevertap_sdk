package com.clevertap.clevertapsdk

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.clevertap.android.sdk.CleverTapAPI
import com.clevertap.android.sdk.pushnotification.CTPushNotificationListener
import java.util.Date
import android.Manifest
import android.content.Intent
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import java.util.*

class MainActivity : AppCompatActivity(), CTPushNotificationListener {

    private val cleverTap by lazy { CleverTapAPI.getDefaultInstance(this) }
    private lateinit var appIdButton: CardView
    private lateinit var sdkRelases: CardView
    private lateinit var loginButton: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Initialize Views
        appIdButton = findViewById(R.id.dash_appid)
        sdkRelases=findViewById(R.id.sdk_releases)
        loginButton = findViewById(R.id.login_id)

        // Request notification permission for Android 13+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission()
            }
        }

        // Initialize CleverTap and set notification listener
        cleverTap?.ctPushNotificationListener = this



        // Set Click Listeners
        appIdButton.setOnClickListener {
            val intent = Intent(this, UpdateProfileScreen::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener {
            cleverTap?.pushEvent("Loggedout")
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
          finish()



        }

        sdkRelases.setOnClickListener(){
            val prodViewedAction = mapOf(
                "Product Name" to "Casio Chronograph Watch",
                "Category" to "Men's Accessories", // Fixed grammatical error
                "Price" to 59.99,                 // CleverTap supports Double type
                "Date" to Date().toString()       // Convert Date to String for compatibility
            )
            cleverTap?.pushEvent("Product viewed", prodViewedAction)
            Toast.makeText(this, " Product viewed", Toast.LENGTH_SHORT).show()

        }




    }

    private fun requestNotificationPermission() {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                showToast("Notification permission granted")
            } else {
                showToast("Notification permission denied")
            }
        }

        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Handle notification click payload
    override fun onNotificationClickedPayloadReceived(payload: HashMap<String, Any>?) {
        payload?.let {
            Log.d("Notification Payload", it.toString())
        } ?: run {
            Log.d("Notification Payload", "Payload is null")
        }
    }
}







































