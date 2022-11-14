package com.uhi.mad.zigzag

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.uhi.mad.zigzag.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var channelId = "Highscore ID"
    private var channelName = "Highscore"

    private var notificationManager: NotificationManager? = null
    private var notifyUser = NotificationCompat.Builder(this, channelId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLocationPerms()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        checkNotifyVersion()
    }

    private fun checkLocationPerms() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_COARSE_LOCATION), 1)
        }
    }

    private fun checkNotifyVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

            notificationManager!!.createNotificationChannel(notificationChannel)
        }
    }

    fun notification(title: String, msg: String) {
        println("HELLO")
        notifyUser.setAutoCancel(true)
            .setSmallIcon(R.drawable.rectangle)
            .setContentTitle(title)
            .setContentText(msg)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSilent(false)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

        notificationManager!!.notify(0, notifyUser.build())
    }
}