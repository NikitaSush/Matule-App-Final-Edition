package com.aiden3630.data

import com.aiden3630.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchLogicTest {

    private val products = listOf(
        Product(1, "Рубашка Воскресенье", 300, "Мужчинам"),
        Product(2, "Шорты Вторник", 400, "Мужчинам"),
        Product(3, "Платье Среда", 800, "Женщинам")
    )

    @Test // Проверка Запроса 6 (Поиск)
    fun `test search filtering by text`() {
        val query = "Рубашка"
        val filtered = products.filter { it.title.contains(query, ignoreCase = true) }

        assertEquals(1, filtered.size)
        assertEquals("Рубашка Воскресенье", filtered[0].title)
    }

    @Test // Проверка фильтрации по категориям
    fun `test search filtering by category`() {
        val category = "Женщинам"
        val filtered = products.filter { it.category == category }

        assertEquals(1, filtered.size)
        assertEquals("Платье Среда", filtered[0].title)
    }
}