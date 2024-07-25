package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text

class Add_New_Room_Activity1 : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    // Declare and initialize itemList ArrayList
    private lateinit var itemList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_room1)
        var intent = getIntent()
        var selectedItemValue = ""

//        // Initialize itemList after setContentView
//        itemList = ArrayList(resources.getStringArray(R.array.item_array).toList())

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

        // Initialize itemList after setContentView
        itemList = ArrayList()
        if (!itemList.contains(room)) {
            itemList.add(room?:"")
        }
        itemList.addAll(resources.getStringArray(R.array.item_array).toList())


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
      //  val totalItemInThisRoom = findViewById<TextView>(R.id.total_items_in_this_room_textview)
        val dbHandler = DataBaseHandler(this)
        val itemCount = dbHandler.getTotalItemCount(campus.toString(), building, room)
     //   totalItemInThisRoom.text = "Total Items: $itemCount"

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
        // Initialize itemList after setContentView
        if (!::itemList.isInitialized) {
            itemList = ArrayList()
            itemList.addAll(resources.getStringArray(R.array.item_array).toList())
        }

        addNewItem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedItemValue = s.toString()

                // Check if the entered item is not in the item list
                if (!itemList.contains(selectedItemValue) && selectedItemValue.isNotEmpty()) {
                    // Add the new item to the list
                    itemList.add(selectedItemValue)

                    // Update the spinner adapter with the new item list
                    val adapter =
                        ArrayAdapter(
                            this@Add_New_Room_Activity1,
                            android.R.layout.simple_spinner_item,
                            itemList
                        )
                    itemSpinner.adapter = adapter
                }

                System.out.println("ITEM IS $selectedItemValue")
            }

            override fun afterTextChanged(s: Editable?) {
                // Do nothing
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
        val addBarcodeManually = findViewById<EditText>(R.id.add_barcode_manually_button)
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