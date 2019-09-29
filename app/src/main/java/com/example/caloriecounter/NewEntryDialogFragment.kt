package com.example.caloriecounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.new_entry_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NewEntryDialogFragment() : DialogFragment() {

    lateinit var fragmentViewModel: DayFragmentViewModel

    // do not access the views here they are not inflated yet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        parentFragment?.let {
            fragmentViewModel = ViewModelProviders.of(
                it).get(DayFragmentViewModel::class.java)
        }
        return inflater.inflate(R.layout.new_entry_fragment, container, false)

    }


    // You can use this for accessing the views they will be iniflated here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
isCancelable=false

        button_cancel.setOnClickListener {
            dismiss()
        }
        button_add.setOnClickListener {
            fragmentViewModel.addNewEntry(text_input_calories.text.toString(),text_input_name.text.toString())
            GlobalScope.launch {
                fragmentViewModel.refreshData()
            }
            dismiss()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = NewEntryDialogFragment()
    }

}
