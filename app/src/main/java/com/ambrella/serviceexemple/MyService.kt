package com.ambrella.serviceexemple

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class MyService : Service() {


    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onTaskRemoved(intent)
        var test=1
        Toast.makeText( applicationContext, "This is a Service running in Background${test++}", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
       // val restartServiceIntent = Intent(applicationContext, this.javaClass)
       // restartServiceIntent.setPackage(packageName)
        //startService(restartServiceIntent)
        super.onTaskRemoved(rootIntent)
    }

    override fun stopService(name: Intent?): Boolean {
        Toast.makeText( applicationContext, "Stop service", Toast.LENGTH_SHORT).show()
        //Intent(applicationContext, MyService::class.java)
        return super.stopService(name)
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        super.onDestroy();
    }

}