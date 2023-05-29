package com.bsd.specialproject.utils.sharedprefer

import com.bsd.specialproject.utils.sharedprefer.SharedPrefConstants.MY_CREDIT_CARDS
import com.bsd.specialproject.utils.sharedprefer.SharedPrefConstants.MY_EXPENSE_LAST_MONTH

interface AppPreference {
    var isFirstTime: Boolean
    var myCreditCards: Set<String>?
    var myExpenseLastMonth: String
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
        get() = preferenceStorage.getStringSet(MY_CREDIT_CARDS)?.distinct()?.toSet()
        set(value) {
            value?.let {
                preferenceStorage.putStringSet(MY_CREDIT_CARDS, it)
            }
        }
    override var myExpenseLastMonth: String
        get() = preferenceStorage.getString(MY_EXPENSE_LAST_MONTH)
        set(value) {
            value.let {
                preferenceStorage.putString(MY_EXPENSE_LAST_MONTH, it)
            }
        }
}
