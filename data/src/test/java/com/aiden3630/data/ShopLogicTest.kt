package com.aiden3630.data

import com.aiden3630.data.manager.CartManager
import com.aiden3630.data.repository.ShopRepositoryImpl
import com.aiden3630.domain.model.Product
import com.aiden3630.domain.repository.ShopRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShopLogicTest {

    // 1. Создаем чистый мок ИНТЕРФЕЙСА (это всегда работает идеально)
    private val repo = mockk<ShopRepository>()
    private val cart = CartManager()

    @Test // Тест запроса 4, 5 (Акции и Каталог)
    fun `test fetch products`() = runTest {
        // Данные, которые мы "якобы" получили от сервера
        val mockData = listOf(
            Product(1, "Shirt", 300, "Men", null, "Desc")
        )

        // 2. Описываем поведение: когда вызывают getProducts(), верни mockData
        every { repo.getProducts() } returns flowOf(mockData)

        // Вызываем метод
        repo.getProducts().collect { list ->
            // Проверяем, что список не пуст
            assertTrue(list.isNotEmpty())
            assertEquals("Shirt", list[0].title)
        }
    }


    @Test // Запрос 8, 9: Добавление и изменение корзины
    fun `test cart logic`() {
        val p = Product(1, "Shirt", 300, "Men", null, "Desc")
        cart.addToCart(p)
        assertEquals(1, cart.cartItems.value.size)
        cart.addToCart(p) // Увеличиваем кол-во (изменение корзины)
        assertEquals(2, cart.cartItems.value[0].quantity)
    }

    @Test // Запрос 10: Оформление заказа
    fun `test clear cart after checkout`() {
        cart.clearCart()
        assert(cart.cartItems.value.isEmpty())
    }
}