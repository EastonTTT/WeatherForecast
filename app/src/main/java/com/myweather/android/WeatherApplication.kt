package com.myweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class WeatherApplication:Application() {
    companion object{
        const val TOKEN = "ItgAMf2ehXjtwXid"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}