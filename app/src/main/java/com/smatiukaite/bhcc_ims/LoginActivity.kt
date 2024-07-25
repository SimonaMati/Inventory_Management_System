package com.smatiukaite.bhcc_ims

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var intent = Intent()

        //Create a new account
        val createAccount = findViewById<TextView>(R.id.textViewCreateAccount)
        createAccount.setOnClickListener {
            intent = Intent(this, CreateAccountActivity::class.java).apply {
            }
            startActivity(intent)
        }

        //Login to an account
        val email = findViewById<EditText>(R.id.editTextEmailAddress)
        val password = findViewById<EditText>(R.id.user_password_edit_text)
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            val emailStr = email.text.toString()
            val passwordStr = password.text.toString()

            val db = DataBaseHandler(this)
            val user = db.getUser(emailStr, passwordStr)
            if (user != null) {
                intent = Intent(this, HomeActivity::class.java).apply {
                    putExtra("id", user.id)
                    System.out.println("USERS ID IS " + user.id)

                    putExtra("firstName", user.firstName)
                    System.out.println("USERS NAME IS " + user.firstName)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid email address or password", Toast.LENGTH_SHORT).show()
            }
        }

//        FORGOT THE PASSWORD
      //  val forgotPasswordButton = findViewById<TextView>(R.id.forgotPassword)
//        forgotPasswordButton.setOnClickListener {
//            intent = Intent(this, Forgot_Password_Activity::class.java).apply {
//
//            }
//            startActivity(intent)
//        }

    }
}