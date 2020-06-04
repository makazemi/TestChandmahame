package com.chandmahame.testchandmahame.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.chandmahame.testchandmahame.R
import com.chandmahame.testchandmahame.base.BaseApplication
import com.chandmahame.testchandmahame.repository.ErrorBody
import com.chandmahame.testchandmahame.repository.Event
import com.chandmahame.testchandmahame.repository.Loading
import kotlinx.android.synthetic.main.fragment_list_image_local.*
import javax.inject.Inject

class ListImageLocalFragment : Fragment() {
    companion object{
        const val TAG="ListImageServerFragment"
    }

    private lateinit var imageAdapter: ImageAdapter
    @Inject
    lateinit var requestManager: RequestManager
    @Inject
    lateinit var  viewModelFactory: ViewModelProvider.Factory

    private val viewModel: HomeViewModel by activityViewModels {
        viewModelFactory
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_image_local, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRcy()
        subscribeObserverListImage()
    }

    private fun initRcy(){
        imageAdapter= ImageAdapter(requestManager, ImageAdapter.LOCAL_IMAGE)
        rcy_img.apply {
            layoutManager= LinearLayoutManager(this@ListImageLocalFragment.context)
            adapter=imageAdapter
        }
    }
    private fun subscribeObserverListImage(){
        viewModel.localImage.observe(viewLifecycleOwner, Observer {
            it?.data?.getContentIfNotHandled()?.let {
                imageAdapter.submitList(it)
            }
            onDataStateChange(it.loading,it.error)
        })
    }
    private fun onDataStateChange(loading: Loading, error: Event<ErrorBody>?){
        error?.let {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this.context,it.message, Toast.LENGTH_SHORT).show()
            }
        }
        displayProgressBar(loading.isLoading)

    }
    private fun displayProgressBar(inProgress:Boolean){
        if(inProgress)
            progress_bar.visibility=View.VISIBLE
        else
            progress_bar.visibility=View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rcy_img.adapter=null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as BaseApplication).appComponent.inject(this)
    }

}