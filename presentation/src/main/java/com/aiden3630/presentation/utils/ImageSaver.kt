package com.aiden3630.presentation.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun saveUriToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        // Создаем уникальное имя файла
        val fileName = "project_img_${System.currentTimeMillis()}.jpg"

        // Открываем поток чтения из временного Uri
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null

        // Создаем файл во внутренней памяти
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)

        // Копируем
        inputStream.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        // Возвращаем абсолютный путь к файлу (строку)
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}