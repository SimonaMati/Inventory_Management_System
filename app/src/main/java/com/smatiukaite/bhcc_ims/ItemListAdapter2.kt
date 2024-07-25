package com.smatiukaite.bhcc_ims

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ItemListAdapter2(context: Context, private val itemList: List<Item>) :
    ArrayAdapter<Item>(context, R.layout.list_item_layout3, itemList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_item_layout3, parent, false)

        val itemNameTextView = view.findViewById<TextView>(R.id.item_name_text)
        val barcodeTextView = view.findViewById<TextView>(R.id.barcode_text)
        val commentTextView = view.findViewById<TextView>(R.id.comment_text)

        val item = itemList[position]
        itemNameTextView.text = item.name
        barcodeTextView.text = item.barcode
        commentTextView.text = item.comment

        return view
    }
}