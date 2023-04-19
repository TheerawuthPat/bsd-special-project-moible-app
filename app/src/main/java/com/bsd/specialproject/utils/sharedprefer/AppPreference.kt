package com.bsd.specialproject.utils.sharedprefer

interface AppPreference {
    var isFirstTime: Boolean
}

class AppPreferenceImpl constructor(
    val preferenceStorage: PreferenceStorage
) : AppPreference {

    override var isFirstTime: Boolean
        get() = preferenceStorage.getBoolean(SharedPrefConstants.FIRST_TIME, false)
        set(value) {
            preferenceStorage.putBoolean(SharedPrefConstants.FIRST_TIME, value)
        }
}
