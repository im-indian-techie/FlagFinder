package com.ashin.flagfinder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FlagApplication:Application() {
    override fun onCreate() {
        super.onCreate()
    }
}