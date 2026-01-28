package com.aiden3630.domain.repository

import com.aiden3630.domain.model.UserProject
interface AuthRepository {
    suspend fun signIn(email: String, password: String)

    suspend fun signUp(email: String, password: String, name: String, surname: String)

    suspend fun getProjectById(id: String): UserProject?
}