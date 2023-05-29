package com.bsd.specialproject.ui.home

import androidx.lifecycle.*
import com.bsd.specialproject.utils.sharedprefer.AppPreference

class HomeViewModel(private val appPreference: AppPreference) : ViewModel() {

    private val _myExpenseLastMonth = MutableLiveData<String>()
    val myExpenseLastMonth: LiveData<String> = _myExpenseLastMonth

    fun fetchMyExpense() {
        _myExpenseLastMonth.postValue(appPreference.myExpenseLastMonth)
    }

    fun saveMyExpense(myExpense: String) {
        appPreference.myExpenseLastMonth = myExpense
        _myExpenseLastMonth.postValue(myExpense)
    }

}
