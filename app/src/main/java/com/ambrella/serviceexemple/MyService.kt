package com.ambrella.serviceexemple

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import kotlinx.coroutines.*

class MyService : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onBind(intent: Intent): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Toast.makeText( applicationContext, "Create Service", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var t1=intent?.getIntExtra(ST,0)?:0
       coroutineScope.launch {
           var test=0
           while ( test<=t1+100){
               delay(1000)
               Toast.makeText( applicationContext, "This is a Service running in Background${test++}", Toast.LENGTH_SHORT).show()
           }
       }

       // return super.onStartCommand(intent, flags, startId) = START STICKY
        return START_REDELIVER_INTENT
    }





    override fun onDestroy() {
        coroutineScope.cancel()
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
        super.onDestroy();
    }

    companion object {
private const val ST="start"
        fun newIntent(context: Context,start:Int): Intent {
            return Intent(context, MyService::class.java).apply {
                putExtra(ST, start)
            }
        }
    }
}