package com.goazzi.workoutmanager.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.goazzi.workoutmanager.R
import com.goazzi.workoutmanager.adapter.CalenderPagerAdapter
import com.goazzi.workoutmanager.helper.Util
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CalenderViewPagerFragment : Fragment() {

    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var applicationContext: Context
    private lateinit var root: View
    private lateinit var viewPager: ViewPager2
    //viewModel

    companion object {
        private const val TAG = "EventFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentActivity = requireActivity()
        applicationContext = fragmentActivity.applicationContext
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_calender_view_pager, container, false)
        initViews(root)
        return root
    }

    private fun initViews(root: View) {
        viewPager = root.findViewById(R.id.view_pager)
        val adapter = CalenderPagerAdapter(childFragmentManager, fragmentActivity.lifecycle)
        viewPager.adapter = adapter
        val tabLayout = root.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = "OBJECT ${(position + 1)}"
            when (position) {
                0 -> tab.text = Util.getSpacedText("Calender")
                else -> tab.text = Util.getSpacedText("Events")
            }
        }.attach()
    }
}