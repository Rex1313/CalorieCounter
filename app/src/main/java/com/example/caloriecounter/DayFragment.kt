package com.example.caloriecounter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_day.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DayFragment : Fragment() {

    lateinit var activityViewModel: MainActivityViewModel
    lateinit var fragmentViewModel: DayFragmentViewModel
    lateinit var date: String

    // do not access the views here they are not inflated yet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityViewModel = ViewModelProviders.of(activity as FragmentActivity)
            .get(MainActivityViewModel::class.java)



        fragmentViewModel =
            ViewModelProviders.of(this).get(DayFragmentViewModel::class.java)

        fragmentViewModel.dayDate = date
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day, container, false)
    }


    // You can use this for accessing the views they will be iniflated here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()

        GlobalScope.launch {
            fragmentViewModel.getData()
        }

        fragmentViewModel.uiModelLiveData.observe(this, Observer { uiModel ->


            val adapter = FoodListAdapter(activity?.baseContext, uiModel.entries)
            text_view_day.text = uiModel.dateDescription
            list_view_food.adapter = adapter
            text_view_calculation.text =
                uiModel.limit + '-' + uiModel.eatenCalories + '=' + uiModel.leftCalories
        })
        floating_action_button_add_meal.setOnClickListener {
            NewEntryDialogFragment.newInstance().show(childFragmentManager, "NewEntry")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): DayFragment {
            return DayFragment()
        }
    }

}
