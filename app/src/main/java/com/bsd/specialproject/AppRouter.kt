package com.bsd.specialproject

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.bsd.specialproject.ui.addcreditcard.AddCreditCardActivity
import com.bsd.specialproject.ui.main.MainActivity

interface AppRouter {
    fun toMain(activity: AppCompatActivity)
    fun toAddCreditCard(activity: FragmentActivity)
}

class AppRouterImpl : AppRouter {

    override fun toMain(activity: AppCompatActivity) {
        MainActivity.start(activity)
    }

    override fun toAddCreditCard(activity: FragmentActivity) {
        AddCreditCardActivity.start(activity)
    }
}
