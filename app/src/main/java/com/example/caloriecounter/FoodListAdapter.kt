package com.example.caloriecounter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.caloriecounter.database.Entry
import android.content.ClipData.Item
import android.widget.TextView


class FoodListAdapter(
    val context: Context?,
    val entries: List<Entry>
) : BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.food_card, parent, false)
        }
        val entry = entries.get(position);
        bind(view, entry)
        return view!!;
    }


    fun bind(itemView: View?, entry: Entry) {
        itemView?.let {
            it.findViewById<TextView>(R.id.card_view_name).text = entry.entryName
            it.findViewById<TextView>(R.id.card_view_calories).text = entry.entryCalories.toString()
        }
    }

    override fun getItem(position: Int): Entry? {
        return entries.get(position)
    }

    override fun getItemId(position: Int): Long {
        return entries.get(position).id?.toLong()?:0
    }

    override fun getCount(): Int {
        return entries.size
    }

    private val inflater: LayoutInflater =
        context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
}