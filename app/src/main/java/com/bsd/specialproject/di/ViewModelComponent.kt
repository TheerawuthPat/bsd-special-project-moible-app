package com.bsd.specialproject.di

import com.bsd.specialproject.ui.addcreditcard.AddCreditCardViewModel
import com.bsd.specialproject.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { AddCreditCardViewModel() }
}
