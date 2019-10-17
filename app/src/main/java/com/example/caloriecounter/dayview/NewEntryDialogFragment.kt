package com.example.caloriecounter.dayview

import android.content.res.Resources
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
import com.example.caloriecounter.models.EntryTypeModel
import kotlinx.android.synthetic.main.new_entry_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
                    text_input_calories.setText(entry.entryCalories.format(0))
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
                        fragmentViewModel.addNewEntry(
                            id,
                            text_input_calories.text.toString(),
                            text_input_name.text.toString(),
                            (spinner_category.selectedItem as EntryTypeModel).type.toString()
                        )
                        fragmentViewModel.refreshData()
                    }
                    dismiss()
                } else {
                    text_input_layout_calories.setError(resources.getString(R.string.calories_empty_error));
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
