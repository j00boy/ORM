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
import com.orm.data.model.board.BoardList
import com.orm.data.model.club.Club
import com.orm.data.repository.BoardRepository
import com.orm.data.repository.ClubRepository
import com.orm.data.repository.NotificationRepository
import com.orm.data.repository.UserRepository
import com.orm.ui.MainActivity
import com.orm.ui.board.BoardActivity
import com.orm.ui.board.BoardDetailActivity
import com.orm.ui.club.ClubActivity
import com.orm.ui.club.ClubDetailActivity
import com.orm.ui.club.ClubMemberActivity
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

    @Inject
    lateinit var clubRepository: ClubRepository

    @Inject
    lateinit var boardRepository: BoardRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("Firebase", "message received...... ${remoteMessage.notification.toString()}")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = userRepository.getUserInfo()

                if (user?.pushNotificationsEnabled == true) {
                    showNotification(remoteMessage)
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

    private fun showNotification(notification: RemoteMessage) {
        CoroutineScope(Dispatchers.Main).launch {
            var intent: Intent? = null
            val noti = Notification.toNotificationData(notification)
            Log.d("notiTest", noti.alertType)

            when(noti.alertType){
                "APPLICATION" -> {
                    Log.d("notiTest", "in appli")
                    intent = Intent(this@MyFirebaseMessagingService, ClubMemberActivity::class.java)
                    val club = getClub(noti.clubId)
                    Log.d("notiTest", club.toString())
                    intent.putExtra("club", club)
                }
                "ACCEPTANCE" -> {
                    intent = Intent(this@MyFirebaseMessagingService, ClubDetailActivity::class.java)
                    val club = getClub(noti.clubId)
                    intent.putExtra("club", club)
                }
                "EXPEL" -> {
                    intent = Intent(this@MyFirebaseMessagingService, ClubActivity::class.java)
                }
                "NEW_BOARD" -> {
                    intent = Intent(this@MyFirebaseMessagingService, BoardActivity::class.java)
                    val club = getClub(noti.clubId)
                    intent.putExtra("club", club)
                }
                "NEW_COMMENT" -> {
                    intent = Intent(this@MyFirebaseMessagingService, BoardDetailActivity::class.java)
                    val boardList = getBoardList(noti.boardId!!)
                    val club = getClub(noti.clubId)
                    intent.putExtra("club", club)
                    intent.putExtra("boardList", boardList)
                }
            }

            val pIntent = PendingIntent.getActivity(this@MyFirebaseMessagingService, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val channelId = "orm"

            val notificationBuilder = NotificationCompat.Builder(this@MyFirebaseMessagingService, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher_orm)
                .setContentTitle(notification.notification?.title)
                .setContentText(notification.notification?.body)
                .setContentIntent(pIntent)
                .setAutoCancel(true)

            getSystemService(NotificationManager::class.java).run {
                val channel = NotificationChannel(channelId, "알림", NotificationManager.IMPORTANCE_HIGH)
                createNotificationChannel(channel)
                notify(Date().time.toInt(), notificationBuilder.build())
            }
        }
    }


    suspend fun getBoardList(boardId: Int): BoardList {
        return withContext(Dispatchers.IO) {
            val board = boardRepository.getBoards(boardId)
            BoardList(
                boardId = board!!.boardId,
                userId = board.userId,
                title = board.title,
                hit = board.hit,
                createdAt = board.createdAt,
                commentCount = board.commentCount,
                userNickname = board.userNickname,
            )
        }
    }
    suspend fun getClub(clubId: Int): Club {
        return withContext(Dispatchers.IO) {
            clubRepository.getClubById(clubId)!!
        }
    }
}