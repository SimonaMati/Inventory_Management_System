package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.core.view.isEmpty
import com.google.android.material.bottomnavigation.BottomNavigationView

class Inventory_Lists_Activity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_lists)

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

        //Load the list of lists
        val noContent = findViewById<TextView>(R.id.no_content_text)
        val listView = findViewById<ListView>(R.id.inventory_lists)
        val inventoryList = fetchInventoryData()
        val adapter = InventoryListAdapter(this, inventoryList)
        listView.adapter = adapter

        noContent.visibility = if (inventoryList == null || inventoryList.isEmpty()) View.VISIBLE else View.INVISIBLE
        listView.visibility = if (inventoryList == null || inventoryList.isEmpty()) View.INVISIBLE else View.VISIBLE

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = adapter.getItem(position)

            val year = selectedItem?.year ?: 0
            val campus = selectedItem?.campus ?: ""
            var room = selectedItem?.room ?: ""

            // Check if the room number starts with a letter followed by a number
            val roomPattern = Regex("[A-Za-z]\\d+")
            if (roomPattern.matches(room)) {
                room = room.substring(1) // Remove the first character
            }

            intent = Intent(this, Search_Activity_1::class.java).apply {
                putExtra("id", id)
                putExtra("firstName", usersFirstName)
                putExtra("year", selectedItem?.year)
                putExtra("campus", selectedItem?.campus)
                putExtra("building", selectedItem?.building)
                putExtra("room", room)
                System.out.println("ROOM " + room)
            }
            startActivity(intent)
        }

        //Bottom Navigation bar access here
        bottomNavigation(usersID, usersFirstName)
    }

    private fun fetchInventoryData(): List<InventoryItem> {
        val dbHelper = DataBaseHandler(this)
        val inventoryList = ArrayList<InventoryItem>()

        val db = dbHelper.readableDatabase
        val query =
            "SELECT year, campus, building, room FROM Inventory Group By year, campus, building, room"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {

            do {
                val year = cursor.getInt(cursor.getColumnIndex("year"))
                val campus = cursor.getString(cursor.getColumnIndex("campus"))
                val building = cursor.getString(cursor.getColumnIndex("building"))
                val room = cursor.getInt(cursor.getColumnIndex("room"))


                // Different year, campus, building, or room
                val roomStr = building + room

                val inventoryItem = InventoryItem(
                    0, // Provide the correct ID if you have it, or use 0 for new item
                    year,
                    campus,
                    building, // Provide empty string or default value for building
                    roomStr,
                    "", // Provide empty string or default value for itemName
                    "", // Provide empty string or default value for barcode
                    "", // Provide empty string or default value for serialNumber
                    "", // Provide empty string or default value for comment
                    0 // Provide the correct creatorId if you have it, or use 0 for unknown creator
                )
                inventoryList.add(inventoryItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return inventoryList
    }

    fun bottomNavigation(usersID: Int, usersFirstName: String?) {
        // Find and initialize bottomNav
        bottomNavigationView = findViewById(R.id.bottomNav)

        // Set up the selected item and listener for bottomNav
        bottomNavigationView.setSelectedItemId(R.id.inventory_lists)
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
                    startActivity(intent4)
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.inventory_lists -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

}