package com.example.caloriecounter

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.InputType
import android.text.format.DateUtils
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
import com.example.caloriecounter.dayview.NewEntryDialogFragment
import com.example.caloriecounter.utils.WidgetUtils
import kotlinx.android.synthetic.main.import_export_confirmation_dialog.view.*
import kotlinx.android.synthetic.main.settings_fragment.view.*
import org.joda.time.LocalDate


class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsFragmentViewModel
    private lateinit var activityViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsFragmentViewModel::class.java)
        activity?.let {
            activityViewModel = ViewModelProviders.of(it).get(MainActivityViewModel::class.java)
        }
        context?.let { context ->
            GlobalScope.launch {
                viewModel.getData(context)
            }

            viewModel.uiModelLiveData.observe(this, Observer { uiModel ->
                text_input_daily_limit.setText(uiModel.calorieLimit)
                text_input_username.setText(uiModel.username)

            })
            text_input_daily_limit.setOnClickListener {
                val onSaveClicked: (String) -> Unit = {
                    if (it.isNotEmpty()) {
                        GlobalScope.launch {
                            viewModel.addDailySetting(it)
                            viewModel.getData(context)
                        }
                    } else {
                        GlobalScope.launch {
                            viewModel.addDailySetting("0")
                            viewModel.getData(context)
                        }
                    }
                    activity?.let {
                        WidgetUtils.requestUpdateSimpleWidgets(it.application)
                    }

                }
                InputValueDialogFragment.newInstance(
                    InputType.TYPE_CLASS_NUMBER,
                    resources.getString(R.string.settings_calorie_limit),
                    resources.getString(R.string.change_settings_title),
                    it.text_input_daily_limit.text.toString(),
                    onSaveClicked
                )
                    .show(childFragmentManager, "SetCalories")

            }

            text_input_username.setOnClickListener {
                val onSaveClicked: (String) -> Unit = {
                    viewModel.addUsername(it, context)
                    GlobalScope.launch {
                        viewModel.getData(context)
                    }
                }

                InputValueDialogFragment.newInstance(
                    InputType.TYPE_CLASS_TEXT,
                    resources.getString(R.string.settings_username),
                    resources.getString(R.string.change_settings_title),
                    it.text_input_username.text.toString(),
                    onSaveClicked
                )
                    .show(childFragmentManager, "SetUsername")
            }

            button_export.setOnClickListener {
                val dialogView = LayoutInflater.from(context)
                    .inflate(R.layout.import_export_confirmation_dialog, null)

                val dialogBuilder = AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setTitle(resources.getString(R.string.export))

                val alertDialog = dialogBuilder.show()
                dialogView.text_view_dialog_text_import_export.text =
                    resources.getString(R.string.confirmation_text_export)
                dialogView.dialog_ok_import_export.setOnClickListener {
                    GlobalScope.launch {
                        viewModel.exportDataToCSV()
                    }
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
                    .setTitle(resources.getString(R.string.import_data))

                val alertDialog = dialogBuilder.show()
                dialogView.text_view_dialog_text_import_export.text =
                    resources.getString(R.string.confirmation_text_import)
                dialogView.dialog_ok_import_export.setOnClickListener {
                    GlobalScope.launch {
                        viewModel.importDataToCSV()
                        activityViewModel.refreshDataWithDate(LocalDate.now().toString(com.example.caloriecounter.utils.DateUtils.DB_DATE_FORMAT))
                        viewModel.getData(context)
                    }

                    alertDialog.dismiss()

                }
                dialogView.dialog_cancel_import_export.setOnClickListener {
                    alertDialog.dismiss()
                }

            }
        }

    }

}
