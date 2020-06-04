package com.chandmahame.testchandmahame.worker

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chandmahame.testchandmahame.R
import com.chandmahame.testchandmahame.di.workManager.ChildWorkerFactory
import com.chandmahame.testchandmahame.model.NotificationResponse
import com.chandmahame.testchandmahame.repository.MainRepository
import com.chandmahame.testchandmahame.ui.camera.CameraActivity
import com.chandmahame.testchandmahame.ui.home.HomeActivity
import com.chandmahame.testchandmahame.util.Constant.CAMERA
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.*

class PullNotificationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val repository: MainRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = coroutineScope{
        Log.d(TAG,"doWork")
        try {
            val response=repository.pullNotification()
            response.data?.getContentIfNotHandled()?.let {
                createNotification(it)
            }
             Result.success()

        } catch (throwable: Throwable) {
            Log.d(TAG,"work faile=${throwable.message}")
             Result.failure()
        }
    }

    private fun createNotification(item:NotificationResponse){

        val resultIntent = if(item.path==CAMERA) Intent(appContext, CameraActivity::class.java)
        else
            Intent(appContext, HomeActivity::class.java)
        resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val resultPendingIntent = PendingIntent.getActivity(
            appContext,
            0, resultIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val channelId = appContext.getString(R.string.fcm_notification_channel_id)
        val channelName = appContext.getString(R.string.fcm_notification_channel_name)

        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification =
            NotificationCompat.Builder(appContext, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(item.content_title)
                .setContentText(item.summary_text)
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText(item.big_text))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
        notificationManager.notify(0 /* ID of notification */, notification)

    }
    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory

    companion object{
        const val TAG="PullNotificationWorker"
    }
}
