package com.smatiukaite.bhcc_ims

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.smatiukaite.bhcc_ims.User
import java.util.*

val DATABASE_NAME = "MyDB"
val TABLE_NAME = "Users"
val COL_ID = "id"
val COL_NAME = "first_name"
val COL_LAST_NAME = "last_name"
val COL_EMAIL = "email"
val COL_PASSWORD = "password"
private var currentUser: User? = null

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " VARCHAR(256), " +
                COL_LAST_NAME + " VARCHAR(256), " +
                COL_EMAIL + " VARCHAR(256), " +
                COL_PASSWORD + " VARCHAR(256))"

        val createInventoryTable = "CREATE TABLE Inventory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "creatorId INTEGER, " +
                "year INTEGER, " +
                "campus VARCHAR(256), " +
                "building VARCHAR(256), " +
                "room INTEGER, " +
                "itemName VARCHAR(256), " +
                "barcode VARCHAR(256), " +
                "serialNumber VARCHAR(256), " +
                "comment VARCHAR(256))"

        if (db != null) {
            db.execSQL(createTable)
            db.execSQL(createInventoryTable)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertData(user: User) {
        val db = this.writableDatabase

        // Check if email already exists
        val emailCursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $COL_EMAIL = ?", arrayOf(user.email)
        )
        if (emailCursor.count > 0) {
            // Email already exists
            Toast.makeText(context, "Email already in use", Toast.LENGTH_SHORT).show()
        } else {
            // User name is available, insert the new user
            val cv = ContentValues()
            cv.put(COL_NAME, user.firstName)
            cv.put(COL_LAST_NAME, user.lastName)
            cv.put(COL_EMAIL, user.email)

            // Verify that the password matches the repeated password
            if (user.retrieveFinalPassword() == "") {
                throw IllegalArgumentException("Password cannot be empty")
            } else if (user.retrieveFinalPassword() != user.retrieveFinalPassword()) {
                throw IllegalArgumentException("Passwords do not match")
            }

            // Store the password in the database
            cv.put(COL_PASSWORD, user.retrieveFinalPassword())

            val result = db.insert(TABLE_NAME, null, cv)

            if (result == -1L) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Account was created!", Toast.LENGTH_SHORT).show()
            }
        }
        db.close()
    }

    fun getUser(email: String, password: String): User? {
        val db = this.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $COL_EMAIL = ?",
            arrayOf(email)
        )
        val user = if (cursor.moveToFirst()) {
            User(
                cursor.getString(cursor.getColumnIndex(COL_NAME)),
                cursor.getString(cursor.getColumnIndex(COL_LAST_NAME)),
                cursor.getString(cursor.getColumnIndex(COL_EMAIL)),
                "",
                ""
            ).also { user ->
                user.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                user.finalPassword = cursor.getString(cursor.getColumnIndex(COL_PASSWORD))
            }
        } else {
            null
        }
        db.close()

        if (user != null && user.retrieveFinalPassword() == password) {
            currentUser = user  // Set the current user
            return user
        } else {
            return null
        }
    }

    fun logout() {
        currentUser = null
    }

    fun updateUser(user: User) {
        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(COL_NAME, user.firstName)
        cv.put(COL_LAST_NAME, user.lastName)
        cv.put(COL_PASSWORD, user.retrieveFinalPassword()) // Update the password field

        // Update the user's record in the database
        val result = db.update(
            TABLE_NAME,
            cv,
            "$COL_EMAIL = ?",
            arrayOf(user.email)
        )

        if (result == -1) {
            Toast.makeText(context, "Failed to update user information", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Information updated", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }


    fun insertInventoryItem(item: InventoryItem): Long {
        val existingItem = getInventoryItemById(item.id)
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("creatorId", item.creatorId)

        // Add the current year
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        cv.put("year", item.year)

        cv.put("campus", item.campus)
        cv.put("building", item.building)
        cv.put("room", item.room)
        cv.put("itemName", item.itemName)
        cv.put("barcode", item.barcode)
        cv.put("serialNumber", item.serialNumber)
        cv.put("comment", item.comment)

        val itemId: Long

        if (existingItem != null) {
            // Item already exists, update it
            val result = db.update(
                "Inventory", cv, "id = ?", arrayOf(item.id.toString())
            )
            if (result == -1) {
                Toast.makeText(context, "Failed to update inventory item", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Inventory item updated successfully", Toast.LENGTH_SHORT).show()
            }
            itemId = item.id.toLong()
        } else {
            // Item doesn't exist, insert it
            val result = db.insert("Inventory", null, cv)
            if (result == -1L) {
                Toast.makeText(context, "Failed to insert inventory item", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Inventory item inserted successfully", Toast.LENGTH_SHORT).show()
            }
            itemId = result
        }
        db.close()
        return itemId
    }



    fun getInventoryItemById(itemId: Int): InventoryItem? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Inventory WHERE id = ?", arrayOf(itemId.toString()))
        val inventoryItem: InventoryItem? = if (cursor.moveToFirst()) {
            InventoryItem(
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getInt(cursor.getColumnIndex("year")),
                cursor.getString(cursor.getColumnIndex("campus")),
                cursor.getString(cursor.getColumnIndex("building")),
                cursor.getString(cursor.getColumnIndex("room")),
                cursor.getString(cursor.getColumnIndex("itemName")),
                cursor.getString(cursor.getColumnIndex("barcode")),
                cursor.getString(cursor.getColumnIndex("serialNumber")),
                cursor.getString(cursor.getColumnIndex("comment")),
                cursor.getInt(cursor.getColumnIndex("creatorId"))
            )
        } else {
            null
        }
        cursor.close()
        db.close()
        return inventoryItem
    }

    fun getTotalItemCount(campus: String, building: String, room: String?): Int {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM Inventory WHERE year = ? AND campus = ? AND building = ? AND room = ?"
        val roomValue = room ?: "" // If room is null, use an empty string as the value
        val cursor = db.rawQuery(query, arrayOf(campus, building, roomValue))
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }

    fun deleteInventoryItem(itemId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete("Inventory", "id = ?", arrayOf(itemId.toString()))
        db.close()
        return result != -1
    }

    fun deleteInventoryItems(building: String, room: String, campus: String): Boolean {
        val db = this.writableDatabase
        val whereClause = "building = ? AND room = ? AND campus = ?"
        val whereArgs = arrayOf(building, room, campus)
        val result = db.delete("Inventory", whereClause, whereArgs)
        db.close()
        return result != -1
    }

    fun getAllItems(): List<Item> {
        val itemList = mutableListOf<Item>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT itemName, barcode, comment FROM Inventory", null)

        if (cursor.moveToFirst()) {
            do {
                val itemName = cursor.getString(cursor.getColumnIndex("itemName"))
                val barcode = cursor.getString(cursor.getColumnIndex("barcode"))
                val comment = cursor.getString(cursor.getColumnIndex("comment"))
                val item = Item(itemName, barcode, comment)
                itemList.add(item)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return itemList
    }

    fun getListItems(building: String, room: String): List<Item> {
        val itemList = mutableListOf<Item>()
        val db = readableDatabase

        val query = "SELECT itemName, barcode, comment FROM Inventory WHERE building = ? AND room = ?"
        val cursor = db.rawQuery(query, arrayOf(building, room))

        if (cursor.moveToFirst()) {
            do {
                val itemName = cursor.getString(cursor.getColumnIndex("itemName"))
                val barcode = cursor.getString(cursor.getColumnIndex("barcode"))
                val comment = cursor.getString(cursor.getColumnIndex("comment"))
                val item = Item(itemName, barcode, comment)
                itemList.add(item)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return itemList
    }

    fun getInventoryItemsByUserId(userId: Int): List<InventoryItem> {
        val inventoryItems = mutableListOf<InventoryItem>()
        val db = readableDatabase

        val query = "SELECT * FROM Inventory WHERE creatorId = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val year = cursor.getInt(cursor.getColumnIndex("year"))
                val campus = cursor.getString(cursor.getColumnIndex("campus"))
                val building = cursor.getString(cursor.getColumnIndex("building"))
                val room = cursor.getString(cursor.getColumnIndex("room"))
                val itemName = cursor.getString(cursor.getColumnIndex("itemName"))
                val barcode = cursor.getString(cursor.getColumnIndex("barcode"))
                val serialNumber = cursor.getString(cursor.getColumnIndex("serialNumber"))
                val comment = cursor.getString(cursor.getColumnIndex("comment"))
                val creatorId = cursor.getInt(cursor.getColumnIndex("creatorId"))

                val inventoryItem = InventoryItem(
                    id, year, campus, building, room, itemName, barcode,
                    serialNumber, comment, creatorId
                )
                inventoryItems.add(inventoryItem)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return inventoryItems
    }

    fun getTotalRoomCount(): Int {
        val db = this.readableDatabase
        val query = "SELECT COUNT(DISTINCT room) FROM Inventory"
        val cursor = db.rawQuery(query, null)
        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count
    }


}