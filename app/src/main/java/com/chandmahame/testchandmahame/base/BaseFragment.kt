package com.chandmahame.testchandmahame.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import com.chandmahame.testchandmahame.repository.ErrorBody
import com.chandmahame.testchandmahame.repository.Event
import com.chandmahame.testchandmahame.repository.Loading
import javax.inject.Inject

abstract class BaseFragment:Fragment() {

    @Inject
    lateinit var requestManager: RequestManager
    @Inject
    lateinit var  viewModelFactory: ViewModelProvider.Factory

    fun onDataStateChange(loading: Loading, error: Event<ErrorBody>?){
        error?.let {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this.context,it.message, Toast.LENGTH_SHORT).show()
            }
        }
        displayProgressBar(loading.isLoading)

    }
    abstract fun displayProgressBar(inProgress:Boolean)
}