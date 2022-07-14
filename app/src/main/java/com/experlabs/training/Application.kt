package com.experlabs.training

import android.app.Application
import io.branch.referral.Branch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(activityModule, viewmodelModule, repositorymodule)
        }

        // Initialize the Branch object
        Branch.enableTestMode()
        Branch.getAutoInstance(this);
    }

}