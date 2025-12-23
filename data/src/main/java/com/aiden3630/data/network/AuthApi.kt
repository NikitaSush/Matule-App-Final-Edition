package com.aiden3630.data.network

import com.aiden3630.data.model.SignInRequest
import com.aiden3630.data.model.SignInResponse
import com.aiden3630.data.model.SignUpRequest
import com.aiden3630.data.model.UserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    // Путь из Swagger: /collections/users/auth-with-password
    @POST("collections/users/auth-with-password")
    suspend fun signIn(@Body request: SignInRequest): SignInResponse

    // Путь из Swagger: /collections/users/records
    // Возвращает UserDto (схема ResponseRegister похожа на User)
    @POST("collections/users/records")
    suspend fun signUp(@Body request: SignUpRequest): UserDto
}