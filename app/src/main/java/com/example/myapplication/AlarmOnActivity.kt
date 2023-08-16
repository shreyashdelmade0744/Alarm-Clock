package com.example.myapplication

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AlarmOnActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_on)

        val mp = MediaPlayer.create(applicationContext, R.raw.alarm)
        mp.start()
        var button : Button = findViewById(R.id.stopbutton)

        button.setOnClickListener {
            mp.stop()
            this.finish()
        }



    }
}