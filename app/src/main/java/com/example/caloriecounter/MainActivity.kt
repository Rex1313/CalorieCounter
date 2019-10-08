package com.example.caloriecounter

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.example.caloriecounter.viewpager.DayFragmentViewPagerAdapter
import com.example.caloriecounter.viewpager.ViewPagerValues
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.LocalDate

class MainActivity : FragmentActivity() {

    lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

    }
}
