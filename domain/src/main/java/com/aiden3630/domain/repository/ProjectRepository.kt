package com.aiden3630.domain.repository

import com.aiden3630.domain.model.UserProject
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    suspend fun createProject(name: String, type: String, dateStart: String, imageUri: String?)
    fun getAllProjects(): Flow<List<UserProject>>
    suspend fun getProjectById(id: String): com.aiden3630.domain.model.UserProject?
}