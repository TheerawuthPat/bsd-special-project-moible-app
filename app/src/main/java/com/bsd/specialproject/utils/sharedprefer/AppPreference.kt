package com.bsd.specialproject.utils.sharedprefer

import com.bsd.specialproject.utils.sharedprefer.SharedPrefConstants.MY_CREDIT_CARDS

interface AppPreference {
    var isFirstTime: Boolean
    var myCreditCards: Set<String>?
}

class AppPreferenceImpl constructor(
    val preferenceStorage: PreferenceStorage
) : AppPreference {

    override var isFirstTime: Boolean
        get() = preferenceStorage.getBoolean(SharedPrefConstants.FIRST_TIME, false)
        set(value) {
            preferenceStorage.putBoolean(SharedPrefConstants.FIRST_TIME, value)
        }
    override var myCreditCards: Set<String>?
        get() = preferenceStorage.getStringSet(MY_CREDIT_CARDS)
        set(value) {
            value?.let {
                preferenceStorage.putStringSet(MY_CREDIT_CARDS, it)
            }
        }
}
