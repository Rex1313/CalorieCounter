package com.example.caloriecounter.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.caloriecounter.R
import com.example.caloriecounter.base.format
import com.example.caloriecounter.base.onChange
import com.example.caloriecounter.dayview.DayFragmentViewModel
import com.example.caloriecounter.models.EntryTypeModel
import com.example.caloriecounter.models.UIFavorite
import kotlinx.android.synthetic.main.new_entry_fragment.*
import kotlinx.android.synthetic.main.new_favourite_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewFavouriteDialogFragment(val id: Int?) : DialogFragment() {

    lateinit var fragmentViewModel: FavoritesViewModel

    // do not access the views here they are not inflated yet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        parentFragment?.let {
            fragmentViewModel = ViewModelProviders.of(
                it
            ).get(FavoritesViewModel::class.java)
        }
        return inflater.inflate(R.layout.new_favourite_fragment, container, false)

    }


    // You can use this for accessing the views they will be iniflated here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        context?.let {


            val adapter = ArrayAdapter<EntryTypeModel>(
                it, android.R.layout.simple_spinner_dropdown_item, fragmentViewModel.entryTypes
            )
            spinner_category_favourite.adapter = adapter

            if (id != null) {
                text_view_add_new_favourite.setText(resources.getString(R.string.edit_entry));
                button_add_favourite.setText(resources.getString(R.string.save))

                GlobalScope.launch {
                    fragmentViewModel.getFavouriteById(id)
                }
                fragmentViewModel.favoriteLiveData.observe(this, Observer { favourite ->
                    text_input_calories_favourite.setText(favourite.calories.toDouble().format(0))
                    text_input_name_favourite.setText(favourite.name)
                    spinner_category_favourite.setSelection(
                        fragmentViewModel.getEntryTypePosition(
                            favourite.entryType
                        )
                    )
                })

            }
            button_cancel_favourite.setOnClickListener {
                dismiss()
            }
            button_add_favourite.setOnClickListener {
                if (text_input_calories_favourite.text.toString().isNotEmpty() && text_input_name_favourite.text.toString().isNotEmpty() && id != null) {
                    GlobalScope.launch {
                        fragmentViewModel.editFavouriteById(
                            id,
                            text_input_name_favourite.text.toString(),
                            text_input_calories_favourite.text.toString(),
                            (spinner_category_favourite.selectedItem as EntryTypeModel).type
                        )

                        fragmentViewModel.refreshData()
                    }
                    dismiss()
                } else if (id == null && text_input_calories_favourite.text.toString().isNotEmpty() && text_input_name_favourite.text.toString().isNotEmpty()) {

                    GlobalScope.launch {
                        fragmentViewModel.addFavorite(
                            text_input_name_favourite.text.toString(),
                            text_input_calories_favourite.text.toString(),
                            (spinner_category_favourite.selectedItem as EntryTypeModel).type
                        )
                        fragmentViewModel.refreshData()
                        dismiss()
                    }

                } else {
                    text_input_layout_calories_favourite.setError(resources.getString(R.string.value_empty_error))
                    text_input_layout_name_favourite.setError(resources.getString(R.string.value_empty_error))
                }

            }
            text_input_calories_favourite.onChange {
                if (it.isNotEmpty()) {
                    text_input_layout_calories_favourite.setError(null);
                }
            }
            text_input_name_favourite.onChange {
                if (it.isNotEmpty()) {
                    text_input_layout_name_favourite.setError(null);
                }
            }

            spinner_category_favourite
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int?) = NewFavouriteDialogFragment(id)
    }

}