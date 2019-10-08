package com.example.caloriecounter

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders

class MainActivity : FragmentActivity() {

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        val splashScreenFragment= SplashScreenFragment.newInstance()

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, splashScreenFragment)
            .commit()
    }
}
