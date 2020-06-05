package com.chandmahame.testchandmahame.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.service.autofill.Dataset
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.chandmahame.testchandmahame.api.ApiService
import com.chandmahame.testchandmahame.model.Dairy
import com.chandmahame.testchandmahame.model.DairyResponse
import com.chandmahame.testchandmahame.model.NotificationResponse
import com.chandmahame.testchandmahame.util.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FilenameFilter
import javax.inject.Inject

class MainRepository @Inject constructor(private val application:Application,private val apiService: ApiService) {
    companion object{
        const val TAG="MainRepository"
    }
    fun getServerDairy():LiveData<DataState<List<Dairy>>>{
        return object :NetworkBoundResource<DairyResponse,List<Dairy>>(
            isConnectedToTheInternet(),
            true
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<DairyResponse>) {
               withContext(Dispatchers.Main){
                   onCompleteJob(
                       DataState.data(response.body.items)
                   )
               }
            }
            override fun createCall(): LiveData<GenericApiResponse<DairyResponse>> = apiService.getListDairy()
            override suspend fun createCacheRequestAndReturn() {}
            override fun loadFromCache(): LiveData<List<Dairy>> = AbsentLiveData.create()
            override suspend fun updateLocalDb(cacheObject: List<Dairy>?) {}
            override fun shouldFetch(cacheObject: List<Dairy>?): Boolean = true
        }.asLiveData()
    }

    suspend fun getLocalDairy(path:String):List<Dairy>{
        val result = ArrayList<Dairy>()
        withContext(Dispatchers.IO) {
            val folder = application.getExternalFilesDir(path)
            folder?.let { destination ->
                Log.d(TAG,"destinaton=$destination")
                if (destination.exists()) {
                    var i = 1
                    val allFiles =
                        folder.listFiles(FilenameFilter { dir, name ->
                            if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")) {
                                result.add(Dairy(i, "${Uri.fromFile(dir)}/$name", name))
                                i++
                            }
                            true
                        })
                }
            }
        }
        return result
    }
    suspend fun pullNotification():DataState<NotificationResponse> {

        return when (val response = fetchCatching()) {

            is ApiSuccessResponse -> {
                Log.d(TAG, "response notif=$response")
                DataState.data(response.body)
            }
            is ApiErrorResponse -> {
                Log.d(TAG, "response notif=$response")
                onErrorReturn(response.error)
            }
            is ApiEmptyResponse -> {
                Log.d(TAG, "response notif=$response")
                onErrorReturn(ErrorBody("204", "HTTP 204. Returned NOTHING."))
            }
        }

    }
    private suspend fun fetchCatching(): GenericApiResponse<NotificationResponse> {
        return try {
            apiService.getNotification()
        } catch (ex: CancellationException) {
            Log.d(TAG,"CancellationException=${ex.message}")
            throw ex
        } catch (ex: Throwable) {
            Log.d(TAG,"Throwable=${ex.message}")
            GenericApiResponse.create(ex)
        }
    }
    private fun onErrorReturn(error:ErrorBody):DataState<NotificationResponse>{
        var msg = error.message
        if(msg == null){
            msg = ErrorHandling.ERROR_UNKNOWN
        }
        else if(ErrorHandling.isNetworkError(msg)){
            msg = ErrorHandling.ERROR_CHECK_NETWORK_CONNECTION
        }

      return DataState.error(ErrorBody(error.code,msg))
    }
    private fun isConnectedToTheInternet(): Boolean {
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            return cm.activeNetworkInfo.isConnected
        } catch (e: Exception) {
        }
        return false
    }
}