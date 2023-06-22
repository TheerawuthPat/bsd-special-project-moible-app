package com.bsd.specialproject.di

import com.bsd.specialproject.ui.addcreditcard.CreditCardViewModel
import com.bsd.specialproject.ui.home.HomeViewModel
import com.bsd.specialproject.ui.main.MainViewModel
import com.bsd.specialproject.ui.promotion.MyPromotionViewModel
import com.bsd.specialproject.ui.searchresult.SearchResultViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { CreditCardViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { SearchResultViewModel(get(), get()) }
    viewModel { MyPromotionViewModel(get(), get()) }
}
