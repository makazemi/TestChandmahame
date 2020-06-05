package com.chandmahame.testchandmahame.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandmahame.testchandmahame.R
import com.chandmahame.testchandmahame.base.BaseApplication
import com.chandmahame.testchandmahame.base.BaseFragment
import com.chandmahame.testchandmahame.ui.home.ImageAdapter.Companion.SERVER_IMAGE
import kotlinx.android.synthetic.main.fragment_list_image_server.*


class ListImageServerFragment : BaseFragment() {
    companion object{
        const val TAG="ListImageServerFragment"
    }

    private lateinit var imageAdapter: ImageAdapter

    private val viewModel: HomeViewModel by activityViewModels {
        viewModelFactory
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_image_server, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRcy()
        subscribeObserverListImage()
    }

    private fun initRcy(){
        imageAdapter= ImageAdapter(requestManager,SERVER_IMAGE)
        rcy_img.apply {
            layoutManager=LinearLayoutManager(this@ListImageServerFragment.context)
            adapter=imageAdapter
        }
    }
    private fun subscribeObserverListImage(){
        viewModel.dairyImage.observe(viewLifecycleOwner, Observer {
            it?.data?.getContentIfNotHandled()?.let {
                imageAdapter.submitList(it)
            }
            onDataStateChange(it.loading,it.error)
        })
    }

    override fun displayProgressBar(inProgress:Boolean){
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