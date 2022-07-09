package com.experlabs.training

import com.experlabs.training.repository.MemeRepository
import com.experlabs.training.retrofit.RetrofitObject
import org.koin.dsl.module

val repositorymodule = module {
    single {
            RetrofitObject()
    }
    single {
        MemeRepository(get())
    }
}