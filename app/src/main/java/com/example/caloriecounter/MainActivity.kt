package com.example.caloriecounter

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.caloriecounter.utils.WidgetUtils


class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityViewModel
    lateinit var settingsFragment: Fragment
    lateinit var navHost: NavHostFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        setSupportActionBar(findViewById(R.id.toolbar))
        NavigationUI.setupActionBarWithNavController(this, navHost.navController)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        supportActionBar?.setTitle(R.string.toolbar_main_title)

    }


    override fun onResume() {
        super.onResume()
        val widgetExtra = intent?.extras?.get(WidgetUtils.PENDING_INTENT_START_ACTIVITY)
        widgetExtra?.let {
            viewModel.showNewEntryFromWidget()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_settings -> {
            navHost.navController.navigate(R.id.action_settings)
            true
        }
        R.id.action_favorites -> {
            navHost.navController.navigate(R.id.favoritesFragment)
            true

        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}
