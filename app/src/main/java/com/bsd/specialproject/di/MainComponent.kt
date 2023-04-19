package com.bsd.specialproject.di

import android.content.Context
import android.content.SharedPreferences
import com.bsd.specialproject.AppRouter
import com.bsd.specialproject.AppRouterImpl
import com.bsd.specialproject.utils.sharedprefer.*
import com.bsd.specialproject.utils.DeviceSettings
import com.bsd.specialproject.utils.DeviceSettingsImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val localModule = module {
    single<SharedPreferences>(named(SharedPrefConstants.SHARED_PREFERENCES_NAME)) {
        androidContext().applicationContext.getSharedPreferences(
            SharedPrefConstants.SHARED_PREFERENCES_NAME,
            Context.MODE_PRIVATE
        )
    }
    single<PreferenceStorage> { PreferenceStorageImpl(get(named(SharedPrefConstants.SHARED_PREFERENCES_NAME))) }
    single<AppPreference> { AppPreferenceImpl(get()) }
    single<DeviceSettings> { DeviceSettingsImpl(get()) }
}


val routerModule = module {
    single<AppRouter> { AppRouterImpl() }
}

val appComponent = listOf(
    viewModelModule,
    localModule,
    routerModule
)

