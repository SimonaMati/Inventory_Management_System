package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.ButtonBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text

class Add_New_Room_Activity2 : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_room2)
        var intent = getIntent()

        //Top page information update
        val item = intent.getStringExtra("item").toString()
//        val itemTextView = findViewById<TextView>(R.id.item_name_textview)
//        itemTextView.text = item

//        val itemNumber = findViewById<TextView>(R.id.item_number_textview)

        val building = intent.getStringExtra("building").toString()
        System.out.println("BUILDING " + building)

        val campus = intent.getStringExtra("campus").toString()
        System.out.println("CAMPUS " + campus)

        val room = intent.getStringExtra("room").toString()
        System.out.println("ROOM " + room)

        //User's name update
        val usersName = findViewById<TextView>(R.id.users_name)
        val usersID = intent.getIntExtra("id", 0)
        System.out.println("USER ID " + usersID)

        val usersFirstName = intent.getStringExtra("firstName")
        if (usersFirstName != null && usersFirstName.isNotEmpty()) {
            usersName.text = usersFirstName
        }

        //Scan barcode
        val scanBarcode = findViewById<SurfaceView>(R.id.cameraSurfaceView)
        val scannedBarcodeStr = ""

        //Show scanned barcode in the TextView
        val scannedBarcode = findViewById<TextView>(R.id.barcode_TextView)
        scannedBarcode.text = scannedBarcodeStr

        //Back button carrying information
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener {
            intent = Intent(this, Add_New_Room_Activity1::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
                putExtra("building", building)
                putExtra("campus", campus)
                putExtra("room", room)
                putExtra("item", item)
            }
            startActivity(intent)
        }

        //Save button
        val saveButton = findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener {
            intent = Intent(this, Add_New_Room_Activity3::class.java).apply {
                putExtra("id", usersID)
                putExtra("firstName", usersFirstName)
                putExtra("building", building)
                putExtra("campus", campus)
                putExtra("room", room)
                putExtra("item", item)
                putExtra("barcode", scannedBarcodeStr)
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