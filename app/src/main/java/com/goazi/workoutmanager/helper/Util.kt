package com.goazi.workoutmanager.helper

import android.view.View
import com.goazi.workoutmanager.model.Session
import com.google.android.material.snackbar.Snackbar

class Util {
    companion object {
        private const val TAG = "Util"

        fun showSnackBar(view: View, message: String) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }

        fun getSecondsInString(long: Long): String {
//            return "asdbksj"
            return (long / 1000).toString()
        }
    }

    interface WorkOnClick {
        fun onWorkClicked(view: View, session: Session)
    }

    interface RestOnClick {
        fun onRestClicked(view: View, session: Session)
    }
}