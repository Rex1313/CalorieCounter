package com.example.caloriecounter.dayview

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
import com.example.caloriecounter.database.Favourite
import com.example.caloriecounter.models.EntryTypeModel
import com.example.caloriecounter.models.UIFavorite
import kotlinx.android.synthetic.main.new_entry_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import com.example.caloriecounter.utils.WidgetUtils


class NewEntryDialogFragment(val id: Int?) : DialogFragment() {

    lateinit var fragmentViewModel: DayFragmentViewModel

    // do not access the views here they are not inflated yet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        parentFragment?.let {
            fragmentViewModel = ViewModelProviders.of(
                it
            ).get(DayFragmentViewModel::class.java)
        }
        return inflater.inflate(R.layout.new_entry_fragment, container, false)

    }


    // You can use this for accessing the views they will be iniflated here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        context?.let {

            fragmentViewModel.favoritesLiveData.observe(this, Observer {
                val autocompleteAdapter = ArrayAdapter<UIFavorite>(
                    context, android.R.layout.simple_spinner_dropdown_item, it
                )
                text_input_name.setThreshold(1)
                text_input_name.setAdapter(autocompleteAdapter)
                text_input_name.setOnItemClickListener(object : AdapterView.OnItemClickListener {
                    override fun onItemClick(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        val selectedItem = (parent?.adapter?.getItem(position) as UIFavorite)
                        text_input_calories.setText(selectedItem.calories.toString())
                        text_input_name.setText(selectedItem.name)
                    }

                })
            })


            val adapter = ArrayAdapter<EntryTypeModel>(
                it, android.R.layout.simple_spinner_dropdown_item, fragmentViewModel.entryTypes
            )
            spinner_category.adapter = adapter



            if (id != null) {
                text_view_add_new.setText(resources.getString(R.string.edit_entry));
                button_add.setText(resources.getString(R.string.save))

                GlobalScope.launch {
                    fragmentViewModel.getEntryById(id)
                }
                fragmentViewModel.entryLiveData.observe(this, Observer { entry ->
                    text_input_calories.setText(entry.entryValue.format(0))
                    text_input_name.setText(entry.entryName)
                    spinner_category.setSelection(fragmentViewModel.getEntryTypePosition(entry.entryType))
                })

            }
            button_cancel.setOnClickListener {
                dismiss()
            }
            button_add.setOnClickListener {
                if (text_input_calories.text.toString().isNotEmpty()) {
                    GlobalScope.launch {
                        if (checkbox_favourite.isChecked) {
                            fragmentViewModel.addNewFavorite(
                                text_input_calories.text.toString(),
                                text_input_name.text.toString(),
                                (spinner_category.selectedItem as EntryTypeModel).type.toString()
                            )
                        }


                        fragmentViewModel.addNewEntry(
                            id,
                            text_input_calories.text.toString(),
                            text_input_name.text.toString(),
                            (spinner_category.selectedItem as EntryTypeModel).type.toString()
                        )
                        fragmentViewModel.refreshData()
                        activity?.let {
                            WidgetUtils.requestUpdateSimpleWidgets(it.application)
                        }
                        dismiss()
                    }

                } else {
                    text_input_layout_calories.setError(resources.getString(R.string.value_empty_error));
                }
            }
            text_input_calories.onChange {
                if (it.isNotEmpty()) {
                    text_input_layout_calories.setError(null);
                }
            }


            spinner_category
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int?) = NewEntryDialogFragment(id)
    }

}
