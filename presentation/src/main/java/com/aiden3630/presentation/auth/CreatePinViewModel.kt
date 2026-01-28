package com.aiden3630.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.data.manager.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePinViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {
    fun savePin(pin: String) {
        viewModelScope.launch {
            tokenManager.savePin(pin)
        }
    }
}