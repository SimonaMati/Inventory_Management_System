package com.smatiukaite.bhcc_ims

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class InventoryListAdapter(context: Context, private var inventoryList: List<InventoryItem>) :
    ArrayAdapter<InventoryItem>(context, R.layout.list_item_layout, inventoryList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false)

            holder = ViewHolder()
            holder.yearTextView = view.findViewById(R.id.year_text)
            holder.campusTextView = view.findViewById(R.id.campus_text)
            holder.roomTextView = view.findViewById(R.id.room_text)

            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val item = getItem(position)

        holder.yearTextView.text = item?.year.toString()
        holder.campusTextView.text = item?.campus
        holder.roomTextView.text = item?.room

        return view!!
    }

    private class ViewHolder {
        lateinit var yearTextView: TextView
        lateinit var campusTextView: TextView
        lateinit var roomTextView: TextView
    }

    fun updateData(newInventoryList: List<InventoryItem>) {
        inventoryList = newInventoryList
        notifyDataSetChanged()
    }
}