package com.chandmahame.testchandmahame.di
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ViewModelModule {

    @Singleton
    @Binds
    abstract fun bindMainViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory



//    @Binds
//    @IntoMap
//    @MainViewModelKey(SplashViewModel::class)
//    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

}








