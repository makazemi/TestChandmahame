package com.chandmahame.testchandmahame.di
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chandmahame.testchandmahame.di.keys.ViewModelKey
import com.chandmahame.testchandmahame.ui.camera.CameraViewModel
import com.chandmahame.testchandmahame.ui.home.HomeViewModel
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
    abstract fun bindCameraViewModel(viewModel:CameraViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel:HomeViewModel): ViewModel
}








