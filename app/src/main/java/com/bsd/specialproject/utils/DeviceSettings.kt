package com.bsd.specialproject.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

interface DeviceSettings {
    val deviceId: String
    val deviceBrand: String
    val deviceModel: String
    val osVersion: String
    val appVersion: String
}

class DeviceSettingsImpl constructor(
    private val context: Context
) : DeviceSettings {

    override val deviceId: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

    override val deviceBrand: String
        get() = Build.MANUFACTURER

    override val deviceModel: String
        get() = Build.MODEL

    override val osVersion: String
        get() = Build.VERSION.SDK_INT.toString()

    override val appVersion: String
        get() = try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName
        } catch (e: Exception) {
            ""
        }
}
