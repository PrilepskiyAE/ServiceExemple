package com.ambrella.serviceexemple

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.URLUtil
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.work.*
import java.io.BufferedInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "KotlinApp"
        val button: Button = findViewById(R.id.button)
        button.setOnClickListener { startService(Intent(applicationContext, MyService::class.java)) }
        val button2: Button = findViewById(R.id.button2)
        button2.setOnClickListener {
          stopService(Intent(applicationContext, MyService::class.java))
        }
        val btnDownload: Button = findViewById(R.id.btnDownload)
        val btnShow: Button = findViewById(R.id.btnShow)
        val editText:EditText=findViewById(R.id.editText)
        val imageView:ImageView=findViewById(R.id.imageView)
        val textView:TextView=findViewById(R.id.textView)
        btnDownload.setOnClickListener{
            // Test image url
            // https://www.freeimageslive.com/galleries/nature/animals/pics/guinea_pig.jpg
            // https://www.freeimageslive.com/galleries/nature/animals/pics/hens4949.jpg

            val text:String? = editText.text.toString()
            if (URLUtil.isValidUrl(text)){
                // Define the input data for work manager
                val data = Data.Builder()
                data.putString("url",editText.text.toString())

                // You can pass more data in parameters
                //data.putString("url2","https://www.freeimageslive.com/galleries/nature/animals/pics/hens4949.jpg")

                // Create an one time work request
                val downloadImageWork = OneTimeWorkRequest.Builder(DownloadImageWorker::class.java)
                    .setInputData(data.build())
                    .build()

                // Enqueue the work
                WorkManager.getInstance().enqueue((downloadImageWork))
            }else{
                toast("invalid url")
            }
        }
        btnShow.setOnClickListener{
            if (countSavedBitmap>0){
                imageView.setImageURI(nameToUri(savedBitmapList[countSavedBitmap-1]))
                textView.text = savedBitmapList[savedBitmapList.count()-1]
            }else
            {
                toast("No downloaded image found")
            }
        }
    }
}

class DownloadImageWorker(context: Context, params: WorkerParameters): Worker(context,params){
    override fun doWork(): Result {
        // Get the input data from parameters
        val urlString:String? = inputData.getString("url")

        // You can get the more url as the same way
        //val urlString:String? = inputData.getString("url2")

        // Do the work here
        val url: URL? = stringToURL(urlString)

        url?.let {
            // IMPORTANT - Put internet permission on manifest file
            var connection: HttpURLConnection? = null

            try {
                // Initialize a new http url connection
                connection = url.openConnection() as HttpURLConnection

                // Connect the http url connection
                connection?.connect()

                // Get the input stream from http url connection
                val inputStream = connection?.inputStream

                // Initialize a new BufferedInputStream from InputStream
                val bufferedInputStream = BufferedInputStream(inputStream)

                // Convert BufferedInputStream to Bitmap object

                // Return the downloaded bitmap
                val bmp: Bitmap? = BitmapFactory.decodeStream(bufferedInputStream)
                bmp?.saveToInternalStorage(applicationContext)

                Log.d("download","success")
                return Result.success()

            } catch (e: IOException) {
                e.printStackTrace()
                Log.d("download",e.toString())

            } finally {
                // Disconnect the http url connection
                connection?.disconnect()
            }

        }

        Log.d("download","failed")
        return Result.failure()
    }
}