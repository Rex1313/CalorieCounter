package com.example.caloriecounter

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.Observer
import com.example.caloriecounter.base.onChange
import kotlinx.android.synthetic.main.import_export_confirmation_dialog.view.*
import java.util.*


class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsFragmentViewModel::class.java)
        context?.let { context ->
            GlobalScope.launch {
                viewModel.getData(context)
            }

            viewModel.uiModelLiveData.observe(this, Observer { uiModel ->
                text_input_daily_limit.setText(uiModel.calorieLimit)
                text_input_username.setText(uiModel.username)

            })
            text_input_daily_limit.onChange {
                if (it.isNotEmpty()) {
                    GlobalScope.launch {
                        viewModel.addDailySetting(it.toString())
                    }
                } else {
                    GlobalScope.launch {
                        viewModel.addDailySetting("0")
                    }
                }
            }

            text_input_username.onChange {
                viewModel.addUsername(it.toString(), context)

            }
            button_export.setOnClickListener {
                val dialogView = LayoutInflater.from(context)
                    .inflate(R.layout.import_export_confirmation_dialog, null)

                val dialogBuilder = AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setTitle("Export")

                val alertDialog = dialogBuilder.show()
                dialogView.text_view_dialog_text_import_export.text =
                    resources.getString(R.string.confirmation_text_export)
                dialogView.dialog_ok_import_export.setOnClickListener {
                    //TODO logic
                    alertDialog.dismiss()

                }
                dialogView.dialog_cancel_import_export.setOnClickListener {
                    alertDialog.dismiss()
                }

            }
            button_import.setOnClickListener {
                val dialogView = LayoutInflater.from(context)
                    .inflate(R.layout.import_export_confirmation_dialog, null)

                val dialogBuilder = AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setTitle("Import")

                val alertDialog = dialogBuilder.show()
                dialogView.text_view_dialog_text_import_export.text =
                    resources.getString(R.string.confirmation_text_import)
                dialogView.dialog_ok_import_export.setOnClickListener {
                    //TODO logic
                    alertDialog.dismiss()

                }
                dialogView.dialog_cancel_import_export.setOnClickListener {
                    alertDialog.dismiss()
                }

            }
        }

    }

}
