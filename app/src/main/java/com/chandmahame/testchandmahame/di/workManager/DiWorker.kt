package com.chandmahame.testchandmahame.di.workManager

import androidx.work.ListenableWorker
import com.chandmahame.testchandmahame.worker.PullNotificationWorker
import com.chandmahame.testchandmahame.worker.SaveImageWorker
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass


@Module(includes = [AssistedInject_AppAssistedInjectModule::class])
@AssistedModule
abstract class AppAssistedInjectModule {}

@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)

@Module
interface WorkerBindingModule {

    @Binds
    @IntoMap
    @WorkerKey(SaveImageWorker::class)
    fun bindSaveImageWorker(worker: SaveImageWorker.Factory): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(PullNotificationWorker::class)
    fun bindNotificationWorker(worker: PullNotificationWorker.Factory): ChildWorkerFactory
}
