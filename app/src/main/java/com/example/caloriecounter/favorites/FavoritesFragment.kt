package com.example.caloriecounter.favorites

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.caloriecounter.R
import com.example.caloriecounter.base.ResourceProvider
import com.example.caloriecounter.dayview.NewEntryDialogFragment
import kotlinx.android.synthetic.main.delete_confirmation_dialog.view.*
import kotlinx.android.synthetic.main.favorites_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private lateinit var viewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
        GlobalScope.launch {
            viewModel.getFavorites()
        }

        viewModel.favoritesLiveData.observe(this, Observer {
            context?.let { context ->
                val onOverflowClicked: (view: View, id: Int?) -> Unit = { view: View, id: Int? ->
                    val popupMenu = PopupMenu(context, view)
                    popupMenu.menuInflater.inflate(R.menu.food_card_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.menu_card_delete -> {
                                showDeleteDialog(context, id)
                                true
                            }
                            R.id.menu_card_edit -> {
                                NewFavouriteDialogFragment.newInstance(id)
                                    .show(childFragmentManager, "NewFavourite")


                                true
                            }
                            else -> false
                        }
                    }

                    popupMenu.show()
                }
                val onItemClicked: (Int?) -> Unit = {
                    showEditDialogFragment(it)
                }
                val onItemLongClicked: (Int?) -> Unit = {
                    showDeleteDialog(context, it)
                }
                val adapter =
                    FavoritesAdapter(it, onOverflowClicked, onItemClicked, onItemLongClicked)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = adapter


            }
        })

        edittext_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                GlobalScope.launch {
                    viewModel.filterFavorites(edittext_search.text.toString())
                }
            }
        })

        floating_action_button_add_favorite.setOnClickListener {
            NewFavouriteDialogFragment.newInstance(null)
                .show(childFragmentManager, "AddFavoriteDialogFragment")
        }
    }

    fun showDeleteDialog(context: Context, favouriteId: Int?) {
        val deleteDialogView = LayoutInflater.from(context)
            .inflate(R.layout.delete_confirmation_dialog, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(deleteDialogView)
            .setTitle(ResourceProvider.getString(R.string.delete_dialog_title))

        val deleteAlertDialog = dialogBuilder.show()

        deleteDialogView.dialog_delete.setOnClickListener {
            GlobalScope.launch {
                viewModel.deleteFavouriteById(favouriteId)
                viewModel.refreshData()
            }
            deleteAlertDialog.dismiss()

        }
        deleteDialogView.dialog_cancel.setOnClickListener {
            deleteAlertDialog.dismiss()
        }
    }

    fun showEditDialogFragment(id: Int?) {
        NewFavouriteDialogFragment.newInstance(id)
            .show(childFragmentManager, "EditFavourite")
    }
}
