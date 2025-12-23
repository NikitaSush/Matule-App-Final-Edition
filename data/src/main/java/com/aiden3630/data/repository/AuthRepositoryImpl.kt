package com.aiden3630.data.repository

import android.util.Log
import com.aiden3630.data.manager.TokenManager
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
    private val tokenManager: TokenManager
) : AuthRepository {

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun signIn(email: String, password: String) {
        delay(500)

        // Читаем базу
        val usersJson = tokenManager.getUsersDb().first()
        Log.d("AuthRepo", "--- ВХОД ---")
        Log.d("AuthRepo", "Текущая база данных: $usersJson")

        val usersList = try {
            jsonParser.decodeFromString<List<UserDto>>(usersJson)
        } catch (e: Exception) {
            Log.e("AuthRepo", "Ошибка чтения базы при входе: ${e.message}")
            emptyList()
        }

        // Ищем
        val foundUser = usersList.find {
            it.email?.equals(email, ignoreCase = true) == true
        }

        if (foundUser != null) {
            val fakeToken = "token_${foundUser.id}"
            tokenManager.saveToken(fakeToken)
            tokenManager.saveUserInfo(
                email = foundUser.email ?: email,
                name = foundUser.name,
                surname = foundUser.surname
            )
            Log.d("AuthRepo", "ВХОД УСПЕШЕН: найден ${foundUser.email}")
        } else {
            Log.e("AuthRepo", "ОШИБКА: Пользователь $email не найден в списке из ${usersList.size} человек.")
            throw Exception("Пользователь не найден")
        }
    }

    override suspend fun signUp(email: String, password: String, name: String, surname: String) {
        delay(500)
        Log.d("AuthRepo", "--- РЕГИСТРАЦИЯ НАЧАТА ---")

        // 1. Читаем текущую базу
        val usersJson = tokenManager.getUsersDb().first()
        Log.d("AuthRepo", "База ДО записи: $usersJson")

        val currentUsers = try {
            jsonParser.decodeFromString<MutableList<UserDto>>(usersJson)
        } catch (e: Exception) {
            Log.e("AuthRepo", "База повреждена или пуста, создаем новую. Ошибка: ${e.message}")
            mutableListOf()
        }

        // 2. Проверяем дубликаты
        if (currentUsers.any { it.email?.equals(email, ignoreCase = true) == true }) {
            Log.e("AuthRepo", "ОШИБКА: $email уже занят")
            throw Exception("Почта уже занята")
        }

        // 3. Создаем юзера
        val newUser = UserDto(
            id = UUID.randomUUID().toString(),
            email = email,
            name = name,
            surname = surname,
            avatar = null
        )
        currentUsers.add(newUser)

        // 4. Сохраняем
        val newJson = jsonParser.encodeToString(currentUsers)
        Log.d("AuthRepo", "Пытаемся сохранить JSON: $newJson")

        tokenManager.saveUsersDb(newJson)

        // 5. Проверка: сразу читаем обратно, чтобы убедиться
        val checkSave = tokenManager.getUsersDb().first()
        Log.d("AuthRepo", "База ПОСЛЕ записи (проверка): $checkSave")

        // 6. Логиним
        tokenManager.saveToken("token_${newUser.id}")
        tokenManager.saveUserInfo(email, name, surname)
    }
}