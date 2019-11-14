package com.example.caloriecounter.favorites

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
                val onOverflowClicked: (View, Int?) -> Unit = { view, position -> }
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
