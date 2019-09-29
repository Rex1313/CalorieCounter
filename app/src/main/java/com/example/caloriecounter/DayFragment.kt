package com.example.caloriecounter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.caloriecounter.viewmodel.DayFragmentViewModelFactory
import kotlinx.android.synthetic.main.fragment_day.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DayFragment : Fragment() {

    lateinit var activityViewModel: MainActivityViewModel
    lateinit var fragmentViewModel: DayFragmentViewModel

    // do not access the views here they are not inflated yet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityViewModel = ViewModelProviders.of(activity as FragmentActivity)
            .get(MainActivityViewModel::class.java)

        val dayFragmentFactory = DayFragmentViewModelFactory("2012-12-12")



        fragmentViewModel =
            ViewModelProviders.of(this).get(DayFragmentViewModel::class.java)
        fragmentViewModel.dayDate="1220-12-12";
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day, container, false)
    }


    // You can use this for accessing the views they will be iniflated here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("DayFragment: Get Entries taken from db again...")
        GlobalScope.launch {
            fragmentViewModel.getData()
        }

        fragmentViewModel.uiModelLiveData.observe(this, Observer { uiModel ->
            val adapter = FoodListAdapter(activity?.baseContext, uiModel.entries)
            list_view_food.adapter = adapter
            text_view_calculation.text =
                uiModel.limit.toInt().toString() + '-' + uiModel.eatenCalories.toInt().toString() + '=' + uiModel.leftCalories.toInt().toString()
        })
        floating_action_button_add_meal.setOnClickListener { NewEntryDialogFragment.newInstance().show(childFragmentManager, "NewEntry") }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DayFragment()
    }

}
