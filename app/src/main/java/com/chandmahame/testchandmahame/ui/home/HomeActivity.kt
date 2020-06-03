package com.chandmahame.testchandmahame.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chandmahame.testchandmahame.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()

    }
}