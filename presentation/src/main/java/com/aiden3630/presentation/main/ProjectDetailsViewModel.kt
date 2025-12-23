package com.aiden3630.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.domain.model.UserProject
import com.aiden3630.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {

    // Состояние: загруженный проект (может быть null, пока грузится)
    private val _project = MutableStateFlow<UserProject?>(null)
    val project = _project.asStateFlow()

    // Метод загрузки по ID
    fun loadProject(id: String) {
        viewModelScope.launch {
            val result = repository.getProjectById(id)
            _project.value = result
        }
    }
}