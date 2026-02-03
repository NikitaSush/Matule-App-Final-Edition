package com.aiden3630.domain.repository

import com.aiden3630.domain.model.UserProject
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    // 👇 Обновили сигнатуру: добавили dateEnd, category, toWhom, source
    suspend fun createProject(
        name: String,
        type: String,
        dateStart: String,
        dateEnd: String,
        imageUri: String?,
        category: String,
        toWhom: String,
        source: String
    )

    fun getAllProjects(): Flow<List<UserProject>>

    suspend fun getProjectById(id: String): UserProject?

    suspend fun deleteProject(id: String)

    suspend fun updateProject(project: UserProject)
}