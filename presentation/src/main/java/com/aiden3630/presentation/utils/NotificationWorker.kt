package com.aiden3630.presentation.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val notificationService = NotificationService(
            applicationContext,
            com.aiden3630.data.manager.TokenManager(applicationContext)
        )

        notificationService.showNotification(
            title = "Мы скучаем!",
            message = "Вы не заходили в приложение уже целую минуту. Вернитесь к своим проектам!"
        )

        return Result.success()
    }
}