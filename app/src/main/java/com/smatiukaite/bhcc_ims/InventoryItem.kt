package com.smatiukaite.bhcc_ims

import android.os.Parcel
import android.os.Parcelable

data class InventoryItem(
    val id: Int,
    val year: Int,
    val campus: String,
    val building: String,
    var room: String,
    val itemName: String,
    val barcode: String,
    val serialNumber: String,
    val comment: String,
    val creatorId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(year)
        parcel.writeString(campus)
        parcel.writeString(building)
        parcel.writeString(room)
        parcel.writeString(itemName)
        parcel.writeString(barcode)
        parcel.writeString(serialNumber)
        parcel.writeString(comment)
        parcel.writeInt(creatorId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InventoryItem> {
        override fun createFromParcel(parcel: Parcel): InventoryItem {
            return InventoryItem(parcel)
        }

        override fun newArray(size: Int): Array<InventoryItem?> {
            return arrayOfNulls(size)
        }
    }
}
