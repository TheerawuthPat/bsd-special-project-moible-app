package com.bsd.specialproject.di

import com.bsd.specialproject.ui.addcreditcard.CreditCardViewModel
import com.bsd.specialproject.ui.home.HomeViewModel
import com.bsd.specialproject.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { CreditCardViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}
