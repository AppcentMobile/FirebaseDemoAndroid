package com.appcent.android.firebasedemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // init Timber for logging in debug mode
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


    }

}