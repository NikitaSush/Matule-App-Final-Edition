package com.aiden3630.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.presentation.Route
import com.aiden3630.data.manager.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    val tokenManager: TokenManager
) : ViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            // Ждем 2 секунды
            delay(2000)

            // Читаем токен из памяти (берем первое значение)
            val token = tokenManager.getToken().first()
            if (!token.isNullOrEmpty()) {
                // 👇 ВСЕГДА на ПИН-КОД, если залогинен
                _startDestination.value = Route.SIGN_IN_PIN
            } else {
                _startDestination.value = Route.SIGN_IN
            }
        }
    }
}