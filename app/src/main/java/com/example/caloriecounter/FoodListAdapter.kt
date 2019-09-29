package com.example.caloriecounter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.caloriecounter.database.Entry
import android.content.ClipData.Item
import android.widget.TextView
import com.example.caloriecounter.models.UIEntry


class FoodListAdapter(
    val context: Context?,
    val entries: List<UIEntry>
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


    fun bind(itemView: View?, entry: UIEntry) {
        itemView?.let {
            it.findViewById<TextView>(R.id.card_view_name).text = entry.name
            it.findViewById<TextView>(R.id.card_view_calories).text = entry.calories
        }
    }

    override fun getItem(position: Int): UIEntry? {
        return entries.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return entries.size
    }

    private val inflater: LayoutInflater =
        context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
}