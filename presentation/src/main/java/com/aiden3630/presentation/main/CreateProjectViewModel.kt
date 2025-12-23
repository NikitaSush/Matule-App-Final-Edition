package com.aiden3630.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.domain.repository.ProjectRepository
import com.aiden3630.presentation.utils.saveUriToInternalStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import android.net.Uri
import javax.inject.Inject

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val repository: ProjectRepository,
    private val notificationService: com.aiden3630.presentation.utils.NotificationService
) : ViewModel() {

    // Добавили Context, чтобы скопировать файл
    fun createProject(
        context: Context, // 👈 Передаем контекст
        name: String,
        type: String,
        dateStart: String,
        imageUri: Uri?, // 👈 Принимаем Uri, а не String
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            // 1. Если есть Uri, копируем файл в память приложения
            val savedImagePath = if (imageUri != null) {
                saveUriToInternalStorage(context, imageUri)
            } else {
                null
            }

            // 2. Сохраняем проект уже с ПУТЕМ к локальному файлу
            repository.createProject(name, type, dateStart, savedImagePath)
            notificationService.showNotification("Новый проект", "Проект '$name' успешно создан!")

            onSuccess()
        }
    }
}