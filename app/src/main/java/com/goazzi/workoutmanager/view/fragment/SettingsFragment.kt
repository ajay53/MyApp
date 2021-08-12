package com.goazzi.workoutmanager.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.goazzi.workoutmanager.R

class SettingsFragment: Fragment() {
    companion object {
        private const val TAG = "SettingsFragment"
    }

    private lateinit var fragmentActivity: FragmentActivity
    private lateinit var applicationContext: Context

    //    private lateinit var viewModel: WorkoutViewModel
    private lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        fragmentActivity = requireActivity()
        applicationContext = fragmentActivity.applicationContext
//        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root: View = inflater.inflate(R.layout.fragment_settings, container, false)
        initViews(root)
        return root
    }

    private fun initViews(root: View) {

    }
}