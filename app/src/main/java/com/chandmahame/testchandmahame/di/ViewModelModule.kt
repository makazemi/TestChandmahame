package com.chandmahame.testchandmahame.di
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chandmahame.testchandmahame.di.keys.ViewModelKey
import com.chandmahame.testchandmahame.ui.camera.CameraViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindMainViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


    @Binds
    @IntoMap
    @ViewModelKey(CameraViewModel::class)
    abstract fun bindSplashViewModel(viewModel:CameraViewModel): ViewModel

}








