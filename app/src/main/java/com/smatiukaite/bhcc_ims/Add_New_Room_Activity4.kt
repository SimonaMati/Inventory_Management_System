package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class Add_New_Room_Activity4 : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_room4)
        var intent = getIntent()
        var selectedItemValue = ""

        //Top page information update
        val campus = intent.getStringExtra("campus").toString()
        System.out.println("CAMPUS " + campus)

        val room = intent.getStringExtra("room").toString()
        System.out.println("ROOM " + room)

        val building = intent.getStringExtra("building").toString()
        System.out.println("BUILDING " + building)

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

        //Show total items in this room
       // val totalItemInThisRoom = findViewById<TextView>(R.id.total_items_in_this_room_textview)
        val dbHandler = DataBaseHandler(this)
        val itemCount = dbHandler.getTotalItemCount(campus.toString(), building, room)
        //totalItemInThisRoom.text = "Total Items: $itemCount"

        //ACCESS THE SPINNER
        //Item Spinner
        val itemSpinner: Spinner = findViewById(R.id.item_spinner)
        val addNewItem = findViewById<EditText>(R.id.add_new_item_button)

        ArrayAdapter.createFromResource(
            this, R.array.item_array, R.layout.spinner_item_layout
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            itemSpinner.adapter = adapter
        }

        itemSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItemValue = parent.getItemAtPosition(position).toString()

                //Lock EditText when the spinner is in use
                addNewItem.isEnabled = selectedItemValue == "Select an Item"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedItemValue = ""
            }
        }

        addNewItem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            //Lock itemSpinner when the EditText is in use
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Unlock spinner when the EditText is empty
                itemSpinner.isEnabled = s.isNullOrEmpty()
                selectedItemValue = s?.toString() ?: ""

                System.out.println("ITEM IS $selectedItemValue")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        //If user wants to scan a barcode
        var scanBarcodeButton = findViewById<Button>(R.id.start_scanning_button)
        scanBarcodeButton.setOnClickListener {
            intent = Intent(this, Add_New_Room_Activity2::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
                putExtra("building", building)
                putExtra("campus", campus)
                putExtra("room", room)
                putExtra("item", selectedItemValue)
            }
            startActivity(intent)
        }

//If user enters a barcode manually
        val addBarcodeManually = findViewById<EditText>(R.id.add_barcode_manually_editText)
        val nextButton = findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            val addBarcodeManuallyStr = addBarcodeManually.text.toString()

            if (addBarcodeManuallyStr.isEmpty()) {
                Toast.makeText(this, "No barcode scanned or typed", Toast.LENGTH_SHORT).show()
            } else {
                intent = Intent(this, Add_New_Room_Activity3::class.java).apply {
                    putExtra("id", usersID)
                    putExtra("firstName", usersFirstName)
                    putExtra("building", building)
                    putExtra("campus", campus)
                    putExtra("room", room)
                    putExtra("item", selectedItemValue)
                    putExtra("barcode", addBarcodeManuallyStr)
                }
                startActivity(intent)
            }
        }

        //See a list
        val viewListButton = findViewById<TextView>(R.id.view_list_button)
        viewListButton.setOnClickListener {
            intent = Intent(this, Search_Activity_3::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
                putExtra("building", building)
                putExtra("campus", campus)
                putExtra("room", room)
                val page = "addNewRoomActivity4"
                putExtra("page", page)
            }
            startActivity(intent)
        }

        //Print a list
        val printListButton = findViewById<TextView>(R.id.print_list_button)
        printListButton.setOnClickListener {

        }

        //Save a list and start with new inventory for another room
        val finishListButton = findViewById<Button>(R.id.save_list_button)
        finishListButton.setOnClickListener {
            intent = Intent(this, Add_New_Room_Activity::class.java).apply {
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