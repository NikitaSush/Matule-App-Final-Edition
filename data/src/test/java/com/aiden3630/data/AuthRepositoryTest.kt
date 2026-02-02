package com.aiden3630.data.repository

import com.aiden3630.data.manager.JsonDbManager
import com.aiden3630.data.manager.TokenManager
import com.aiden3630.data.model.UserDto
import com.aiden3630.data.network.AuthApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AuthRepositoryTest {

    // 1. Создаем фейки
    private val api = mockk<AuthApi>(relaxed = true)
    private val tokenManager = mockk<TokenManager>(relaxed = true)
    // 👇 Добавляем фейковый JsonDbManager
    private val jsonDbManager = mockk<JsonDbManager>(relaxed = true)

    // 2. Создаем репозиторий с ТРЕМЯ аргументами
    private val repository = AuthRepositoryImpl(api, tokenManager, jsonDbManager)

    @Test
    fun `signIn saves token and user info`() = runTest {
        // GIVEN (Дано)
        val email = "test@mail.ru"
        val password = "pass"

        // Настраиваем фейковую базу: когда попросят getAllUsers, верни список с одним юзером
        coEvery { jsonDbManager.getAllUsers() } returns listOf(
            UserDto(
                id = "123",
                email = email,
                password = password,
                name = "TestName",
                surname = "TestSurname"
            )
        )

        // WHEN (Когда)
        repository.signIn(email, password)

        // THEN (Тогда проверяем)
        // Проверяем, что репозиторий сохранил токен
        coVerify { tokenManager.saveToken(any()) }
        // Проверяем, что репозиторий сохранил данные профиля
        coVerify { tokenManager.saveUserInfo(email, "TestName", "TestSurname") }
    }
}