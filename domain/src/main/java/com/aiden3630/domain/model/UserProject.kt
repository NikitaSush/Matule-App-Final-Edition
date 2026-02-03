package com.aiden3630.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProject(
    val id: String,
    val name: String,
    val type: String,
    val dateStart: String,
    val dateEnd: String,
    val imageUri: String?,
    val category: String,
    val toWhom: String,  // 👈 Добавили
    val source: String
)