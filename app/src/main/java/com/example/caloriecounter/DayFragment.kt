package com.example.caloriecounter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders

class DayFragment : Fragment() {

    lateinit var activityViewModel:MainActivityViewModel
    lateinit var fragmentViewModel:DayFragmentViewModel

    // do not access the views here they are not inflated yet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       activityViewModel = ViewModelProviders.of(activity as FragmentActivity).get(MainActivityViewModel::class.java)
       fragmentViewModel = ViewModelProviders.of(this).get(DayFragmentViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day, container, false)
    }


    // You can use this for accessing the views they will be iniflated here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DayFragment()
    }

}
