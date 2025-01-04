package com.clevertap.clevertapsdk
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.clevertap.android.sdk.CleverTapAPI
import com.google.android.material.snackbar.Snackbar


class LoginScreen : AppCompatActivity(), View.OnClickListener {
    private val clevertapDefaultInstance by lazy { CleverTapAPI.getDefaultInstance(this) }
    private lateinit var linearBody: LinearLayout
    private lateinit var textEditTextUser: EditText
    private lateinit var textEditTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var checkBox: CheckBox
    private lateinit var btnRegister: Button
    private lateinit var inputValidation: InputValidation
    private lateinit var dbHelper: DbHelper
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREF_NAME = "Shared_pref"
        const val KEY_EMAIL = "Email"
        const val KEY_PASSWORD = "password"
        const val KEY_CHECKBOX = "CHECKBOX"
        const val KEY_FIRST_RUN = "isFirstRun"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        supportActionBar?.hide()

        clevertapDefaultInstance?.recordScreen("Login Screen")

        initViews()
        initListeners()
        initObjects()

        // Handle first app run
        handleFirstRun()

        // Load saved user credentials if available
        loadUserCredentials()
    }

    private fun initViews() {
        textEditTextUser = findViewById(R.id.username_field)
        textEditTextPassword = findViewById(R.id.password_field)
        btnLogin = findViewById(R.id.login_button)
        checkBox = findViewById(R.id.checkBox)
        btnRegister = findViewById(R.id.register_button)
        linearBody = findViewById(R.id.linearBody)
    }

    private fun initListeners() {
        btnLogin.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }

    private fun initObjects() {
        dbHelper = DbHelper(this)
        inputValidation = InputValidation(this)
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun handleFirstRun() {
        val isFirstRun = sharedPreferences.getBoolean(KEY_FIRST_RUN, true)
        if (isFirstRun) {
            // Clear saved credentials on the first run after a fresh install
            clearSavedCredentials()
            val editor = sharedPreferences.edit()
            editor.putBoolean(KEY_FIRST_RUN, false)
            editor.apply()
        }
    }

    private fun loadUserCredentials() {
        val savedEmail = sharedPreferences.getString(KEY_EMAIL, null)
        val savedPassword = sharedPreferences.getString(KEY_PASSWORD, null)
        val isRemembered = sharedPreferences.getBoolean(KEY_CHECKBOX, false)

        if (isRemembered && !savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
            textEditTextUser.setText(savedEmail)
            textEditTextPassword.setText(savedPassword)
            checkBox.isChecked = true

            // Automatically verify and login
            autoLogin(savedEmail, savedPassword)
        }
    }

    private fun autoLogin(email: String, password: String) {
        if (dbHelper.logCheckUser(email, password)) {
            val profileUpdate = hashMapOf<String, Any?>(
                "EMAIL" to email
            )
            clevertapDefaultInstance?.onUserLogin(profileUpdate)

            navigateToMainActivity()
        } else {
            Snackbar.make(linearBody, getString(R.string.error_message_invalid), Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_button -> verifyFromDB()
            R.id.register_button -> navigateToRegisterScreen()
        }
    }

    private fun navigateToRegisterScreen() {
        startActivity(Intent(this, RegisterScreen::class.java))
    }

    private fun verifyFromDB() {
        if (!inputValidation.isTextFilled(textEditTextUser, getString(R.string.error_message_nofill_name)) ||
            !inputValidation.isTextFilled(textEditTextPassword, getString(R.string.error_message_nofill_pass))
        ) {
            return
        }

        if (dbHelper.logCheckUser(
                textEditTextUser.text.toString().trim(),
                textEditTextPassword.text.toString().trim()
            )
        ) {
            saveUserCredentials()
            navigateToMainActivity()
        } else {
            Snackbar.make(linearBody, getString(R.string.error_message_invalid), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun saveUserCredentials() {
        val editor = sharedPreferences.edit()
        if (checkBox.isChecked) {
            editor.putString(KEY_EMAIL, textEditTextUser.text.toString())
            editor.putString(KEY_PASSWORD, textEditTextPassword.text.toString())
            editor.putBoolean(KEY_CHECKBOX, true)
            Toast.makeText(this, "User credentials saved", Toast.LENGTH_SHORT).show()

            // Tracking user identity post successful login to the app
            val profileUpdate = hashMapOf<String, Any?>(
                "EMAIL" to textEditTextUser.text.toString()
            )
            clevertapDefaultInstance?.onUserLogin(profileUpdate)

        } else {
            editor.remove(KEY_EMAIL)
            editor.remove(KEY_PASSWORD)
            editor.putBoolean(KEY_CHECKBOX, false)
        }
        editor.apply()
    }

    private fun clearSavedCredentials() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_EMAIL)
        editor.remove(KEY_PASSWORD)
        editor.remove(KEY_CHECKBOX)
        editor.apply()
    }

    private fun navigateToMainActivity() {
        val mainIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("uname", textEditTextUser.text.toString().trim())
        }
        textEditTextUser.text.clear()
        textEditTextPassword.text.clear()
        startActivity(mainIntent)
        finish()
    }
}



















































































































/*class LoginScreen : AppCompatActivity(), View.OnClickListener {
    private val clevertapDefaultInstance by lazy { CleverTapAPI.getDefaultInstance(this) }
    private lateinit var linearBody: LinearLayout
    private lateinit var textEditTextUser: EditText
    private lateinit var textEditTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var checkBox: CheckBox
    private lateinit var btnRegister: Button
    private lateinit var inputValidation: InputValidation
    private lateinit var dbHelper: DbHelper
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREF_NAME = "Shared_pref"
        const val KEY_EMAIL = "Email"
        const val KEY_PASSWORD = "password"
        const val KEY_CHECKBOX = "CHECKBOX"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        supportActionBar?.hide()

        clevertapDefaultInstance?.recordScreen("Login Screen")

        initViews()
        initListeners()
        initObjects()
        loadUserCredentials()
    }

    private fun initViews() {
        textEditTextUser = findViewById(R.id.username_field)
        textEditTextPassword = findViewById(R.id.password_field)
        btnLogin = findViewById(R.id.login_button)
        checkBox = findViewById(R.id.checkBox)
        btnRegister = findViewById(R.id.register_button)
        linearBody = findViewById(R.id.linearBody)
    }

    private fun initListeners() {
        btnLogin.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }

    private fun initObjects() {
        dbHelper = DbHelper(this)
        inputValidation = InputValidation(this)
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }


   private fun loadUserCredentials() {
       val savedEmail = sharedPreferences.getString(KEY_EMAIL, null)
       val savedPassword = sharedPreferences.getString(KEY_PASSWORD, null)
       val isRemembered = sharedPreferences.getBoolean(KEY_CHECKBOX, false)

       if (isRemembered && !savedEmail.isNullOrEmpty() && !savedPassword.isNullOrEmpty()) {
           textEditTextUser.setText(savedEmail)
           textEditTextPassword.setText(savedPassword)
           checkBox.isChecked = true

           // Automatically verify and login
           autoLogin(savedEmail, savedPassword)
       }
   }

    private fun autoLogin(email: String, password: String) {
        if (dbHelper.logCheckUser(email, password)) {
            var profileUpdate: HashMap<String?, Any?>? = HashMap<String?,Any?>()
            profileUpdate?.put("EMAIL", textEditTextUser.text.toString())
            clevertapDefaultInstance?.onUserLogin(profileUpdate)

            navigateToMainActivity()
        } else {
            Snackbar.make(linearBody, getString(R.string.error_message_invalid), Snackbar.LENGTH_LONG).show()
        }
    }



    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_button -> verifyFromDB()
            R.id.register_button -> navigateToRegisterScreen()
        }
    }

    private fun navigateToRegisterScreen() {
        startActivity(Intent(this, RegisterScreen::class.java))
    }

    private fun verifyFromDB() {
        if (!inputValidation.isTextFilled(textEditTextUser, getString(R.string.error_message_nofill_name)) ||
            !inputValidation.isTextFilled(textEditTextPassword, getString(R.string.error_message_nofill_pass))
        ) {
            return
        }

        if (dbHelper.logCheckUser(
                textEditTextUser.text.toString().trim(),
                textEditTextPassword.text.toString().trim()
            )
        ) {
            saveUserCredentials()
            navigateToMainActivity()
        } else {
            Snackbar.make(linearBody, getString(R.string.error_message_invalid), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun saveUserCredentials() {
        val editor = sharedPreferences.edit()
        if (checkBox.isChecked) {
            editor.putString(KEY_EMAIL, textEditTextUser.text.toString())
            editor.putString(KEY_PASSWORD, textEditTextPassword.text.toString())
            editor.putBoolean(KEY_CHECKBOX, true)
            Toast.makeText(this, "User credentials saved", Toast.LENGTH_SHORT).show()

          // Tracking user identity post successful login to the app

            var profileUpdate: HashMap<String?, Any?>? = HashMap<String?,Any?>()
            profileUpdate?.put("EMAIL", textEditTextUser.text.toString())
            clevertapDefaultInstance?.onUserLogin(profileUpdate)


        } else {
            editor.remove(KEY_EMAIL)
            editor.remove(KEY_PASSWORD)
            editor.putBoolean(KEY_CHECKBOX, false)
        }
        editor.apply()
    }

    private fun navigateToMainActivity() {
        val mainIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("uname", textEditTextUser.text.toString().trim())
        }
        textEditTextUser.text.clear()
        textEditTextPassword.text.clear()
        startActivity(mainIntent)
        finish()
    }
}*/


















































































































































































/*package com.netcore.smarttechdemo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.netcore.android.Smartech
import io.hansel.hanselsdk.Hansel
import java.lang.ref.WeakReference

class LoginScreen : AppCompatActivity(), View.OnClickListener {

    private lateinit var linearBody: LinearLayout
    private lateinit var textEditTextUser: EditText
    private lateinit var textEditTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var checkBox: CheckBox
    private lateinit var btnRegister: Button
    private lateinit var inputValidation: InputValidation
    private lateinit var dbHelper: DbHelper
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREF_NAME = "Shared_pref"
        const val KEY_EMAIL = "Email"
        const val KEY_PASSWORD = "password"
        const val KEY_CHECKBOX = "CHECKBOX"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        supportActionBar?.hide()

        initViews()
        initListeners()
        initObjects()
        loadUserCredentials()

    }

    private fun initViews() {
        textEditTextUser = findViewById(R.id.username_field)
        textEditTextPassword = findViewById(R.id.password_field)
        btnLogin = findViewById(R.id.login_button)
        checkBox = findViewById(R.id.checkBox)
        btnRegister = findViewById(R.id.register_button)
        linearBody = findViewById(R.id.linearBody)
    }

    private fun loadUserCredentials() {
        val savedEmail = sharedPreferences.getString(KEY_EMAIL, null)
        val savedPassword = sharedPreferences.getString(KEY_PASSWORD, null)
        val isRemembered = sharedPreferences.getBoolean(KEY_CHECKBOX, false)

        // If user had previously opted to remember credentials, autofill them
        if (isRemembered && savedEmail != null && savedPassword != null) {
            textEditTextUser.setText(savedEmail)
            textEditTextPassword.setText(savedPassword)
            checkBox.isChecked = true
        }
    }

    private fun initListeners() {
        btnLogin.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }

    private fun initObjects() {
        dbHelper = DbHelper(this)
        inputValidation = InputValidation(this)
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }



    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_button -> verifyFromDB()
            R.id.register_button -> navigateToRegisterScreen()
        }
    }

    private fun navigateToRegisterScreen() {
        val payload : HashMap<String, Any> = HashMap()
        Smartech.getInstance(WeakReference(this)).trackEvent("opened_app", payload)

        val intentReg = Intent(applicationContext, RegisterScreen::class.java)
        startActivity(intentReg)
    }

    private fun verifyFromDB() {
        if (!inputValidation.isTextFilled(textEditTextUser, getString(R.string.error_message_nofill_name)) ||
            !inputValidation.isTextFilled(textEditTextPassword, getString(R.string.error_message_nofill_pass))
        ) {
            return
        }

        if (dbHelper.logCheckUser(
                textEditTextUser.text.toString().trim(),
                textEditTextPassword.text.toString().trim()
            )
        ) {
            saveUserCredentials()
            if (sharedPreferences.getBoolean(KEY_CHECKBOX, false)) {
                navigateToMainActivity()
            }
        } else {
            Snackbar.make(linearBody, getString(R.string.error_message_invalid), Snackbar.LENGTH_LONG).show()
        }
    }


    //this function need to handle at the time logout:
*//*    private fun logout() {
        val editor = sharedPreferences.edit()
        editor.clear() // Clear all saved data
        editor.apply()

        // Clear fields and uncheck "Remember Me"
        textEditTextUser.text = null
        textEditTextPassword.text = null
        checkBox.isChecked = false

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        // Redirect to login screen if needed
        startActivity(Intent(this, LoginScreen::class.java))
        finish()
    }*//*


    private fun saveUserCredentials() {
        val editor = sharedPreferences.edit()
        if (checkBox.isChecked) {
            // Save credentials only if "Remember Me" is checked
            editor.putString(KEY_EMAIL, textEditTextUser.text.toString())
            Smartech.getInstance(WeakReference(applicationContext)).login(textEditTextUser.text.toString())
            Hansel.getUser().setUserId(textEditTextUser.text.toString())
            editor.putString(KEY_PASSWORD, textEditTextPassword.text.toString())
            editor.putBoolean(KEY_CHECKBOX, true)
            Toast.makeText(this, "User credentials saved", Toast.LENGTH_SHORT).show()
        } else {
            // Clear saved credentials if unchecked
            editor.remove(KEY_EMAIL)
            editor.remove(KEY_PASSWORD)
            editor.putBoolean(KEY_CHECKBOX, false)
        }
        editor.apply()
    }


    private fun navigateToMainActivity() {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.putExtra("uname", textEditTextUser.text.toString().trim())
        textEditTextUser.text = null
        textEditTextPassword.text = null
        startActivity(mainIntent)
        finish()
    }
}*/























































/*
package com.netcore.smarttechdemo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.netcore.android.Smartech
import io.hansel.hanselsdk.Hansel
import java.lang.ref.WeakReference

class LoginScreen: AppCompatActivity(), View.OnClickListener {
    private val activity = this@LoginScreen
    private lateinit var linearBody: LinearLayout
    private lateinit var textEditTextUser: EditText
    private lateinit var textEditTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var checkBox: CheckBox
    private lateinit var btnRegister: Button
    private lateinit var inputValidation: InputValidation
    private lateinit var dbHelper: DbHelper
    lateinit var sharedPreferences: SharedPreferences
    var isRemembered=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        supportActionBar!!.hide()

        initViews()
        initListeners()
        initObjects()
        sharedPreferences=getSharedPreferences("Shared_pref",Context.MODE_PRIVATE)

    }

    //Initialize views
    private fun initViews() {
        textEditTextUser = findViewById(R.id.username_field)
        textEditTextPassword = findViewById(R.id.password_field)
        btnLogin = findViewById(R.id.login_button)
        checkBox=findViewById(R.id.checkBox)
        btnRegister = findViewById(R.id.register_button)
        linearBody = findViewById(R.id.linearBody)
    }
    //Initialize listeners
    private fun initListeners() {
        btnLogin.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }
    //Initialize objects
    private fun initObjects() {
        dbHelper = DbHelper(activity)
        inputValidation = InputValidation(activity)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.login_button -> verifyFromDB()
            R.id.register_button -> {
                val payload : HashMap<String, Any> = HashMap()
                payload["source"] = "android"

                Smartech.getInstance(WeakReference(this)).trackEvent("opened_app", payload)
                val intentReg = Intent(applicationContext, RegisterScreen::class.java)
                startActivity(intentReg)
            }
        }
    }

    private fun verifyFromDB() {

        val checked:Boolean=checkBox.isChecked
        val editor:SharedPreferences.Editor=sharedPreferences.edit()
        editor.putString("Email",textEditTextUser.text.toString())
        Hansel.getUser().setUserId(textEditTextUser.text.toString())
        editor.putString("password",textEditTextPassword.text.toString())
        editor.putBoolean("CHECKBOX",checked)
        editor.apply()
        Toast.makeText(this,"usercredentilas saved",Toast.LENGTH_SHORT).show()
        if (!inputValidation.isTextFilled(textEditTextUser, getString(R.string.error_message_nofill_name))) {

            return
        }
        if (!inputValidation.isTextFilled(textEditTextPassword, getString(R.string.error_message_nofill_pass))) {
            return
        }

        if (dbHelper.logCheckUser(textEditTextUser.text.toString().trim() { it <= ' '}, textEditTextPassword.text.toString().trim() { it <= ' '})) {

            isRemembered=sharedPreferences.getBoolean("CHECKBOX",false)
            if (isRemembered){
                val mainIntent = Intent(activity, MainActivity::class.java)
                mainIntent.putExtra("uname", textEditTextUser.text.toString().trim())
                textEditTextUser.text = null
                textEditTextPassword.text = null
                startActivity(mainIntent)
                finish()
            }

        } else {
            Snackbar.make(linearBody, getString(R.string.error_message_invalid), Snackbar.LENGTH_LONG).show()
        }
    }
}*/
