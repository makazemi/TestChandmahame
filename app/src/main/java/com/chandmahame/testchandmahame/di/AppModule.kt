package com.chandmahame.testchandmahame.di

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.chandmahame.testchandmahame.api.ApiService
import com.chandmahame.testchandmahame.repository.MainRepository
import com.chandmahame.testchandmahame.util.Constant.BASE_URL
import com.chandmahame.testchandmahame.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object AppModule{


    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideGlideInstance(application: Application): RequestManager {
        return Glide.with(application)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit
            .create(ApiService::class.java)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMainRepository(app: Application,
                               apiService: ApiService):MainRepository{
        return MainRepository(app,apiService)
    }


}