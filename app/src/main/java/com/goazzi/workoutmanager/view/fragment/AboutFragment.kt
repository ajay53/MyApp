package com.goazzi.workoutmanager.view.fragment

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
}