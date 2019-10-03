package com.example.caloriecounter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.caloriecounter.DayFragment
import org.joda.time.LocalDate


class DayFragmentViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {
    var lastPosition = 1
    var realPosition = 0

    override fun getItem(position: Int): Fragment {
        // return fragments.get(mapPagerPositionToModelPosition(position))
        return DayFragment.newInstance(LocalDate.now().minusDays(-realPosition).toString("YYYY-MM-dd"))
    }


    override fun getCount(): Int {
        return 5
    }
}
