package com.chandmahame.testchandmahame.worker

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.chandmahame.testchandmahame.di.workManager.ChildWorkerFactory
import com.chandmahame.testchandmahame.util.Constant.OUTPUT_PATH
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class SaveImageWorker  @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters
) : Worker(appContext, params) {
    override fun doWork(): Result {
        try {
           return Result.failure()

        } catch (throwable: Throwable) {
            return Result.failure()
        }
    }

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory
}

@Throws(FileNotFoundException::class)
fun writeBitmapToFile(applicationContext: Context, bitmap: Bitmap): Uri {
    val name = String.format("image-%s.png", UUID.randomUUID().toString())
    val outputDir = File(applicationContext.filesDir, OUTPUT_PATH)
    if (!outputDir.exists()) {
        outputDir.mkdirs() // should succeed
    }
    val outputFile = File(outputDir, name)
    var out: FileOutputStream? = null
    try {
        out = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
    } finally {
        out?.let {
            try {
                it.close()
            } catch (ignore: IOException) {
            }

        }
    }
    return Uri.fromFile(outputFile)
}