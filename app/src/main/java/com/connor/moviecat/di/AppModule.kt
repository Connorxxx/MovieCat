package com.connor.moviecat.di

import com.connor.moviecat.Repository
import com.connor.moviecat.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { Repository() }
    viewModel { MainViewModel(get()) }
}