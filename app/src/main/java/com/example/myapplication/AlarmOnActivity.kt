package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AlarmOnActivity : AppCompatActivity() , SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var buttonPressed = false
    private lateinit var button : Button
//    private lateinit var button: Button
    private val accelerationThreshold = 50.0f // Adjust this value based on sensitivity
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_on)

        val mp = MediaPlayer.create(applicationContext, R.raw.alarm)
        mp.start()

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
            button.performClick()
            buttonPressed = true

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
}

