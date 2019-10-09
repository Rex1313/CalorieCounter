package com.example.caloriecounter.dayview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.caloriecounter.R
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
            it.findViewById<TextView>(R.id.text_view_subtitle).text = entry.name
            it.findViewById<TextView>(R.id.text_view_title).text = "${entry.calories} kcal"
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