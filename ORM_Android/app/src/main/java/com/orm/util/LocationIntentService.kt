package com.orm.util

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.orm.R
import com.orm.ui.trace.TraceMeasureActivity
import android.content.Intent as BroadcastIntent
import android.os.IBinder as IBinder1

@SuppressLint("MissingPermission")
class LocationIntentService : Service() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var handler: Handler
    private lateinit var handlerThread: HandlerThread

    private val localBroadcastManager: LocalBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }
    private val notificationId = 1
    private val channelId = "location_channel"
    private val updateIntervalMillis = 5000L
    private val range = 6.000f

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()

        handlerThread = HandlerThread("LocationWorkerThread").apply { start() }
        handler = Handler(handlerThread.looper)

        createNotificationChannel()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            startForeground(notificationId, createNotification())
        } else {
            startForeground(
                notificationId,
                createNotification(),
                FOREGROUND_SERVICE_TYPE_LOCATION
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, updateIntervalMillis).apply {
                setMinUpdateDistanceMeters(range)
            }.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    updateLocation(it)
                }
            }
        }

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            handler.looper
        )
    }

    private fun updateLocation(location: Location) {
        val intent = BroadcastIntent("com.orm.LOCATION_UPDATE").apply {
            putExtra("latitude", location.latitude)
            putExtra("longitude", location.longitude)
        }
        localBroadcastManager.sendBroadcast(intent)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createNotification(): Notification {
        val intent = Intent(this, TraceMeasureActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("위치 추적 중")
            .setContentText("${updateIntervalMillis / 1000}초마다 위치를 기록합니다.\n")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createNotificationChannel() {
        val name = "Location Channel"
        val descriptionText = "Channel for location tracking"
        val channel = NotificationChannel(channelId, name, IMPORTANCE_HIGH).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder1? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        handlerThread.quitSafely()
    }
}
