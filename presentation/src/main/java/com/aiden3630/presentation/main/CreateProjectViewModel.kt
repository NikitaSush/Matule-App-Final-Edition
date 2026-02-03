package com.aiden3630.presentation.main

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.domain.model.UserProject
import com.aiden3630.domain.repository.ProjectRepository
import com.aiden3630.presentation.utils.saveUriToInternalStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {

    // Состояние для редактирования существующего проекта
    private val _projectToEdit = MutableStateFlow<UserProject?>(null)
    val projectToEdit = _projectToEdit.asStateFlow()

    /**
     * Загружает данные проекта из репозитория, если мы перешли в режим редактирования.
     */
    fun loadProject(id: String) {
        viewModelScope.launch {
            _projectToEdit.value = repository.getProjectById(id)
        }
    }

    /**
     * Универсальный метод: если id == null — создает новый проект,
     * если id есть — обновляет существующий.
     */
    fun saveOrUpdate(
        context: Context,
        id: String?, // ID проекта (null для создания)
        name: String,
        type: String,
        dateStart: String,
        dateEnd: String,
        category: String,
        toWhom: String,
        source: String,
        imageUri: Uri?, // Новый Uri, если пользователь выбрал фото
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            // 1. Обработка изображения
            // Если пользователь выбрал новое фото — сохраняем его и получаем путь.
            // Если нет — берем путь старого фото из редактируемого проекта.
            val savedPath: String? = imageUri?.let { uri ->
                saveUriToInternalStorage(context, uri)
            } ?: _projectToEdit.value?.imageUri

            // 2. Логика сохранения
            if (id == null) {
                // СОЗДАНИЕ: Передаем все 8 параметров в репозиторий
                repository.createProject(
                    name = name,
                    type = type,
                    dateStart = dateStart,
                    dateEnd = dateEnd,
                    imageUri = savedPath,
                    category = category,
                    toWhom = toWhom,
                    source = source
                )
            } else {
                // ОБНОВЛЕНИЕ: Создаем объект и отправляем в репозиторий
                val updatedProject = UserProject(
                    id = id,
                    name = name,
                    type = type,
                    dateStart = dateStart,
                    dateEnd = dateEnd,
                    imageUri = savedPath,
                    category = category,
                    toWhom = toWhom,
                    source = source
                )
                repository.updateProject(updatedProject)
            }

            // Вызываем коллбек успеха для закрытия экрана
            onSuccess()
        }
    }
}