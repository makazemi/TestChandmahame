package com.chandmahame.testchandmahame.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chandmahame.testchandmahame.R

const val TAG="HomeActivity"
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }
}