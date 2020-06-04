package com.chandmahame.testchandmahame.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment:Fragment) :FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->{
                ListImageLocalFragment()
            }
            1 ->{
                ListImageServerFragment()
            }
            else ->  ListImageLocalFragment()
        }
    }

}