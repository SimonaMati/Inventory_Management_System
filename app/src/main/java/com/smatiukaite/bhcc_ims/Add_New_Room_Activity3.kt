package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text
import java.util.*

class Add_New_Room_Activity3 : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_room3)
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

        //Get item and barcode
        val itemText = findViewById<TextView>(R.id.item_textView)
        val item = intent.getStringExtra("item")
        itemText.text = item
        System.out.println("ITEM " + item.toString())

        val barcodeText = findViewById<TextView>(R.id.barcode)
        val barcode = intent.getStringExtra("barcode")
        barcodeText.text = barcode
        System.out.println("BARCODE " + barcode.toString())

        //Top page information update
        val campus = intent.getStringExtra("campus").toString()
        System.out.println("CAMPUS " + campus)

        val room = intent.getStringExtra("room").toString()
        System.out.println("ROOM " + room)

        val building = intent.getStringExtra("building").toString()
        System.out.println("BUILDING " + building)

        //Show total items in this room
       // val totalItemInThisRoom = findViewById<TextView>(R.id.total_items_in_this_room_textview)
        val dbHandler = DataBaseHandler(this)
        val itemCount = dbHandler.getTotalItemCount(campus.toString(), building, room)
      //  totalItemInThisRoom.text = "Total Items: $itemCount"

        //Add serial number and comment
        val serialNumber = findViewById<EditText>(R.id.serial_number_editText)
        val comment = findViewById<EditText>(R.id.comment_editText)

        //Save button
        val saveButton = findViewById<Button>(R.id.save_button)

        saveButton.setOnClickListener {
            val serialNumberStr = serialNumber.text.toString()
            val commentStr = comment.text.toString()

            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)

            val inventoryItem = InventoryItem(
                0, // Provide the correct ID if you have it, or use 0 for new item
                currentYear,
                campus,
                building,
                room,
                item!!,
                barcode!!,
                serialNumberStr,
                commentStr,
                usersID
            )

            val dbHelper = DataBaseHandler(this)
            val itemId = dbHelper.insertInventoryItem(inventoryItem)

            // Check if the item was successfully inserted
            if (itemId != -1L) {
                intent = Intent(this, Add_New_Room_Activity4::class.java).apply {
                    putExtra("id", usersID)
                    putExtra("year", currentYear)
                    putExtra("firstName", usersFirstName)
                    putExtra("building", building)
                    putExtra("campus", campus)
                    putExtra("room", room)
                    putExtra("item", item)
                    putExtra("barcode", barcode)
                    putExtra("serialNumber", serialNumberStr)
                    putExtra("comment", commentStr)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to insert inventory item", Toast.LENGTH_SHORT).show()
            }
        }

        //Bottom Navigation bar access here
        bottomNavigation(usersID, usersFirstName)
    }

    fun bottomNavigation(usersID: Int, usersFirstName: String?) {
        //BOTTOM NAVIGATION BAR
        // Find and initialize bottomNav
        bottomNavigationView = findViewById(R.id.bottomNav)

        // Set up the selected item and listener for bottomNav
//        bottomNavigationView.setSelectedItemId(R.id.search)
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