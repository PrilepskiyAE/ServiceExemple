package com.ambrella.serviceexemple

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

class MyForegraundService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Toast.makeText( applicationContext, "Create ForegraundService", Toast.LENGTH_SHORT).show()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var t1=intent?.getIntExtra(ST,0)?:0
       coroutineScope.launch {
           var test=0
           while ( test<=t1+100){
               delay(1000)
               Toast.makeText( applicationContext, "This is a ForegraundService running in Background${test++}", Toast.LENGTH_SHORT).show()
           }
           stopSelf()
       }

       // return super.onStartCommand(intent, flags, startId) = START STICKY
        return  START_STICKY
    }





    override fun onDestroy() {
        coroutineScope.cancel()
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        super.onDestroy()

    }
    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    private fun createNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Title")
        .setContentText("Text")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .build()

    companion object {
        private const val ST="start"
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
        fun newIntent(context: Context,start:Int): Intent {
            return Intent(context, MyForegraundService::class.java).apply {
                putExtra(ST, start)
            }
        }
    }
}