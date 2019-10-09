package com.example.caloriecounter.dayview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriecounter.R
import com.example.caloriecounter.models.UIEntry


class FoodRecycleListAdapter(
    val entries: List<UIEntry>,
    val myOverflowClicked: (view: View, id: Int?) -> Unit
) : RecyclerView.Adapter<FoodRecycleListAdapter.FoodCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodCardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodCardViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: FoodCardViewHolder, position: Int) {
        val entry: UIEntry = entries[position]
        holder.bind(entry)
    }

    override fun getItemCount(): Int = entries.size

    inner class FoodCardViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.food_card, parent, false)) {
        private var calories: TextView? = null
        private var name: TextView? = null
        private var menuIcon: ImageView? = null

        init {
            calories = itemView.findViewById(R.id.text_view_subtitle)
            name = itemView.findViewById(R.id.text_view_title)
            menuIcon = itemView.findViewById(R.id.image_view_menu)

        }

        fun bind(entry: UIEntry) {
            calories?.text = entry.calories
            name?.text = entry.name
            menuIcon?.setOnClickListener {
                myOverflowClicked(itemView, entry.id)
            }

        }

    }
}
