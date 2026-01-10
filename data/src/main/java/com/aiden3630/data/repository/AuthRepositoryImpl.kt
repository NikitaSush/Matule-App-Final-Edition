package com.aiden3630.data.repository

import android.util.Log
import com.aiden3630.data.manager.TokenManager
import com.aiden3630.data.manager.JsonDbManager
import com.aiden3630.data.model.UserDto
import com.aiden3630.data.network.AuthApi
import com.aiden3630.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager,
    private val jsonDbManager: JsonDbManager
) : AuthRepository {

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun signIn(email: String, password: String) {
        delay(1000) // Имитация сети

        // 1. Читаем реальный файл
        val usersList = jsonDbManager.getAllUsers()

        // 2. Ищем пользователя по Email И (!) Паролю
        // (В реальном файле теперь хранятся пароли)
        val foundUser = usersList.find {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }

        if (foundUser != null) {
            val fakeToken = "token_${foundUser.id}"

            // Сохраняем сессию
            tokenManager.saveToken(fakeToken)
            tokenManager.saveUserInfo(
                email = foundUser.email ?: email,
                name = foundUser.name,
                surname = foundUser.surname
            )
            Log.d("AuthRepo", "ВХОД УСПЕШЕН из файла: ${foundUser.name}")
        } else {
            // Проверяем, может email есть, но пароль не тот?
            val emailExists = usersList.any { it.email.equals(email, ignoreCase = true) }
            if (emailExists) {
                throw Exception("Неверный пароль")
            } else {
                throw Exception("Пользователь не найден")
            }
        }
    }

    override suspend fun signUp(email: String, password: String, name: String, surname: String) {
        delay(1000)

        // 1. Читаем файл для проверки дубликатов
        val currentUsers = jsonDbManager.getAllUsers()

        if (currentUsers.any { it.email.equals(email, ignoreCase = true) }) {
            throw Exception("Почта уже занята")
        }

        // 2. Создаем объект (С ПАРОЛЕМ!)
        val newUser = UserDto(
            id = UUID.randomUUID().toString(),
            email = email,
            password = password, // 👈 Сохраняем пароль в файл
            name = name,
            surname = surname,
            avatar = null
        )

        // 3. Пишем в файл
        jsonDbManager.addUser(newUser)

        // 4. Автоматический вход
        tokenManager.saveToken("token_${newUser.id}")
        tokenManager.saveUserInfo(email, name, surname)

        Log.d("AuthRepo", "Юзер записан в файл users.json")
    }
}
