package com.example.myapplication

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var time : EditText
    private lateinit var set : Button
    private var hour: Int = 0
    private var minutes: Int = 0
    private var total :Long= 0
    private lateinit var timePicker: TimePicker
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        set = findViewById(R.id.SettingAlarm)
        time = findViewById(R.id.Time)
        timePicker  = findViewById(R.id.timePicker1)
        var hr = timePicker.hour
        var min = timePicker.minute


        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            hour = hourOfDay - hr
            minutes = minute - min
            var timeee = System.currentTimeMillis()
//            var calc = ((hour * 60 + minutes) * 60 * 1000).toLong()
            total = (((hour * 60 + minutes) * 60 * 1000).toLong())
            set.setOnClickListener {
                Toast.makeText(applicationContext,"$total",Toast.LENGTH_SHORT).show()
                Toast.makeText(applicationContext,"$timeee",Toast.LENGTH_SHORT).show()
//
                if (total > 0) { // Ensure the alarm time is in the future
                    val i = Intent(applicationContext, MyBroadcastReceiver::class.java)
                    val pi = PendingIntent.getBroadcast(
                        applicationContext,
                        111,
                        i,
                        PendingIntent.FLAG_IMMUTABLE // Use FLAG_UPDATE_CURRENT here
                    )
                    val am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + total, pi)
                    Toast.makeText(
                        applicationContext,
                        "Alarm is set for ${hour + hr}:${minutes + min}",
                        Toast.LENGTH_LONG
                    ).show()
                    val channelId = "my_channel_id"
                    val channelName = "My Channel"
                    val importance = NotificationManager.IMPORTANCE_HIGH
                    val channel = NotificationChannel(channelId, channelName, importance)
                    val notificationManager = getSystemService(NotificationManager::class.java)
                    notificationManager.createNotificationChannel(channel)

                    // Create a notification
                    val notificationBuilder = NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.cooku)
                        .setContentTitle("Alarm Clock Running")
                        .setContentText("Alarm is set for ${hour + hr}:${minutes + min} ")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)

// Post the notification
                    val notificationId = 1  // Unique ID for the notification
                    notificationManager.notify(notificationId, notificationBuilder.build())

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please select a future time for the alarm",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }


        }
     }
}