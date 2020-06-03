package com.chandmahame.testchandmahame.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.chandmahame.testchandmahame.api.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(private val application:Application,private val apiService: ApiService) {

    fun isConnectedToTheInternet(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            return cm.activeNetworkInfo.isConnected
        } catch (e: Exception) {
        }
        return false
    }
}