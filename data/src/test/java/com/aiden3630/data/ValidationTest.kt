package com.aiden3630.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationTest {

    // Тот самый Regex из нашего ТЗ
    private val emailRegex = "^[a-z0-9_]+@[a-z0-9_]+\\.ru$".toRegex()

    @Test // Проверка требований к Email (Спринт 3)
    fun `test email validation success`() {
        assertTrue("aiden3630@mail.ru".matches(emailRegex))
        assertTrue("test_user123@yandex.ru".matches(emailRegex))
    }

    @Test // Проверка на ошибки в Email
    fun `test email validation failure`() {
        assertFalse("Aiden@mail.ru".matches(emailRegex)) // Заглавные нельзя
        assertFalse("test@gmail.com".matches(emailRegex)) // Только .ru
        assertFalse("test@mail.org".matches(emailRegex))  // Только .ru
        assertFalse("test!@mail.ru".matches(emailRegex))  // Спецсимволы нельзя
    }
}