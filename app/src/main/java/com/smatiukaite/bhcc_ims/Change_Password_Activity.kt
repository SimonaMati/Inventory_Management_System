package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class Change_Password_Activity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        // Retrieve the intent that started this activity
        var intent = getIntent()

        //User's name update
        val usersName = findViewById<TextView>(R.id.users_name)
        val usersID = intent.getIntExtra("id", 0)
        System.out.println("USER ID " + usersID)

        val usersFirstName = intent.getStringExtra("firstName")
        if (usersFirstName != null && usersFirstName.isNotEmpty()) {
            usersName.text = usersFirstName
        }

        //User corner
        val userIcon = findViewById<ImageView>(R.id.person_icon)
        userIcon.setOnClickListener {
            intent = Intent(this, UserPageActivity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
        }

        val email = findViewById<EditText>(R.id.email_edit_text)
        val oldPassword = findViewById<EditText>(R.id.old_password_edit_text)
        val newPassword = findViewById<EditText>(R.id.new_password_edit_text)
        val repeatNewPassword = findViewById<EditText>(R.id.repeat_user_password_edit_text)

        val updateButton = findViewById<Button>(R.id.update_button)
        updateButton.setOnClickListener {
            val enteredEmail = email.text.toString()
            val enteredOldPassword = oldPassword.text.toString()
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

            val dbHandler = DataBaseHandler(this)
            val user = dbHandler.getUser(enteredEmail, enteredOldPassword)

            if (user != null && user.retrieveFinalPassword() == enteredOldPassword) {
                user.finalPassword = enteredNewPassword
                dbHandler.updateUser(user)
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()

                intent = Intent(this, UserPageActivity::class.java).apply {
                    putExtra("id", usersID)
                    putExtra("firstName", usersFirstName)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Incorrect old password", Toast.LENGTH_SHORT).show()
            }
        }

        //Bottom Navigation bar access here
        bottomNavigation(usersID, usersFirstName)
    }

    fun bottomNavigation(usersID: Int, usersFirstName: String?) {
        // Find and initialize bottomNav
        bottomNavigationView = findViewById(R.id.bottomNav)

        // Set up the selected item and listener for bottomNav
        bottomNavigationView.setSelectedItemId(R.id.home)
        bottomNavigationView.setOnItemSelectedListener { item ->
            val intent1 = Intent(applicationContext, Search_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            val intent2 = Intent(applicationContext, HomeActivity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            val intent3 = Intent(applicationContext, Statistics_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            val intent4 = Intent(applicationContext, Campus_Maps_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            val intent5 = Intent(applicationContext, Inventory_Lists_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            when (item.itemId) {
                R.id.search -> {
                    startActivity(intent1)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.home -> {
                    true
                }
                R.id.statistics -> {
                    startActivity(intent3)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.campus_map -> {
                    startActivity(intent4)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.inventory_lists -> {
                    startActivity(intent5)
                    overridePendingTransition(0, 0)
                    true
                }
                else -> {
                    false
                }
            }
        }
}
}