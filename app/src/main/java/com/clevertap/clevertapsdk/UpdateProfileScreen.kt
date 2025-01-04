package com.clevertap.clevertapsdk

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.clevertap.android.sdk.CleverTapAPI
import java.text.SimpleDateFormat
import java.util.*

class UpdateProfileScreen : AppCompatActivity() {
    private val cleverTap by lazy { CleverTapAPI.getDefaultInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_profile_screen)

        // References to UI components
        val emailEditText = findViewById<EditText>(R.id.et_email)
        val usernameEditText=findViewById<EditText>(R.id.et_username)
        val mobileEditText = findViewById<EditText>(R.id.et_mobile)
        val dobEditText = findViewById<EditText>(R.id.et_dob)
        val genderRadioGroup = findViewById<RadioGroup>(R.id.rg_gender)
        val submitButton = findViewById<Button>(R.id.btn_submit)

        // Show Date Picker Dialog on DOB field click
        dobEditText.setOnClickListener {
            showDatePickerDialog { selectedDate ->
                dobEditText.setText(selectedDate)
            }
        }

        // Handle Submit Button Click
        submitButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val userName = usernameEditText.text.toString()
            val mobile = mobileEditText.text.toString()
            val dob = dobEditText.text.toString()
            val genderId = genderRadioGroup.checkedRadioButtonId
            val gender = if (genderId == R.id.rb_male) "Male" else "Female"

            if (email.isEmpty() || mobile.isEmpty() || dob.isEmpty()||userName.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                // Push profile data to CleverTap
                val profileUpdate = hashMapOf<String, Any>(
                     "EMAIL" to email,
                     "MOBILE" to mobile,
                     "GENDER" to gender,
                     "DOB" to dob,
                     "USERNAME" to userName
                )
                cleverTap?.pushProfile(profileUpdate)

                Toast.makeText(this, "User profile update", Toast.LENGTH_SHORT).show()
            }
        }
    }




    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                // Use yyyy-MM-dd format
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                onDateSelected(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

}
