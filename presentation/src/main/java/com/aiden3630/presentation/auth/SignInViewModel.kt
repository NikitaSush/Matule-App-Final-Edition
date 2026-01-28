package com.aiden3630.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.data.manager.TokenManager
import com.aiden3630.domain.repository.AuthRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager,
    private val notificationService: com.aiden3630.presentation.utils.NotificationService// 👈 Добавляем TokenManager
) : ViewModel() {

    private val _authEvent = Channel<AuthEvent>()
    val authEvent = _authEvent.receiveAsFlow()

    // Состояния для полей
    private val _emailState = MutableStateFlow("")
    val emailState = _emailState.asStateFlow()

    private val _passwordState = MutableStateFlow("")
    val passwordState = _passwordState.asStateFlow()

    init {
        // При запуске загружаем сохраненные данные
        viewModelScope.launch {
            tokenManager.getEmail().collect { savedEmail ->
                _emailState.value = savedEmail
            }
        }
        viewModelScope.launch {
            tokenManager.getPassword().collect { savedPass ->
                _passwordState.value = savedPass
            }
        }
    }

    // Методы для обновления текста из UI
    fun onEmailChange(newValue: String) {
        _emailState.value = newValue
    }

    fun onPasswordChange(newValue: String) {
        _passwordState.value = newValue
    }

    fun onSignInClick() {
        // Берем текущие значения
        val email = _emailState.value
        val password = _passwordState.value

        viewModelScope.launch {
            try {
                // Сохраняем введенные данные перед входом
                tokenManager.saveUserData(email, password)

                // Пробуем войти
                repository.signIn(email, password)
                notificationService.showNotification("Вход выполнен", "Добро пожаловать в Matule!")
                _authEvent.send(AuthEvent.Success)
            } catch (e: Exception) {
                _authEvent.send(AuthEvent.Error(e.message ?: "Ошибка"))
            }
        }
    }
    fun onSocialLogin() {
        viewModelScope.launch {
            // Имитируем задержку сети
            kotlinx.coroutines.delay(500)
            // Говорим "Успех"
            _authEvent.send(AuthEvent.Success)
            notificationService.showNotification("Вход выполнен", "Добро пожаловать в Matule!")
        }
    }
}

sealed class AuthEvent {
    object Success : AuthEvent()
    data class Error(val message: String) : AuthEvent()
}