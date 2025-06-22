package com.duc.karaoke_app.core.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.duc.karaoke_app.R
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.notification?.let {
            val title = it.title ?: "Thông báo"
            val message = it.body ?: ""
            showNotification(title, message)
        }
    }

    private fun showNotification(title: String, message: String){
        val channelId = "default_channel_id"
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.music) // Icon thông báo của bạn
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        val sharedPref = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val oldToken = sharedPref.getString("fcm_token", "")

        if (newToken != oldToken) {
            Log.d("FCMService", "FCM token changed.\nOld: $oldToken\nNew: $newToken")
            sharedPref.edit()
                .putString("fcm_token", newToken)
                .apply()
        } else {
            Log.d("FCMService", "✅ Token không đổi, không cần cập nhật")
        }
    }

}