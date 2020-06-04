package com.chandmahame.testchandmahame.di

import android.app.Application
import com.chandmahame.testchandmahame.base.BaseActivity
import com.chandmahame.testchandmahame.di.workManager.AppAssistedInjectModule
import com.chandmahame.testchandmahame.di.workManager.AppWorkerFactory
import com.chandmahame.testchandmahame.di.workManager.WorkerBindingModule
import com.chandmahame.testchandmahame.ui.camera.CameraActivity
import com.chandmahame.testchandmahame.ui.home.HomeActivity
import com.chandmahame.testchandmahame.ui.home.HomeFragment
import com.chandmahame.testchandmahame.ui.home.ListImageLocalFragment
import com.chandmahame.testchandmahame.ui.home.ListImageServerFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        ViewModelModule::class,
        AppAssistedInjectModule::class,
        WorkerBindingModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(baseActivity: BaseActivity)
    fun inject(cameraActivity: CameraActivity)
    fun inject(homeActivity: HomeActivity)
    fun inject(fragment:HomeFragment)
    fun inject(fragment:ListImageServerFragment)
    fun inject(fragment: ListImageLocalFragment)
    fun factoryAppWorker(): AppWorkerFactory

}








