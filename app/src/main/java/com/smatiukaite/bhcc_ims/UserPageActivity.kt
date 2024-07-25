package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserPageActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var dataBaseHandler: DataBaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_page)

        // Initialize your dataBaseHandler instance
        dataBaseHandler = DataBaseHandler(this)

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

        //Three buttons
        //EDIT USER INFORMATION
        val editUserInfoButton = findViewById<Button>(R.id.edit_user_info_button)
        editUserInfoButton.setOnClickListener {
            intent = Intent(this, User_Page_Edit_Info_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
        }

        //CHANGE USER'S PASSWORD
        val changePasswordButton = findViewById<Button>(R.id.change_password_button)
        changePasswordButton.setOnClickListener {
            intent = Intent(this, Change_Password_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
        }

        //ENTER LISTS THAT WERE CREATED BY THIS USER
        val enterUsersListsButton = findViewById<Button>(R.id.user_made_lists_button)
        enterUsersListsButton.setOnClickListener {
            intent = Intent(this, User_Created_Lists_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
        }

        //LOG OUT
        val logoutButton = findViewById<Button>(R.id.signOutButton)
        logoutButton.setOnClickListener {
            dataBaseHandler.logout()
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //DELETE ACCOUNT
        val deleteAccountButton = findViewById<Button>(R.id.deleteAccountButton)
        deleteAccountButton.setOnClickListener {
            Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
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