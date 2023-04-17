package com.bsd.specialproject

import android.app.Application
import com.bsd.specialproject.di.appComponent
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class MainApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin {
            androidContext(this@MainApplication)
            modules(appComponent)
            androidFileProperties()
        }
    }

}
