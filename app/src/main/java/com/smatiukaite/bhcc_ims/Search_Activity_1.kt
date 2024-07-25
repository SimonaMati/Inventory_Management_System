package com.smatiukaite.bhcc_ims

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class Search_Activity_1 : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search1)

        // Retrieve the intent that started this activity
        var intent = getIntent()

        //User's name update
        val usersName = findViewById<TextView>(R.id.users_name)
        val usersID = intent.getIntExtra("id", 0)
        System.out.println("USER ID $usersID")

        val usersFirstName = intent.getStringExtra("firstName")
        usersName.text = usersFirstName ?: "Unknown user"

        //User corner
        val userIcon = findViewById<ImageView>(R.id.person_icon)
        userIcon.setOnClickListener {
            intent = Intent(this, UserPageActivity::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
        }

        //Top page information update
        val building = intent.getStringExtra("building").toString()
        System.out.println("BUILDING " + building)

        val campusName = findViewById<TextView>(R.id.campus_textview)
        val campus = intent.getStringExtra("campus")
        campusName.text = campus
        System.out.println("CAMPUS " + campusName.toString())

        val roomName = findViewById<TextView>(R.id.room_textview)
        val room = intent.getStringExtra("room")
        roomName.text = building + room
        System.out.println("ROOM " + room.toString())

        // Retrieve the year, campus, building, and room from the intent
        val year = intent.getIntExtra("year", 0)
//        val campus = intent.getStringExtra("campus") ?: ""
//        val building = intent.getStringExtra("building") ?: ""
//        val room = intent.getStringExtra("room") ?: ""

        // Fetch the items and their amounts from the SQLite database
        val itemList = fetchItemsInRoom(year, campus.toString(), building, room.toString())

        // Create a custom adapter to display the items and their amounts in the ListView
        val adapter = ItemListAdapter(this, itemList.toList())

        val listView = findViewById<ListView>(R.id.item_list_in_a_room)
        listView.adapter = adapter

        //ADD ITEM
        val addItemButton = findViewById<Button>(R.id.add_item_button)
        addItemButton.setOnClickListener {
            intent = Intent(this, Add_New_Room_Activity1::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
                putExtra("campus", campus)
                putExtra("building", building)
                putExtra("room", room)
            }
            startActivity(intent)
        }


        //EDIT LIST
        val editListButton = findViewById<Button>(R.id.edit_list_button)

        //DELETE LIST
        val deleteListButton = findViewById<Button>(R.id.delete_list_button)
        deleteListButton.setOnClickListener {
            val dbHandler = DataBaseHandler(this@Search_Activity_1)
            val buildingToDelete = building
            val roomToDelete = room?: ""
            val campusToDelete = campus?: ""

            val deleted = dbHandler.deleteInventoryItems(buildingToDelete, roomToDelete, campusToDelete)
            if (deleted) {
                // List of inventory items deleted successfully
                Toast.makeText(this@Search_Activity_1, "List deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Failed to delete list of inventory items
                Toast.makeText(this@Search_Activity_1, "Failed to delete list", Toast.LENGTH_SHORT).show()
            }
            intent = Intent(this@Search_Activity_1, Search_Activity::class.java).apply {
                putExtra("id",usersID)
                putExtra("firstName", usersFirstName)
            }
            startActivity(intent)
            dbHandler.close()
        }

        //VIEW LIST
        val viewListButton = findViewById<TextView>(R.id.view_list_button)
        viewListButton.setOnClickListener {
            intent = Intent(this, Search_Activity_3::class.java).apply {
                putExtra("id",usersID)
                putExtra("firstName", usersFirstName)
                putExtra("campus", campus)
                putExtra("building", building)
                putExtra("room", room)
            }
            startActivity(intent)
        }

        //Bottom Navigation bar access here
        bottomNavigation(usersID, usersFirstName)
    }

    private fun fetchItemsInRoom(year: Int, campus: String, building: String, room: String): Map<String, Int> {
        val dbHelper = DataBaseHandler(this)
        val itemList = HashMap<String, Int>()

        val db = dbHelper.readableDatabase
        val query = "SELECT itemName FROM Inventory WHERE year = ? AND campus = ? AND building = ? AND room = ?"
        val cursor = db.rawQuery(query, arrayOf(year.toString(), campus, building, room))

        if (cursor.moveToFirst()) {
            do {
                val itemName = cursor.getString(cursor.getColumnIndex("itemName"))

                if (itemList.containsKey(itemName)) {
                    val currentAmount = itemList[itemName] ?: 0
                    itemList[itemName] = currentAmount + 1
                } else {
                    itemList[itemName] = 1
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return itemList
    }

    // Custom adapter for the ListView
    private class ItemListAdapter(context: Context, private val itemList: List<Pair<String, Int>>) :
        ArrayAdapter<Pair<String, Int>>(context, R.layout.list_item_layout2, itemList) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.list_item_layout2, parent, false)

            val itemNameTextView = view.findViewById<TextView>(R.id.item_name)
            val itemAmountTextView = view.findViewById<TextView>(R.id.total_items_in_list)

            val item = itemList[position]
            itemNameTextView.text = item.first
            itemAmountTextView.text = item.second.toString()

            return view
        }
    }

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