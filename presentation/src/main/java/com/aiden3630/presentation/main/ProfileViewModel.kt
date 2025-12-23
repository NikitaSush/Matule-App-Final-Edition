package com.aiden3630.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.aiden3630.data.manager.TokenManager

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    // Состояние UI: хранит имя, фамилию и почту
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            // Собираем данные из DataStore
            // (в реальности можно сделать combine, но пока сделаем просто)
            tokenManager.getName().collect { name ->
                _state.value = _state.value.copy(name = name)
            }
        }
        viewModelScope.launch {
            tokenManager.getSurname().collect { surname ->
                _state.value = _state.value.copy(surname = surname)
            }
        }
        viewModelScope.launch {
            tokenManager.getEmail().collect { email ->
                _state.value = _state.value.copy(email = email)
            }
        }
        viewModelScope.launch {
            tokenManager.getNotificationsEnabled().collect { enabled ->
                _state.value = _state.value.copy(isNotificationsEnabled = enabled)
            }
        }
    }

    // 👇 Функция переключения
    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            tokenManager.saveNotificationsEnabled(enabled)
            // State обновится сам, так как мы подписаны через collect выше
        }
    }


    fun logout() {
        viewModelScope.launch {
            tokenManager.clearSession()
        }
    }
}

// Класс для хранения данных на экране
data class ProfileState(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val isNotificationsEnabled: Boolean = true
)