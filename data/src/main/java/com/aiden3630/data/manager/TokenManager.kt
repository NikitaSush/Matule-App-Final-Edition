package com.aiden3630.data.manager

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Создаем хранилище
private val Context.dataStore by preferencesDataStore("auth_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
        private val PASSWORD_KEY = stringPreferencesKey("user_password")

        // Данные профиля
        private val NAME_KEY = stringPreferencesKey("user_name")
        private val SURNAME_KEY = stringPreferencesKey("user_surname")
        private val AVATAR_KEY = stringPreferencesKey("user_avatar")

        // База всех пользователей и проектов (JSON)
        private val ALL_USERS_DB_KEY = stringPreferencesKey("all_users_db_json")
        private val PROJECTS_DB_KEY = stringPreferencesKey("projects_db_json")

        private val NOTIFICATIONS_KEY = booleanPreferencesKey("notifications_enabled")
        private val USER_PIN_KEY = stringPreferencesKey("user_pin_code")
        private val LAST_ROUTE_KEY = stringPreferencesKey("last_route")
    }

    // --- БАЗЫ ДАННЫХ (JSON) ---
    fun getUsersDb(): Flow<String> = context.dataStore.data.map { it[ALL_USERS_DB_KEY] ?: "[]" }
    suspend fun saveUsersDb(json: String) {
        context.dataStore.edit { it[ALL_USERS_DB_KEY] = json }
    }

    fun getProjects(): Flow<String> = context.dataStore.data.map { it[PROJECTS_DB_KEY] ?: "[]" }
    suspend fun saveProjects(json: String) {
        context.dataStore.edit { it[PROJECTS_DB_KEY] = json }
    }

    // --- ТЕКУЩАЯ СЕССИЯ ---
    fun getToken(): Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    fun getEmail(): Flow<String> = context.dataStore.data.map { it[EMAIL_KEY] ?: "" }
    fun getPassword(): Flow<String> = context.dataStore.data.map { it[PASSWORD_KEY] ?: "" }
    fun getName(): Flow<String> = context.dataStore.data.map { it[NAME_KEY] ?: "Пользователь" }
    fun getSurname(): Flow<String> = context.dataStore.data.map { it[SURNAME_KEY] ?: "" }

    fun getNotificationsEnabled(): Flow<Boolean> = context.dataStore.data
        .map { it[NOTIFICATIONS_KEY] ?: true }

    fun getLastRoute(): Flow<String> = context.dataStore.data.map {
        it[LAST_ROUTE_KEY] ?: "home_tab"
    }

    // --- СОХРАНЕНИЕ ДАННЫХ (SETTERS) ---

    suspend fun saveLastRoute(route: String) {
        context.dataStore.edit { it[LAST_ROUTE_KEY] = route }
    }

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

    suspend fun savePin(pin: String) {
        context.dataStore.edit { it[USER_PIN_KEY] = pin }
    }

    fun getPin(): Flow<String?> = context.dataStore.data.map { it[USER_PIN_KEY] }

    // Очистка сессии (при выходе)
    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(NAME_KEY)
            prefs.remove(SURNAME_KEY)
            prefs.remove(AVATAR_KEY)
        }
    }
}