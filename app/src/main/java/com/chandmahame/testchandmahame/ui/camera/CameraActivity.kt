package com.chandmahame.testchandmahame.ui.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chandmahame.testchandmahame.R
import com.chandmahame.testchandmahame.base.BaseActivity
import com.chandmahame.testchandmahame.base.BaseApplication

class CameraActivity : BaseActivity() {
    override fun inject() {
        (application as BaseApplication).appComponent
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}