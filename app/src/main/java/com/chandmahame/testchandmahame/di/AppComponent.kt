package com.chandmahame.testchandmahame.di

import android.app.Application
import com.chandmahame.testchandmahame.base.BaseActivity
import com.chandmahame.testchandmahame.ui.camera.CameraActivity
import com.chandmahame.testchandmahame.ui.home.HomeActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class
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
}








