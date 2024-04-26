package com.dicoding.aplikasigithubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.aplikasigithubuser.R

class SplashScreenActivityclass : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_activity)

        val splashDo = object : Thread(){
            override  fun run(){
                try {
                    Thread.sleep(2500)

                    val doIntent = Intent(baseContext, MainActivity::class.java)
                    startActivity(doIntent)
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        splashDo.start()
    }

}