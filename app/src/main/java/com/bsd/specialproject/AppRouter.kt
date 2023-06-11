package com.bsd.specialproject

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.bsd.specialproject.ui.addcreditcard.AddCreditCardActivity
import com.bsd.specialproject.ui.main.MainActivity
import com.bsd.specialproject.ui.searchresult.SearchResultActivity
import com.bsd.specialproject.ui.searchresult.model.SearchResultModel

interface AppRouter {
    fun toMain(activity: AppCompatActivity)
    fun toAddCreditCard(activity: FragmentActivity)
    fun toSearchResult(activity: FragmentActivity, searchResultModel: SearchResultModel)
}

class AppRouterImpl : AppRouter {

    override fun toMain(activity: AppCompatActivity) {
        MainActivity.start(activity)
    }

    override fun toAddCreditCard(activity: FragmentActivity) {
        AddCreditCardActivity.start(activity)
    }

    override fun toSearchResult(activity: FragmentActivity, searchResultModel: SearchResultModel) {
        SearchResultActivity.start(activity, searchResultModel)
    }
}
