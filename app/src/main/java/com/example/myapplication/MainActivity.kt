package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var time : EditText
    private lateinit var set : Button
    private var hour: Int = 0
    private var minutes: Int = 0
    private var total :Long= 0
    private lateinit var timePicker: TimePicker
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
//                Toast.makeText(applicationContext,"$calc",Toast.LENGTH_SHORT).show()
//            var timetoset = time.text.toString().toInt()
//            time.text = timePicker.hour()
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