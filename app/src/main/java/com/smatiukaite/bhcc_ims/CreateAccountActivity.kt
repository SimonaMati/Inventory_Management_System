package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val context = this

        val firstName = findViewById<EditText>(R.id.editTextFirstName)
        val lastName = findViewById<EditText>(R.id.editTextLastName)
        val email = findViewById<EditText>(R.id.editTextEmailAddress)
        val password = findViewById<EditText>(R.id.user_password_edit_text)
        val repeatPassword = findViewById<EditText>(R.id.repeat_user_password_edit_text)

        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            if (firstName.text.toString().isNotEmpty() &&
                lastName.text.toString().isNotEmpty() &&
                email.text.toString().isNotEmpty() &&
                password.text.toString().isNotEmpty() &&
                repeatPassword.text.toString().isNotEmpty()
            ) {

                val user = User(
                    firstName.text.toString(),
                    lastName.text.toString(),
                    email.text.toString(),
                    password.text.toString(),
                    repeatPassword.text.toString()
                )

                val db = DataBaseHandler(context)
                db.insertData(user)

            val intent = Intent(this, LoginActivity::class.java).apply {
            }
                startActivity(intent)

            } else {
                Toast.makeText(context, "Please fill all the data", Toast.LENGTH_SHORT).show()
            }
        }

    }
}