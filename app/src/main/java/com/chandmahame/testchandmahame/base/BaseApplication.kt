package com.chandmahame.testchandmahame.base

import androidx.multidex.MultiDexApplication
import androidx.work.Configuration
import androidx.work.WorkManager
import com.chandmahame.testchandmahame.di.AppComponent
import com.chandmahame.testchandmahame.di.DaggerAppComponent


class BaseApplication: MultiDexApplication(){


    lateinit var appComponent: AppComponent


    private fun initAppComponent(){
        appComponent= DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(appComponent.factoryAppWorker())
                .build()
        )


    }


}