package com.chandmahame.testchandmahame.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.chandmahame.testchandmahame.R
import com.chandmahame.testchandmahame.base.BaseActivity
import com.chandmahame.testchandmahame.base.BaseApplication
import com.chandmahame.testchandmahame.ui.home.HomeActivity
import com.chandmahame.testchandmahame.util.Constant.OUTPUT_PATH
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer


class CameraActivity : BaseActivity() {
    companion object{
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_CODE_PERMISSION=1000
        const val TAG="CameraTEST"
    }
    private lateinit var currentPhotoPath: String
    private val viewModel: CameraViewModel by viewModels {
        viewModelFactory
    }
    override fun inject() {
        (application as BaseApplication).appComponent
            .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getUserPermission()
        subscribeObserverNavigateToHome()
    }

    private fun getUserPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                REQUEST_CODE_PERMISSION
            )
        }
        else{
            dispatchTakePictureIntent()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            }
            else{
                Toast.makeText(this,"You Should access camera and storage permission",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.chandmahame.testchandmahame.provider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

        @Throws(IOException::class)
        private fun createImageFile(): File {
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(Date())
            val storageDir: File? = getExternalFilesDir(OUTPUT_PATH)
            return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            ).apply {
                currentPhotoPath=absolutePath
            }
        }

    private fun galleryAddPic() {
            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
                val f = File(currentPhotoPath)
                mediaScanIntent.data = Uri.fromFile(f)
                sendBroadcast(mediaScanIntent)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            viewModel.setNavigateToHome(true)
            Log.d(TAG,"onActivityResult=data=$data")
            galleryAddPic()
            data?.extras?.get("data")?.let {
                Log.d(TAG,"onActivityResult")

            }
        }
    }
    private fun subscribeObserverNavigateToHome(){
        viewModel.navigateToHome.observe(this, Observer {
            it?.getContentIfNotHandled()?.let {
                if(it)
                    goToHomeActivity()
            }
        })
    }
    private fun goToHomeActivity(){
        val intent=Intent(this,HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.setNavigateToHome(true)
    }
}