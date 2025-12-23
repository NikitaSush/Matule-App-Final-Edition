package com.aiden3630.data.manager

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import androidx.datastore.preferences.core.booleanPreferencesKey

// Создаем хранилище (оно находится вне класса, это нормально)
private val Context.dataStore by preferencesDataStore("auth_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) { // <--- ОТКРЫТИЕ КЛАССА

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val PASSWORD_KEY = stringPreferencesKey("user_password")

        // Данные профиля
        private val NAME_KEY = stringPreferencesKey("user_name")
        private val SURNAME_KEY = stringPreferencesKey("user_surname")
        private val AVATAR_KEY = stringPreferencesKey("user_avatar")

        // База всех пользователей (JSON)
        private val ALL_USERS_DB_KEY = stringPreferencesKey("all_users_db_json")
        private val PROJECTS_DB_KEY = stringPreferencesKey("projects_db_json")

        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications_enabled")
    }

    // --- База данных пользователей (JSON) ---
    fun getUsersDb(): Flow<String> = context.dataStore.data.map { it[ALL_USERS_DB_KEY] ?: "[]" }

    suspend fun saveUsersDb(json: String) {
        context.dataStore.edit { it[ALL_USERS_DB_KEY] = json }
    }
    fun getProjects(): Flow<String> = context.dataStore.data.map { it[PROJECTS_DB_KEY] ?: "[]" }

    // Сохранить список проектов
    suspend fun saveProjects(json: String) {
        context.dataStore.edit { it[PROJECTS_DB_KEY] = json }
    }

    // --- Токен ---
    fun getToken(): Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    suspend fun saveToken(token: String) = context.dataStore.edit { it[TOKEN_KEY] = token }

    // --- Данные текущего пользователя ---
    fun getEmail(): Flow<String> = context.dataStore.data.map { it[EMAIL_KEY] ?: "" }
    fun getPassword(): Flow<String> = context.dataStore.data.map { it[PASSWORD_KEY] ?: "" }
    fun getName(): Flow<String> = context.dataStore.data.map { it[NAME_KEY] ?: "Пользователь" }
    fun getSurname(): Flow<String> = context.dataStore.data.map { it[SURNAME_KEY] ?: "" }
    fun getNotificationsEnabled(): Flow<Boolean> = context.dataStore.data
        .map { it[NOTIFICATIONS_KEY] ?: true }

    suspend fun saveNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS_KEY] = enabled }
    }

    // Сохранение почты и пароля (Autofill)
    suspend fun saveUserData(email: String, pass: String) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
            prefs[PASSWORD_KEY] = pass
        }
    }

    // Сохранение профиля (после входа)
    suspend fun saveUserInfo(email: String, name: String, surname: String) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
            prefs[NAME_KEY] = name
            prefs[SURNAME_KEY] = surname
        }
    }

    // Очистка (Выход)
    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            // prefs.remove(EMAIL_KEY) // Можно оставить email для автозаполнения, если хочешь
            // prefs.remove(PASSWORD_KEY)
            prefs.remove(NAME_KEY)
            prefs.remove(SURNAME_KEY)
            prefs.remove(AVATAR_KEY)
        }
    }

}