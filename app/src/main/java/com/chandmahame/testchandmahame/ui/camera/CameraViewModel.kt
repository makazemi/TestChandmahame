package com.chandmahame.testchandmahame.ui.camera

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chandmahame.testchandmahame.repository.Event
import javax.inject.Inject

class CameraViewModel @Inject constructor():ViewModel() {

    private val _navigateToHome = MutableLiveData<Event<Boolean>>()

    val navigateToHome:LiveData<Event<Boolean>> get() = _navigateToHome

    fun setNavigateToHome(value:Boolean){
        _navigateToHome.value=Event.dataEvent(value)
    }

}