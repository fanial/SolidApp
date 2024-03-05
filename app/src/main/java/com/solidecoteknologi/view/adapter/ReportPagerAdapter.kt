package com.solidecoteknologi.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ReportPagerAdapter (fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    private val fragments: MutableList<Fragment> = ArrayList()
    private val fragmentTitles: MutableList<String> = ArrayList()

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        fragmentTitles.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitles[position]
    }
}