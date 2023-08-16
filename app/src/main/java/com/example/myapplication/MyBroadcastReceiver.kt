package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        var i = Intent(p0,AlarmOnActivity::class.java)
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (p0 != null) {
            p0.startActivity(i)
        }
    }
}