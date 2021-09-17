package com.goazzi.workoutmanager.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.goazzi.workoutmanager.view.fragment.CalenderFragment
import com.goazzi.workoutmanager.view.fragment.StatsFragment

class CalenderPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CalenderFragment()
            else -> StatsFragment()
        }
    }

}