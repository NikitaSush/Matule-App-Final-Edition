package com.aiden3630.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    @SerialName("identity") val identity: String,
    @SerialName("password") val password: String
)

@Serializable
data class SignInResponse(
    @SerialName("token") val token: String,
    @SerialName("record") val user: UserDto
)

@Serializable
data class SignUpRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String,
    @SerialName("passwordConfirm") val passwordConfirm: String,
    @SerialName("firstname") val name: String,
    @SerialName("lastname") val surname: String
)

@Serializable
data class UserDto(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String? = null,
    @SerialName("password") val password: String? = null,
    @SerialName("firstname") val name: String,
    @SerialName("lastname") val surname: String,
    @SerialName("avatar") val avatar: String? = null
)