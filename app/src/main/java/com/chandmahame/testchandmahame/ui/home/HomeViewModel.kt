package com.chandmahame.testchandmahame.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.chandmahame.testchandmahame.model.Dairy
import com.chandmahame.testchandmahame.repository.DataState
import com.chandmahame.testchandmahame.repository.ErrorBody
import com.chandmahame.testchandmahame.repository.MainRepository
import com.chandmahame.testchandmahame.util.Constant.OUTPUT_PATH
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: MainRepository):ViewModel() {

    val dairyImage = repository.getServerDairy()

     val localImage= liveData<DataState<List<Dairy>>>(viewModelScope.coroutineContext) {
         emit(DataState.loading(true))
            try {
                emit(DataState.data(repository.getLocalDairy(OUTPUT_PATH)))
            } catch(ioException: Exception) {
                emit(DataState.error(ErrorBody(message = "failed to load image")))
            }
        }

}