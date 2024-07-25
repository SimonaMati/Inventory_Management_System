package com.smatiukaite.bhcc_ims

data class Item(val name: String, val barcode: String, val comment: String) {
    override fun toString(): String {
        return "$name\t$barcode\t$comment"
    }
}
