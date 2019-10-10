package com.example.caloriecounter.dayview

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriecounter.R
import com.example.caloriecounter.models.EntryType
import com.example.caloriecounter.models.UIEntry
import kotlinx.android.synthetic.main.food_card.view.*


class FoodRecycleListAdapter(
    val entries: List<UIEntry>,
    val onOverflowClicked: (view: View, id: Int?) -> Unit,
    val onItemClicked: (id: Int?) -> Unit
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
        private var typeIcon: ImageView

        init {
            calories = itemView.text_view_title
            name = itemView.text_view_subtitle
            menuIcon = itemView.image_view_menu
            typeIcon = itemView.imageview_type


        }

        fun bind(entry: UIEntry) {
            calories?.text = entry.calories
            name?.text = entry.name
            menuIcon?.setOnClickListener {
                onOverflowClicked(it, entry.id)
            }
            itemView.setOnClickListener {
                onItemClicked(entry.id)
            }
            if (entry.entryType == EntryType.EXCERCISE.toString()) typeIcon.setImageResource(R.drawable.excercise)
            if (entry.entryType == EntryType.FOOD.toString()) typeIcon.setImageResource(R.drawable.calories)


        }

    }
}
