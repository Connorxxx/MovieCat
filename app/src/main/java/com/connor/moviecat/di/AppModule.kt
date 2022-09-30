package com.connor.moviecat.di

import com.connor.moviecat.Repository
import com.connor.moviecat.model.net.*
import com.connor.moviecat.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { Repository(get()) }
    single { TMDBService() }

    factory { RepoPagingSource(get()) }
    factory { MoviePagingSource(get()) }
    factory { TVPagingSource(get()) }
    factory { (string: String) -> SearchPagingSource(get(), string) }
    viewModel { MainViewModel(get()) }
}