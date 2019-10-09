package com.example.caloriecounter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.caloriecounter.dayview.DayFragment
import kotlinx.android.synthetic.main.splash_screen_fragment.*


class SplashScreenFragment : Fragment() {


    lateinit var viewmodel:SplashScreenFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewmodel = ViewModelProviders.of(this).get(SplashScreenFragmentViewModel::class.java)
        context?.let {
            if (viewmodel.getWelcomeScreenViewed(it)) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_daysFragment)
            }else{
                viewmodel.setWelcomeScreenViewed(it)
            }
        }
       return inflater.inflate(R.layout.splash_screen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()
        val dayFragment = DaysFragment.newInstance()
        button_welcome.setOnClickListener {
            findNavController().navigate(R.id.action_splashScreenFragment_to_daysFragment)
        }

    }

    companion object {
        fun newInstance() = SplashScreenFragment()
    }
}
