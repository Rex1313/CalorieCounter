package com.example.caloriecounter

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.debop.kodatimes.now
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

        button_settings_add.setOnClickListener {
            GlobalScope.launch {
                viewModel.addDailySetting(text_input_daily_limit.text.toString())
            }
            activity!!.supportFragmentManager.popBackStack()

        }
        button_settings_cancel.setOnClickListener{
            activity!!.supportFragmentManager.popBackStack()
        }
    }

}
