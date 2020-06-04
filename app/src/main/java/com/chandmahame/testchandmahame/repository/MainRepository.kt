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
import com.chandmahame.testchandmahame.util.AbsentLiveData
import com.chandmahame.testchandmahame.util.ApiSuccessResponse
import com.chandmahame.testchandmahame.util.Constant
import com.chandmahame.testchandmahame.util.GenericApiResponse
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

    suspend fun getLocalDairy(path:String):LiveData<DataState<List<Dairy>>>{
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
            return liveData<DataState<List<Dairy>>> {
                emit(DataState.loading(true))
                try {
                    emit(DataState.data(result))
                } catch(ioException: Exception) {
                    emit(DataState.error(ErrorBody(message = "failed to load image")))
                }


            }

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