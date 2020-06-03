package com.chandmahame.testchandmahame.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.chandmahame.testchandmahame.di.workManager.ChildWorkerFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

class PullNotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters
) : Worker(appContext, params) {
    override fun doWork(): Result {
        try {
            return Result.failure()

        } catch (throwable: Throwable) {
            return Result.failure()
        }
    }

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory
}