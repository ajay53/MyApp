package com.goazzi.workoutmanager.view.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.goazzi.workoutmanager.R
import com.goazzi.workoutmanager.helper.Util

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
//        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_workout, R.id.navigation_calender, R.id.navigation_about))
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_workout, R.id.navigation_about))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.hide()

        Util.createSilentNotificationChannel(applicationContext)
    }

    override fun onBackPressed() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val selectedItem = navView.selectedItemId
        if (R.id.navigation_workout != selectedItem) {
            navView.selectedItemId = R.id.navigation_workout
        } else {
            super.onBackPressed()
        }
    }
}