package com.example.caloriecounter

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.example.caloriecounter.viewpager.DayFragmentViewPagerAdapter
import com.example.caloriecounter.viewpager.ViewPagerValues
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.LocalDate

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityViewModel
    lateinit var settingsFragment: Fragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        val adapter = DayFragmentViewPagerAdapter(supportFragmentManager, LocalDate.now())
        viewpager.adapter = adapter
        viewpager.setCurrentItem(ViewPagerValues.LIMIT_SWIPING_BACK)
        viewpager.offscreenPageLimit = 0
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                adapter.selectedPosition = position
            }

        })
        adapter.notifyDataSetChanged()
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
