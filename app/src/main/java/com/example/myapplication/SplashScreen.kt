package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        val logo = findViewById<ImageView>(R.id.logo)
        val animationRotate = AnimationUtils.loadAnimation(this, R.anim.bounce)
//        val animationShake = AnimationUtils.loadAnimation(this,R.anim.shake)
        logo.startAnimation(animationRotate)
//        logo.startAnimation(animationShake)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this,MainActivity::class.java)
//            overridePendingTransition(R.anim.diagonalrightenter,R.anim.diagonalexit);
            startActivity(intent)
            overridePendingTransition(R.anim.diagonalrightenter,R.anim.diagonalexit);

            finish()
        },4000)
    }
}