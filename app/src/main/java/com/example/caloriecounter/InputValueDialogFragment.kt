package com.example.caloriecounter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class InputValueDialogFragment(
    val inputType: String?,
    val hint: String,
    val oryginalValue: String?,
    val onSaveClicked:(newValue:String?) -> Unit

) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.input_value_dialog_fragment, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        @JvmStatic
        fun newInstance(
            inputType: String?,
            hint: String,
            oryginalValue: String?,
            onSaveClicked:(newValue:String?) -> Unit
        ) = InputValueDialogFragment(inputType, hint, oryginalValue,onSaveClicked)
    }
}
