package com.hoc.example

import android.app.Application
import com.hoc.example.ui.main.MainViewModel
import org.koin.android.ext.android.startKoin
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(retrofitModule, viewModelModule))
    }
}