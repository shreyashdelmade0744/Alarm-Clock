package com.example.myapplication

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class AlarmOnActivity : AppCompatActivity() , SensorEventListener {



    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor ?= null
    private var buttonPressed = false
    private lateinit var button : Button
//    private lateinit var button: Button
    private lateinit var vibrator: Vibrator
    private val accelerationThreshold = 30.0f // Adjust this value based on sensitivity
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_on)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val mp = MediaPlayer.create(applicationContext, R.raw.alarm)
        mp.isLooping = true
        mp.start()
        startContinuousVibration()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({Toast.makeText(this, "you can shake device to proceed or click stop", Toast.LENGTH_SHORT).show()
        },1000)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        var button:Button = findViewById(R.id.stopbutton)
        button.setOnClickListener {
//            mp.stop()
//            this.finish()
            val intent = Intent(this,ObjectDetection::class.java)
            startActivity(intent)
//            mp.stop()

            }

    }
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val acceleration = calculateAcceleration(event.values[0], event.values[1], event.values[2])

        if (!buttonPressed && acceleration > accelerationThreshold) {
//           button.performClick()
            buttonPressed = true
            val intent = Intent(this,ObjectDetection::class.java)
            startActivity(intent)
//            buttonPressed = true

        } else if (buttonPressed && acceleration < accelerationThreshold) {
            buttonPressed = false
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for accelerometer
    }

    private fun calculateAcceleration(x: Float, y: Float, z: Float): Float {
        return Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startContinuousVibration() {
        val vibrationEffect = VibrationEffect.createWaveform(
            longArrayOf(0, 1000), // Vibration pattern (on, off) in milliseconds
            0 // Repeat indefinitely
        )
        vibrator.vibrate(vibrationEffect)
    }
}

