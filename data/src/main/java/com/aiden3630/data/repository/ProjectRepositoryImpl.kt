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

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun createProject(name: String, type: String, dateStart: String, imageUri: String?) {
        // 1. Читаем текущий список
        val projectsJson = tokenManager.getProjects().first()
        val currentList = try {
            json.decodeFromString<MutableList<UserProject>>(projectsJson)
        } catch (e: Exception) {
            mutableListOf()
        }

        // 2. Создаем новый проект
        val newProject = UserProject(
            id = UUID.randomUUID().toString(),
            name = name,
            type = type,
            dateStart = dateStart,
            category = "Своё",
            imageUri = imageUri
        )

        // 3. Добавляем и сохраняем
        currentList.add(0, newProject) // Добавляем в начало списка
        val newJson = json.encodeToString(currentList)

        tokenManager.saveProjects(newJson)
    }

    // Метод для получения списка (пригодится для отображения на экране Проектов)
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
        val projectsJson = tokenManager.getProjects().first()
        val list = try {
            json.decodeFromString<List<UserProject>>(projectsJson)
        } catch (e: Exception) { emptyList() }

        return list.find { it.id == id }
    }
}