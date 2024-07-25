package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class Add_New_Room_Activity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_room)

        var intent = getIntent()
        var selectedCampusValue = ""
        var selectedBuildingValue = ""

        //User's name update
        val usersName = findViewById<TextView>(R.id.users_name)
        val usersID = intent.getIntExtra("id", 0)
        System.out.println("USER ID " + usersID)

        val usersFirstName = intent.getStringExtra("firstName")
        if (usersFirstName != null && usersFirstName.isNotEmpty()) {
            usersName.text = usersFirstName
        }

        //ACCESS THE SPINNERS
        //Campus Spinner
        val campusSpinner: Spinner = findViewById(R.id.campus_spinner)

        ArrayAdapter.createFromResource(
            this, R.array.campus_array, R.layout.spinner_item_layout
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            campusSpinner.adapter = adapter
        }

        campusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedCampusValue = parent.getItemAtPosition(position).toString()
                System.out.print("CAMPUS " + selectedCampusValue)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedCampusValue = ""
            }
        }

        //Building Spinner
        val buildingSpinner: Spinner = findViewById(R.id.building_spinner)

        ArrayAdapter.createFromResource(
            this, R.array.building_array, R.layout.spinner_item_layout
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            buildingSpinner.adapter = adapter
        }

        buildingSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedBuildingValue = parent.getItemAtPosition(position).toString()
                System.out.print("CAMPUS " + selectedBuildingValue)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedBuildingValue = ""
            }
        }

        //ENTER A ROOM
        var roomName = findViewById<EditText>(R.id.enter_room_name_edit_text)


        //Go to the next activity -> Add_New_Room_Activity1
        val nextButton = findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            val roomNameStr = roomName.text.toString()
            System.out.println("ROOM 1" + roomNameStr)

            if (selectedCampusValue != "Select a Campus"
                && selectedBuildingValue != "Select a Building"
                && roomNameStr.isNotEmpty()
            ) {
                intent = Intent(this, Add_New_Room_Activity1::class.java).apply {
                    putExtra("id", usersID)
                    putExtra("firstName", usersFirstName)
                    putExtra("campus", selectedCampusValue)
                    putExtra("building", selectedBuildingValue)
                    putExtra("room", roomNameStr)
                }
                startActivity(intent)
            }else{
                Toast.makeText(this, "Select campus, building or add a room name", Toast.LENGTH_SHORT).show()
            }
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