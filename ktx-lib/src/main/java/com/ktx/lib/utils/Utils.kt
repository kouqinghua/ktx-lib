package com.ktx.lib.utils

import android.app.ActivityManager
import android.content.Context

class Utils {

    companion object{
        fun getActivityName(context: Context): String {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningTasks = manager.getRunningTasks(1)
            val component = runningTasks[0].topActivity
            return component!!.className
        }
    }
}