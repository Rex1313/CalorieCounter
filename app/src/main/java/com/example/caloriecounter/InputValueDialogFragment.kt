package com.example.caloriecounter

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.caloriecounter.base.format
import com.example.caloriecounter.base.onChange
import com.example.caloriecounter.models.EntryTypeModel
import kotlinx.android.synthetic.main.input_value_dialog_fragment.*
import kotlinx.android.synthetic.main.input_value_dialog_fragment.view.*
import kotlinx.android.synthetic.main.new_entry_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InputValueDialogFragment(
    val inputType: Int,
    val hint: String,
    val oryginalValue: String,
    val onSaveClicked: (newValue: String) -> Unit

) : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.input_value_dialog_fragment, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        context?.let {
            text_input_layout_input_value.hint = hint
            text_input_input_value.inputType = inputType
            text_input_input_value.setText(oryginalValue)

            button_cancel_input_value.setOnClickListener {
                dismiss()
            }
            button_save_input_value.setOnClickListener {
                onSaveClicked(text_input_input_value.text.toString())
                dismiss()

            }


        }
    }

    companion object {
        @JvmStatic
        fun newInstance(
            inputType: Int,
            hint: String,
            oryginalValue: String,
            onSaveClicked: (newValue: String) -> Unit
        ) = InputValueDialogFragment(inputType, hint, oryginalValue, onSaveClicked)
    }
}
