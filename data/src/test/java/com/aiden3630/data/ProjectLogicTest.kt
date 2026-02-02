package com.aiden3630.data

import com.aiden3630.data.manager.TokenManager
import com.aiden3630.data.repository.ProjectRepositoryImpl
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ProjectLogicTest {
    private val tokenManager = mockk<TokenManager>(relaxed = true)
    private val repo = ProjectRepositoryImpl(tokenManager)

    @Test // Запрос 11: Список проектов
    fun `test get all projects`() = runTest {
        repo.getAllProjects()
        verify { tokenManager.getProjects() }
    }

    @Test // Запрос 12: Создание проекта
    fun `test create project`() = runTest {
        repo.createProject("Name", "Type", "Date", null)
        coVerify { tokenManager.saveProjects(any()) }
    }
}