package com.orm.util

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.orm.R
import com.orm.data.model.Notification
import com.orm.data.repository.NotificationRepository
import com.orm.data.repository.UserRepository
import com.orm.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Firebase", "fcm token...... $token")
    }

    @Inject
    lateinit var notificationRepository: NotificationRepository

    @Inject
    lateinit var userRepository: UserRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("Firebase", "message received...... ${remoteMessage.notification.toString()}")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = userRepository.getUserInfo()

                if (user?.pushNotificationsEnabled == true) {
                    remoteMessage.notification?.let {
                        showNotification(it)
                    }
                } else {
                    Log.d("Firebase", "Push notifications are disabled for this user.")
                }

                val notification = Notification.toNotificationData(remoteMessage)
                notificationRepository.insertNotification(notification)

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MyFirebaseMessagingService", "Error handling message", e)
            }
        }
    }

    private fun showNotification(notification: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = "orm"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher_orm)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setContentIntent(pIntent)

        getSystemService(NotificationManager::class.java).run {
            val channel = NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
            createNotificationChannel(channel)
            notify(Date().time.toInt(), notificationBuilder.build())
        }
    }
}