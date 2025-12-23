package com.aiden3630.domain.repository

interface AuthRepository {
    // Функции могут выбрасывать ошибки (Exception), поэтому suspend
    suspend fun signIn(email: String, password: String)

    suspend fun signUp(email: String, password: String, name: String, surname: String)
}