package com.aiden3630.presentation.main // 👈 ВАЖНО: Пакет должен быть таким

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.domain.model.UserProject
import com.aiden3630.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    repository: ProjectRepository
) : ViewModel() {

    val projects: StateFlow<List<UserProject>> = repository.getAllProjects()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}