package com.example.caloriecounter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.caloriecounter.database.Entry
import android.content.ClipData.Item


class FoodListAdapter(
     val context: Context,
     val entries: ArrayList<Entry>
) : BaseAdapter() {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val foodCard = inflater.inflate(R.layout.food_card, parent, false)

            return foodCard;
    }

    override fun getItem(position: Int): Entry? {
        return entries.get(position)
    }

    override fun getItemId(position: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        return entries.count()
    }

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
}