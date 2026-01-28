package com.aiden3630.presentation.auth

import androidx.lifecycle.ViewModel
import com.aiden3630.data.manager.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInPinViewModel @Inject constructor(
    val tokenManager: TokenManager
) : ViewModel()