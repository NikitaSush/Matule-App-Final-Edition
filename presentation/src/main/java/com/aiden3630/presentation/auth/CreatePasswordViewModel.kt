package com.aiden3630.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.data.manager.TokenManager
import com.aiden3630.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePasswordViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    // Канал для отправки событий экрану (Успех/Ошибка)
    private val _event = Channel<AuthEvent>()
    val event = _event.receiveAsFlow()

    fun finalizeRegistration(password: String) {
        viewModelScope.launch {
            try {
                // 1. Достаем данные, которые пользователь ввел на прошлом экране (анкете)
                // .first() берет текущее значение из Flow в DataStore
                val email = tokenManager.getEmail().first()
                val name = tokenManager.getName().first()
                val surname = tokenManager.getSurname().first()

                // 2. Вызываем РЕАЛЬНУЮ регистрацию в нашем JSON-файле с настоящим паролем
                repository.signUp(
                    email = email,
                    password = password,
                    name = name,
                    surname = surname
                )

                // 3. Если всё ок — шлем сигнал успеха
                _event.send(AuthEvent.Success)

            } catch (e: Exception) {
                // Если ошибка (например, email уже занят) — шлем текст ошибки
                _event.send(AuthEvent.Error(e.message ?: "Ошибка при сохранении"))
            }
        }
    }
}