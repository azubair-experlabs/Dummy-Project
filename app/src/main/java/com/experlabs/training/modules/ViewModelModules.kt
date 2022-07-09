package com.experlabs.training
import com.experlabs.training.viewmodels.MemeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewmodelModule = module {
    viewModel {
        MemeViewModel(get())
    }
}