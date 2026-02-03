package com.aiden3630.presentation.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Создает временный Uri для записи фотографии с камеры.
 */
fun createImageUri(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.externalCacheDir
    val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)

    // Authorities должен совпадать с тем, что в AndroidManifest
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}