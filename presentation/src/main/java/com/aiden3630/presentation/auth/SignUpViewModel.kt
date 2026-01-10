package com.aiden3630.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenManager: com.aiden3630.data.manager.TokenManager,
    private val notificationService: com.aiden3630.presentation.utils.NotificationService
) : ViewModel() {

    private val _signUpEvent = Channel<AuthEvent>()
    val signUpEvent = _signUpEvent.receiveAsFlow()

    fun onSignUpClick(name: String, surname: String, email: String, pass: String) {
        viewModelScope.launch {
            try {
                repository.signUp(email, pass, name, surname)
                notificationService.showNotification("Регистрация", "Вы успешно зарегистрировались!")
                _signUpEvent.send(AuthEvent.Success)
            } catch (e: Exception) {
                _signUpEvent.send(AuthEvent.Error(e.message ?: "Ошибка регистрации"))
            }
        }
    }
    fun saveTmpUserInfo(email: String, name: String, surname: String) {
        viewModelScope.launch {
            tokenManager.saveUserInfo(email, name, surname)
        }
    }
}