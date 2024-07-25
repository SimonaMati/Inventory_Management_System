package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class Search_Activity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var adapter: InventoryListAdapter
    private lateinit var listView: ListView
    private lateinit var noContent: TextView
    private var inventoryList: List<InventoryItem> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
//        inventoryList = fetchInventoryData()
        adapter = InventoryListAdapter(this, inventoryList)

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

        //Search by room name or number
        val searchByRoom = findViewById<EditText>(R.id.edit_text_room_name)

        //User corner
        val userIcon = findViewById<ImageView>(R.id.person_icon)
        userIcon.setOnClickListener {
            intent = Intent(this, UserPageActivity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
        }

        //ACCESS THE SPINNERS
        // Campus Spinner
        val campusSpinner: Spinner = findViewById(R.id.spinner_campus_search)
        val campusArray = resources.getStringArray(R.array.campus_array)
        val campusAdapter = ArrayAdapter(this, R.layout.spinner_item_layout, campusArray)
        campusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        campusSpinner.adapter = campusAdapter

        campusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedCampus = campusArray[position]
                inventoryList = fetchInventoryData(selectedCampus)
                adapter = InventoryListAdapter(this@Search_Activity, inventoryList)
                listView.adapter = adapter

                noContent.visibility =
                    if (inventoryList.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
                listView.visibility =
                    if (inventoryList.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        // Building Spinner
        val buildingSpinner: Spinner = findViewById(R.id.spinner_building_search)
        val buildingArray = resources.getStringArray(R.array.building_array)
        val buildingAdapter =
            ArrayAdapter(this, R.layout.spinner_item_layout, buildingArray)
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        buildingSpinner.adapter = buildingAdapter

        // Load the list of lists
        noContent = findViewById<TextView>(R.id.no_content_text)
        listView = findViewById<ListView>(R.id.inventory_lists)
        inventoryList =
            fetchInventoryData("Select a Campus") // Fetch all unfiltered inventory items
        adapter = InventoryListAdapter(this, inventoryList)
        listView.adapter = adapter

        noContent.visibility =
            if (inventoryList.isNullOrEmpty()) View.VISIBLE else View.INVISIBLE
        listView.visibility =
            if (inventoryList.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE

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

    private fun fetchInventoryData(campus: String): List<InventoryItem> {
        val dbHelper = DataBaseHandler(this)
        val inventoryList = ArrayList<InventoryItem>()

        val db = dbHelper.readableDatabase
        val query: String
        val selectionArgs: Array<String>

        if (campus.isEmpty() || campus == "Select a Campus") {
            query = "SELECT year, campus, building, room FROM Inventory GROUP BY year, campus, building, room"
            selectionArgs = emptyArray()
        } else {
            query =
                "SELECT year, campus, building, room FROM Inventory WHERE campus = ? GROUP BY year, campus, building, room"
            selectionArgs = arrayOf(campus)
        }

        val cursor = db.rawQuery(query, selectionArgs)

        if (cursor.moveToFirst()) {
            do {
                val year = cursor.getInt(cursor.getColumnIndex("year"))
                val campus = cursor.getString(cursor.getColumnIndex("campus"))
                val building = cursor.getString(cursor.getColumnIndex("building"))
                val room = cursor.getInt(cursor.getColumnIndex("room"))

                val roomStr = building + room

                val inventoryItem = InventoryItem(
                    0,
                    year,
                    campus,
                    building,
                    roomStr,
                    "",
                    "",
                    "",
                    "",
                    0
                )
                inventoryList.add(inventoryItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return inventoryList
    }

//    private fun performSearchByRoom(roomName: String) {
//        val filteredList = inventoryList.filter { inventoryItem ->
//            inventoryItem.room.contains(roomName, ignoreCase = true)
//        }
//        adapter.updateData(filteredList)
//
//        // Show or hide the "no content" message based on the filtered list
//        noContent.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.INVISIBLE
//        listView.visibility = if (filteredList.isEmpty()) View.INVISIBLE else View.VISIBLE
//    }

//    private fun performSearchByBuilding(building: String) {
//        val filteredList = inventoryList.filter { inventoryItem ->
//            inventoryItem.building.equals(building, ignoreCase = true)
//        }
//        adapter.updateData(filteredList)
//
//        // Show or hide the "no content" message based on the filtered list
//        noContent.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.INVISIBLE
//        listView.visibility = if (filteredList.isEmpty()) View.INVISIBLE else View.VISIBLE
//    }


    fun bottomNavigation(usersID: Int, usersFirstName: String?) {
        // Find and initialize bottomNav
        bottomNavigationView = findViewById(R.id.bottomNav)

        // Set up the selected item and listener for bottomNav
        bottomNavigationView.setSelectedItemId(R.id.search)
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