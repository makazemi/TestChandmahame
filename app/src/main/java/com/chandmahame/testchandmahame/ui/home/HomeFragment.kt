package com.chandmahame.testchandmahame.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chandmahame.testchandmahame.R
import com.chandmahame.testchandmahame.base.BaseApplication
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.pager
import kotlinx.android.synthetic.main.fragment_home.tab_layout

class HomeFragment : Fragment() {


    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager(){
        viewPagerAdapter = ViewPagerAdapter(this)
        pager.adapter = viewPagerAdapter
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            when(position){
                0 ->{
                    tab.text=getString(R.string.local_image)
                }
                1 ->{
                    tab.text=getString(R.string.server_image)
                }
            }
        }.attach()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as BaseApplication).appComponent.inject(this)
    }
}