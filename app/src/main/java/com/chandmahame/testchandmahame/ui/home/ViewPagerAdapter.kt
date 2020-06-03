package com.chandmahame.testchandmahame.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment:Fragment) :FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        var fragment:Fragment=ListImageLocalFragment()
        when(position){
            0 ->{
                fragment=ListImageLocalFragment()
            }
            1 ->{
                fragment=ListImageServerFragment()
            }
        }
        return fragment
    }

}