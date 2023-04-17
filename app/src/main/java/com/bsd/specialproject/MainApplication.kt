package com.bsd.specialproject

import android.app.Application
import org.koin.core.component.KoinComponent

class MainApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
    }

}
