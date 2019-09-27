package com.example.caloriecounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.new_entry_fragment.*


class NewEntryDialog() : Fragment() {


    // do not access the views here they are not inflated yet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.new_entry_fragment, container, false)

    }


    // You can use this for accessing the views they will be iniflated here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        button_cancel.setOnClickListener {

        }
        button_add.setOnClickListener {


        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = NewEntryDialog()
    }

}
