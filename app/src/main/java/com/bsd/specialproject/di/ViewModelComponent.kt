package com.bsd.specialproject.di

import com.bsd.specialproject.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelDependency = module {
    viewModel { MainViewModel() }
}
