package com.example.caloriecounter

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.github.debop.kodatimes.now
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.Observer
import com.example.caloriecounter.base.onChange
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
        GlobalScope.launch {
            viewModel.getData(activity!!.applicationContext)
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
            }
            else {
                Toast.makeText(activity!!.applicationContext,"Limit cant be empty",Toast.LENGTH_SHORT).show()
                GlobalScope.launch {
                    viewModel.addDailySetting("0")
                }
            }
        }

        text_input_username.onChange {
            viewModel.addUsername(it.toString(), activity!!.applicationContext)

        }


    }

}
