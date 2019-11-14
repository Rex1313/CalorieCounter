package com.example.caloriecounter.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.caloriecounter.R
import com.example.caloriecounter.models.EntryType
import com.example.caloriecounter.models.UIFavorite
import kotlinx.android.synthetic.main.food_card.view.*


class FavoritesAdapter(
    val entries: List<UIFavorite>,
    val onOverflowClicked: (view: View, id: Int?) -> Unit,
    val onItemClicked: (id: Int?) -> Unit,
    val onItemLongClicked: (id: Int?) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavoritesViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val entry: UIFavorite = entries[position]
        holder.bind(entry)
    }

    override fun getItemCount(): Int = entries.size

    inner class FavoritesViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
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

        fun bind(entry: UIFavorite) {
            calories?.text = entry.calories
            name?.text = entry.name
            menuIcon?.setOnClickListener {
                onOverflowClicked(it, entry.id)
            }
            itemView.setOnClickListener {
                onItemClicked(entry.id)
            }
            itemView.setOnLongClickListener {
                onItemLongClicked(entry.id)
                true
            }
            if (entry.entryType == EntryType.EXCERCISE.toString()) typeIcon.setImageResource(R.drawable.excercise_white)
            if (entry.entryType == EntryType.FOOD.toString()) typeIcon.setImageResource(R.drawable.calories_white)
        }

    }
}
