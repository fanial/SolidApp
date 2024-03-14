package com.solidecoteknologi.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.solidecoteknologi.view.DailyReportFragment
import com.solidecoteknologi.view.MonthlyReportFragment

class ReportPagerAdapter (fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment = Fragment()
        when(position){
            0 -> fragment = DailyReportFragment()
            1 -> fragment = MonthlyReportFragment()
        }
        return fragment
    }
}