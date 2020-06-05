package com.chandmahame.testchandmahame.base

import android.util.Log
import androidx.multidex.MultiDexApplication
import androidx.work.*
import com.chandmahame.testchandmahame.di.AppComponent
import com.chandmahame.testchandmahame.di.DaggerAppComponent
import com.chandmahame.testchandmahame.util.Constant.PULL_PERIODIC_NOTIFICATION_WORK_NAME
import com.chandmahame.testchandmahame.worker.PullNotificationWorker
import java.util.concurrent.TimeUnit


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
        initWorkManager()
    }
    private fun initWorkManager(){
        Log.d("MainRepository","initWorkManager")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val notificationRequest =
            PeriodicWorkRequestBuilder<PullNotificationWorker>(40, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(this)
           // .enqueue(notificationRequest)
           .enqueueUniquePeriodicWork(PULL_PERIODIC_NOTIFICATION_WORK_NAME,ExistingPeriodicWorkPolicy.REPLACE,notificationRequest)

    }


}