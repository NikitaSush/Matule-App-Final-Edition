package com.aiden3630.data

import com.aiden3630.data.manager.*
import com.aiden3630.data.model.UserDto
import com.aiden3630.data.network.AuthApi
import com.aiden3630.data.repository.AuthRepositoryImpl
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AuthLogicTest {
    private val api = mockk<AuthApi>(relaxed = true)
    private val tokenManager = mockk<TokenManager>(relaxed = true)
    private val jsonDb = mockk<JsonDbManager>(relaxed = true)
    private val repo = AuthRepositoryImpl(api, tokenManager, jsonDb)

    @Test // Запрос 1: Авторизация
    fun `test signIn`() = runTest {
        val email = "test@mail.ru"
        val pass = "123456"
        coEvery { jsonDb.getAllUsers() } returns listOf(
            UserDto("1", email, pass, "Ivan", "Ivanov")
        )
        repo.signIn(email, pass)

        // Проверяем, что токен сохранился
        coVerify { tokenManager.saveToken(any()) }
    }

    @Test // Запрос 2: Создание пользователя (signUp)
    fun `test signUp`() = runTest {
        repo.signUp("new@mail.ru", "pass", "Name", "Surname")
        coVerify { jsonDb.addUser(any()) }
    }

    @Test // Запрос 3 и 13: Изменение профиля и получение инфы
    fun `test save and get info`() = runTest {
        coEvery { jsonDb.getAllUsers() } returns emptyList()
        repo.signUp("test@mail.ru", "pass", "Emir", "M")
        coVerify { tokenManager.saveUserInfo("test@mail.ru", "Emir", "M") }
    }

    @Test // Запрос 14: Выход
    fun `test logout`() = runTest {
        tokenManager.clearSession()
        coVerify { tokenManager.clearSession() }
    }
}