package com.chandmahame.testchandmahame.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.chandmahame.testchandmahame.util.*
import com.chandmahame.testchandmahame.util.Constant.NETWORK_TIMEOUT
import com.chandmahame.testchandmahame.util.Constant.TESTING_CACHE_DELAY
import com.chandmahame.testchandmahame.util.Constant.TESTING_NETWORK_DELAY
import com.chandmahame.testchandmahame.util.ErrorHandling.Companion.ERROR_CHECK_NETWORK_CONNECTION
import com.chandmahame.testchandmahame.util.ErrorHandling.Companion.ERROR_UNKNOWN
import kotlinx.coroutines.*

abstract class NetworkBoundResource<ResponseObject,CacheObject>
    (  isNetworkAvailable: Boolean, // is their a network connection?
       shouldCancelIfNoInternet: Boolean // should this job be cancelled if there is no network?
 ){

    private val tag="NetworkBoundResource"
    protected val result = MediatorLiveData<DataState<CacheObject>>()
    protected lateinit var job: CompletableJob
    private lateinit var coroutineScope: CoroutineScope

    init {
       initNewJob()
        setValue(DataState.loading(true,cachedData = null))
            val dbSource=this.loadFromCache()
            result.addSource(dbSource){
                result.removeSource(dbSource)
                if(shouldFetch(it)){
                    if(isNetworkAvailable){
                        doNetworkRequest()
                    }else{
                        if(shouldCancelIfNoInternet){
                            onErrorReturn(ErrorBody(message = ErrorHandling.UNABLE_TODO_OPERATION_WO_INTERNET))
                        }else{
                            doCacheRequest()
                        }
                    }
                }
                else{
                    doCacheRequest()
                }
            }
    }
    private suspend fun handleNetworkCall(response: GenericApiResponse<ResponseObject>){

        when(response){
            is ApiSuccessResponse ->{
                Log.e(tag,"response.body: ${response.body}")
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse ->{
                onErrorReturn(response.error)
            }
            is ApiEmptyResponse ->{
                onErrorReturn(ErrorBody("204","HTTP 204. Returned NOTHING."))
            }
        }
    }
    private fun doNetworkRequest(){
        coroutineScope.launch {

            // simulate a network delay for testing
            delay(TESTING_NETWORK_DELAY)
            Log.e(tag,"doNetworkRequest")

            withContext(Dispatchers.Main){

                // make network call
                val apiResponse = createCall()
                result.addSource(apiResponse){ response ->
                    result.removeSource(apiResponse)

                    Log.e(tag,"doNetworkRequest dispatcher.Main")
                    coroutineScope.launch {
                        handleNetworkCall(response)
                        Log.e(tag,"doNetworkRequest lauch dovom")
                    }
                }
            }
        }

        GlobalScope.launch(Dispatchers.IO){
            delay(NETWORK_TIMEOUT)

            if(!job.isCompleted){
                Log.e(tag, "NetworkBoundResource: JOB NETWORK TIMEOUT." )
                job.cancel(CancellationException(ErrorHandling.UNABLE_TO_RESOLVE_HOST))
            }
        }
    }
    private fun doCacheRequest(){
        coroutineScope.launch {
            delay(TESTING_CACHE_DELAY)
            // View data from cache only and return
            Log.e(tag,"doCacheRequest")
            createCacheRequestAndReturn()
        }
    }
    private fun setValue(dataState: DataState<CacheObject>){
        result.value = dataState
    }
    fun onCompleteJob(dataState: DataState<CacheObject>){
        GlobalScope.launch(Dispatchers.Main) {
            job.complete()
            setValue(dataState)
        }
    }

    fun onErrorReturn(error:ErrorBody){
        var msg = error.message

        if(msg == null){
            msg = ERROR_UNKNOWN
        }
        else if(ErrorHandling.isNetworkError(msg)){
            Log.e(tag,"ERROR_CHECK_NETWORK_CONNECTION=${msg}")
            msg = ERROR_CHECK_NETWORK_CONNECTION
        }

        Log.e(tag,"onErrorReturn=${msg}")

        onCompleteJob(DataState.error(ErrorBody(error.code,msg)))
    }

    @OptIn(InternalCoroutinesApi::class)
    private fun initNewJob(): Job{
        Log.e(tag, "initNewJob: called.")
        job = Job() // create new job
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true, handler = object: CompletionHandler{
            override fun invoke(cause: Throwable?) {
                if(job.isCancelled){
                    Log.e(tag, "NetworkBoundResource: Job has been cancelled.")
                    cause?.let{
                        onErrorReturn(ErrorBody(message = it.message))
                    }?: onErrorReturn(ErrorBody(message = ERROR_UNKNOWN))
                }
                else if(job.isCompleted){
                    Log.e(tag, "NetworkBoundResource: Job has been completed.")
                    // Do nothing? Should be handled already
                }
            }
        })
        coroutineScope = CoroutineScope(Dispatchers.IO + job)
        return job
    }

    fun asLiveData() = result as LiveData<DataState<CacheObject>>

    abstract suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    abstract suspend fun createCacheRequestAndReturn()

    abstract fun loadFromCache():LiveData<CacheObject>

    abstract suspend fun updateLocalDb(cacheObject: CacheObject?)

    abstract fun shouldFetch(cacheObject: CacheObject?):Boolean

}