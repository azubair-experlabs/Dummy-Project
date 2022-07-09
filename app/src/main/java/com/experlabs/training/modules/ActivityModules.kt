package com.experlabs.training
import com.experlabs.training.activities.MainActivity
import com.experlabs.training.viewmodels.MemeViewModel
import org.koin.dsl.module

val activityModule = module{
    scope<MainActivity> {
        scoped {
            MemeViewModel(get())
        }
    }
}