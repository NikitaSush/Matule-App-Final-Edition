package com.aiden3630.data

import com.aiden3630.data.manager.CartManager
import com.aiden3630.data.repository.ShopRepositoryImpl
import com.aiden3630.domain.model.Product
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ShopLogicTest {
    private val repo = ShopRepositoryImpl(mockk(relaxed = true))
    private val cart = CartManager()

    @Test // Запрос 4, 5: Акции и Каталог
    fun `test fetch products`() = runTest {
        val list = repo.getProducts().first()
        assert(list.isNotEmpty())
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