package com.aiden3630.data

import com.aiden3630.data.manager.TokenManager
import com.aiden3630.data.network.AuthApi
import com.aiden3630.data.repository.AuthRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AuthRepositoryTest {

    // Создаем фейковые классы (пустышки)
    private val api = mockk<AuthApi>(relaxed = true)
    private val tokenManager = mockk<TokenManager>(relaxed = true)

    // Создаем реальный репозиторий, но даем ему фейки
    private val repository = AuthRepositoryImpl(api, tokenManager)

    @Test
    fun `signIn saves token and user info`() = runTest {
        // GIVEN (Дано)
        val email = "test@mail.ru"
        val password = "pass"

        // Обманываем репозиторий: говорим, что в базе есть такой юзер
        coEvery { tokenManager.getUsersDb() } returns flowOf(
            """[{"id":"1","email":"test@mail.ru","firstname":"Test","lastname":"User"}]"""
        )

        // WHEN (Когда)
        repository.signIn(email, password)

        // THEN (Тогда проверяем)
        // Убеждаемся, что репозиторий вызвал метод сохранения
        coVerify { tokenManager.saveToken(any()) }
        coVerify { tokenManager.saveUserInfo(email, "Test", "User") }
    }
}