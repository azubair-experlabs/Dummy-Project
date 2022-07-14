package com.experlabs.training

import com.experlabs.training.repository.MemeRepository
import com.experlabs.training.retrofit.RetrofitObject
import com.experlabs.training.room.MemeDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositorymodule = module {
    single {
            RetrofitObject()
    }

    single {
        MemeDatabase.getInstance(androidContext())
    }

    single {
        MemeRepository(get(), get())
    }
}