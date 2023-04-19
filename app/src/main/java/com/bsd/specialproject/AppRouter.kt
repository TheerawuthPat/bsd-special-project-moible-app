package com.bsd.specialproject

import androidx.appcompat.app.AppCompatActivity
import com.bsd.specialproject.ui.addcreditcard.AddCreditCardActivity
import com.bsd.specialproject.ui.main.MainActivity

interface AppRouter {
    fun toMain(activity: AppCompatActivity)
    fun toAddCreditCard(activity: AppCompatActivity)
}

class AppRouterImpl : AppRouter {

    override fun toMain(activity: AppCompatActivity) {
        MainActivity.start(activity)
    }

    override fun toAddCreditCard(activity: AppCompatActivity) {
        AddCreditCardActivity.start(activity)
    }
}
