package com.goazzi.workoutmanager.background

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.goazzi.workoutmanager.R
import com.goazzi.workoutmanager.helper.Constant
import com.goazzi.workoutmanager.view.activity.ExerciseActivity

class SilentForegroundService : Service() {
    companion object {
        private const val TAG = "SilentForegroundService"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")

        val notificationIntent = Intent().setClass(applicationContext, ExerciseActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(applicationContext, Constant.LOW_IMPORTANCE_CHANNEL_ID)
                .setContentTitle("Workout is Running")
                .setSmallIcon(R.drawable.dumbbell)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(Constant.DEFAULT_NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}