package com.example.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.util.Calendar
import java.util.Locale


class MainActivity : AppCompatActivity() {

//    lateinit var datePicker : Button
    lateinit var tts  : TextToSpeech
    private lateinit var time : EditText
    private lateinit var set : Button
    private var hour: Int = 0
    private var minutes: Int = 0
    private var total :Long= 0
    private lateinit var timePicker: TimePicker
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        set = findViewById(R.id.SettingAlarm)
//        time = findViewById(R.id.Time)
        timePicker  = findViewById(R.id.timePicker1)
        var hr = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        var min = Calendar.getInstance().get(Calendar.MINUTE)

        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            hour = hourOfDay - hr
            minutes = minute - min
//            var timeee = System.currentTimeMillis()
//            var calc = ((hour * 60 + minutes) * 60 * 1000).toLong()
            total = (((hour * 60 + minutes) * 60 * 1000).toLong())

            set.setOnClickListener {


                if (total > 0) { // Ensure the alarm time is in the future
//                    val i = Intent(applicationContext, MyBroadcastReceiver::class.java)
                    val hoursGoing : Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    val secsGoing: Int = Calendar.getInstance().get(Calendar.SECOND)
                    val minsGoing : Int = Calendar.getInstance().get(Calendar.MINUTE)
                    hour = hourOfDay - hoursGoing
                    minutes=minute - minsGoing
                    total = (((hour * 60 + minutes) * 60 * 1000).toLong())

//                    Toast.makeText(applicationContext,"$total",Toast.LENGTH_SHORT).show()

//                    Toast.makeText(this, "$secsGoing", Toast.LENGTH_SHORT).show()

                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        val intent = Intent(this,AlarmOnActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slidedownenter,R.anim.slidedownexit);

                    },total-secsGoing*1000)
//                    val am: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + total, pi)
                    Toast.makeText(
                        applicationContext,
                        "Alarm is set for ${hour + hr}:${minutes + min}",
                        Toast.LENGTH_LONG
                    ).show()

                    var texxt = "Alarm is set for ${hour + hr}:${minutes + min}"
                    tts = TextToSpeech(applicationContext,TextToSpeech.OnInitListener {
                        if(it==TextToSpeech.SUCCESS){
                            tts.language= Locale.ENGLISH
                            tts.setSpeechRate(1.0f)
                            tts.speak(texxt.toString(),TextToSpeech.QUEUE_ADD,null)

                        }
                    })


                    val channelId = "Alarm_Clock"
                    val channelName = "Alarm_Clock"
//
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val importance = NotificationManager.IMPORTANCE_DEFAULT
                        val channel = NotificationChannel(channelId, channelName, importance)
                        val notificationManager = getSystemService(NotificationManager::class.java)
                        notificationManager.createNotificationChannel(channel)

                        // Create a notification
                        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.alarmlogo)
                            .setContentTitle("Alarm Clock Running")
                            .setContentText("Alarm is set for ${hour + hr}:${minutes + min} ")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)

// Post the notification
                        val notificationId = 1  // Unique ID for the notification
                        notificationManager.notify(notificationId, notificationBuilder.build())
                    }

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


//-------------------------------------------------------------------------------------------------------



