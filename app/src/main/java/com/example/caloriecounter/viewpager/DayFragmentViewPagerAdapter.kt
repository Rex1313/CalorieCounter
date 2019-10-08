package com.example.caloriecounter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.caloriecounter.DayFragment
import org.joda.time.LocalDate


class DayFragmentViewPagerAdapter(fragmentManager: FragmentManager,val startDate:LocalDate) :
    FragmentStatePagerAdapter(fragmentManager) {
    var selectedPosition = 1
    var lastPosition = 1


    val fragments = mutableListOf(DayFragment.newInstance(),
        DayFragment.newInstance(),
        DayFragment.newInstance(),
        DayFragment.newInstance())

    override fun getItem(position: Int): Fragment {
        // return fragments.get(mapPagerPositionToModelPosition(position))

        val listPos = position%fragments.size
        val fragment = fragments.get(listPos)
        fragment.date = startDate.minusDays(ViewPagerValues.LIMIT_SWIPING_BACK).minusDays(-position).toString("YYYY-MM-dd")
        return fragment
    }


    override fun getCount(): Int {
        return Int.MAX_VALUE
    }
}
