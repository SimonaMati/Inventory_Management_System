package com.smatiukaite.bhcc_ims

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Forgot_Password_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val email = findViewById<EditText>(R.id.email_edit_text)
        val tokenEditText = findViewById<EditText>(R.id.token_edit_text)
        val newPassword = findViewById<EditText>(R.id.new_password_edit_text)
        val repeatNewPassword = findViewById<EditText>(R.id.repeat_user_password_edit_text)

        val updateButton = findViewById<Button>(R.id.update_button)
        updateButton.setOnClickListener {
            val enteredEmail = email.text.toString()
            val enteredToken = tokenEditText.text.toString()
            val enteredNewPassword = newPassword.text.toString()
            val enteredRepeatNewPassword = repeatNewPassword.text.toString()

            if (enteredNewPassword != enteredRepeatNewPassword) {
                Toast.makeText(
                    this,
                    "New password and repeated new password do not match",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Check if the entered email and security code match a user record in the database
            val dbHelper = DataBaseHandler(this)
            val db = dbHelper.readableDatabase
            val query = "SELECT * FROM Users WHERE email = ? AND securityCode = ?"
            val cursor = db.rawQuery(query, arrayOf(enteredEmail, enteredToken))

            if (cursor.moveToFirst()) {
                // User with matching email and security code found
                // Retrieve the token from the cursor
                val token = cursor.getString(cursor.getColumnIndex("securityCode"))
                Log.d("Forgot_Password_Activity", "Token: $token")

                // Update the user's password with the new password
                val userId = cursor.getInt(cursor.getColumnIndex("id"))
                val values = ContentValues()
                values.put("password", enteredNewPassword)
                db.update("Users", values, "id = ?", arrayOf(userId.toString()))

                Toast.makeText(
                    this,
                    "Password updated successfully",
                    Toast.LENGTH_SHORT
                ).show()

                // Clear the security code for the user in the database
                values.clear()
                values.putNull("COL_SECURITY_CODE")
                db.update("Users", values, "id = ?", arrayOf(userId.toString()))

                // Redirect the user to the login activity
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // No user with matching email and security code found
                Toast.makeText(
                    this,
                    "Invalid email or security code",
                    Toast.LENGTH_SHORT
                ).show()
            }

            cursor.close()
            db.close()
        }
    }
}
