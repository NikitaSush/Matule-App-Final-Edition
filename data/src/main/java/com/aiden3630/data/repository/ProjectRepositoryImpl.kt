package com.aiden3630.data.repository

import com.aiden3630.data.manager.TokenManager
import com.aiden3630.domain.model.UserProject
import com.aiden3630.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val tokenManager: TokenManager
) : ProjectRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    override suspend fun createProject(
        name: String,
        type: String,
        dateStart: String,
        dateEnd: String,
        imageUri: String?,
        category: String,
        toWhom: String,
        source: String
    ) {
        val projectsJson = tokenManager.getProjects().first()
        val currentList = try {
            json.decodeFromString<MutableList<UserProject>>(projectsJson)
        } catch (e: Exception) {
            mutableListOf()
        }

        val newProject = UserProject(
            id = UUID.randomUUID().toString(),
            name = name,
            type = type,
            dateStart = dateStart,
            dateEnd = dateEnd, // 👈 Теперь это поле есть в модели
            imageUri = imageUri,
            category = category,
            toWhom = toWhom,
            source = source
        )

        currentList.add(0, newProject)
        tokenManager.saveProjects(json.encodeToString(currentList))
    }

    override fun getAllProjects(): Flow<List<UserProject>> {
        return tokenManager.getProjects().map { jsonString ->
            try {
                json.decodeFromString<List<UserProject>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getProjectById(id: String): UserProject? {
        val projects = getAllProjects().first()
        return projects.find { project -> project.id == id }
    }

    override suspend fun deleteProject(id: String) {
        val projects = getAllProjects().first().filter { project -> project.id != id }
        tokenManager.saveProjects(json.encodeToString(projects))
    }

    override suspend fun updateProject(project: UserProject) {
        val projectsJson = tokenManager.getProjects().first()
        val currentList = try {
            json.decodeFromString<MutableList<UserProject>>(projectsJson)
        } catch (e: Exception) {
            mutableListOf()
        }

        val index = currentList.indexOfFirst { it.id == project.id }
        if (index != -1) {
            currentList[index] = project
            tokenManager.saveProjects(json.encodeToString(currentList))
        }
    }
}