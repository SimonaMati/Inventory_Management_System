package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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

        //Search
        var searchButton = findViewById<TextView>(R.id.searchButton_home)
        searchButton.setOnClickListener {
            intent = Intent(this, Search_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
        }

        var addNewRoomButton = findViewById<TextView>(R.id.addNewRoomButtonHome)
        addNewRoomButton.setOnClickListener {
            intent = Intent(this, Add_New_Room_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
        }

        var statisticsButton = findViewById<TextView>(R.id.statisticsButtonHome)
        statisticsButton.setOnClickListener {
            intent = Intent(this, Statistics_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
        }

        var campusMapButton = findViewById<TextView>(R.id.campusMapButtonHome)
        campusMapButton.setOnClickListener {
            intent = Intent(this, Campus_Maps_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
        }

        var inventoryListsButton = findViewById<TextView>(R.id.inventoryListButtonHome)
        inventoryListsButton.setOnClickListener {
            intent = Intent(this, Inventory_Lists_Activity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
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