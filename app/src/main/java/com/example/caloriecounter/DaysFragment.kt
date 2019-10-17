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
import androidx.viewpager.widget.ViewPager
import com.example.caloriecounter.MainActivityViewModel
import com.example.caloriecounter.R
import com.example.caloriecounter.dayview.viewpager.DayFragmentViewPagerAdapter
import com.example.caloriecounter.dayview.viewpager.ViewPagerValues
import kotlinx.android.synthetic.main.fragment_day.*
import kotlinx.android.synthetic.main.fragment_days.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.LocalDate

class DaysFragment : Fragment() {

    lateinit var activityViewModel: MainActivityViewModel

    lateinit var date: String

    // do not access the views here they are not inflated yet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityViewModel = ViewModelProviders.of(activity as FragmentActivity)
            .get(MainActivityViewModel::class.java)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_days, container, false)
    }


    // You can use this for accessing the views they will be iniflated here
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        loadAdapter(LocalDate.now())
        activityViewModel.loadData.observe(this, Observer {
            loadAdapter(LocalDate.parse(it))
        })
    }


    fun loadAdapter(date: LocalDate) {
        val adapter = DayFragmentViewPagerAdapter(childFragmentManager, date)
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

    companion object {
        @JvmStatic
        fun newInstance(): DaysFragment {
            return DaysFragment()
        }
    }

}
