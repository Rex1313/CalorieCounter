package com.example.caloriecounter

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.example.caloriecounter.viewpager.DayFragmentViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : FragmentActivity() {

    lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)

        val adapter = DayFragmentViewPagerAdapter(supportFragmentManager)
        viewpager.adapter = adapter
        viewpager.setCurrentItem(1)
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
                if(position<adapter.lastPosition)
                {
                    adapter.realPosition = adapter.realPosition-1
                }else{
                    adapter.realPosition = adapter.realPosition+1
                }
            }

        })
        adapter.notifyDataSetChanged()

    }
}
