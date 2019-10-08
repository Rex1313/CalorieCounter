package com.example.caloriecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityViewModel
    lateinit var settingsFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        val splashScreenFragment = SplashScreenFragment.newInstance()
        settingsFragment = SettingsFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, splashScreenFragment)
            .commit()


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_settings -> {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, settingsFragment)
                .addToBackStack(null)
                .commit()
            true
        }


        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}
