package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Campus_Maps_Activity_2 : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campus_maps2)

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
        //Bottom Navigation bar access here
        bottomNavigation(usersID, usersFirstName)
    }
    fun bottomNavigation(usersID: Int, usersFirstName: String?) {
        //BOTTOM NAVIGATION BAR
        // Find and initialize bottomNav
        bottomNavigationView = findViewById(R.id.bottomNav)

        // Set up the selected item and listener for bottomNav
        bottomNavigationView.setSelectedItemId(R.id.campus_map)
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
                    startActivity(intent2)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.statistics -> {
                    startActivity(intent3)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.campus_map -> {
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