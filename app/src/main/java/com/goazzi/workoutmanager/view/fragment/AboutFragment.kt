package com.goazzi.workoutmanager.view.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.goazzi.workoutmanager.BuildConfig
import com.goazzi.workoutmanager.R
import com.goazzi.workoutmanager.helper.Util

class AboutFragment : Fragment(), View.OnClickListener {

    companion object {
        private const val TAG = "AboutFragment"
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_about, container, false)
        initViews(root)
        return root
    }

    private fun initViews(root: View) {
        val txtEmail = root.findViewById<TextView>(R.id.txt_email)
        txtEmail.setOnClickListener(this)
        val txtPlayStore = root.findViewById<TextView>(R.id.txt_play_store)
        txtPlayStore.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txt_play_store -> {
                openAppRating(applicationContext)
            }
            R.id.txt_email -> {
                val packageManager = fragmentActivity.packageManager
                val intent = Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:"))
                        .putExtra(Intent.EXTRA_EMAIL, arrayOf("ajayst53@gmail.com"))
                        .putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else{
                    Util.showSnackBar(root, "No apps found to send email.")
                }
            }
        }
    }

    private fun openAppRating(context: Context) {
        // you can also use BuildConfig.APPLICATION_ID
//        val appId = "com.reddit.frontpage"
        val appId = BuildConfig.APPLICATION_ID
//        val appId = context.packageName
        val rateIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appId"))
        var marketFound = false

        // find all applications able to handle our rateIntent
        val otherApps = context.packageManager.queryIntentActivities(rateIntent, 0)
        for (otherApp in otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName == "com.android.vending") {
                val otherAppActivity = otherApp.activityInfo
                val componentName = ComponentName(otherAppActivity.applicationInfo.packageName, otherAppActivity.name)
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // task reParenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.component = componentName
                context.startActivity(rateIntent)
                marketFound = true
                break
            }
        }

        // if GP not present on device, open web browser
        /*if (!marketFound) {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appId"))
            context.startActivity(webIntent)
        }*/
    }
}