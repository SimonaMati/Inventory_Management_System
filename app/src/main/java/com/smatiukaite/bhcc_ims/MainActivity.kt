package com.smatiukaite.bhcc_ims

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smatiukaite.bhcc_ims.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

   // private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // bottomNavigationView = findViewById(R.id.bottomNav)
//        bottomNavigationView.setSelectedItemId(R.id.home)
//
//        bottomNavigationView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.search -> {
//                    startActivity(Intent(applicationContext, Search_Activity::class.java))
//                    overridePendingTransition(0, 0)
//                    true
//                }
//                R.id.home -> {
//                    true
//                }
//                R.id.statistics -> {
//                    startActivity(Intent(applicationContext, Search_Activity::class.java))
//                    overridePendingTransition(0, 0)
//                    true
//                }
//                R.id.campus_map -> {
//                    startActivity(Intent(applicationContext, Campus_Maps_Activity::class.java))
//                    overridePendingTransition(0, 0)
//                    true
//                }
//                R.id.inventory_lists -> {
//                    startActivity(Intent(applicationContext, Inventory_Lists_Activity::class.java))
//                    overridePendingTransition(0, 0)
//                    true
//                }
//                else -> {
//                    false
//                }
//            }
//        }
//
    }
}
