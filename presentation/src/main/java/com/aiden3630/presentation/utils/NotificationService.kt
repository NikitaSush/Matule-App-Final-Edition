package com.aiden3630.presentation.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aiden3630.presentation.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.aiden3630.data.manager.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Singleton
class NotificationService @Inject constructor(
    @ApplicationContext private val context: Context, private val tokenManager: com.aiden3630.data.manager.TokenManager
) {
    companion object {
        const val CHANNEL_ID = "matule_channel_id"
        const val CHANNEL_NAME = "Matule Notifications"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Каналы нужны только для Android 8.0+ (API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH // HIGH - чтобы всплывало сверху
            ).apply {
                description = "Уведомления о статусе заказов и аккаунта"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(title: String, message: String) {
        // 👇 2. Запускаем корутину, чтобы прочитать настройку из DataStore
        CoroutineScope(Dispatchers.IO).launch {
            val isEnabled = tokenManager.getNotificationsEnabled().first()

            if (isEnabled) {
                // Если разрешено -> показываем
                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_round)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .build()

                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(System.currentTimeMillis().toInt(), notification)
            }
        }
    }
}