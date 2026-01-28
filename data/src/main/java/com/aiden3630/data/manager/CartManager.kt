package com.aiden3630.data.manager

import com.aiden3630.domain.model.CartItemModel
import com.aiden3630.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartManager @Inject constructor() {

    // Список товаров
    private val _cartItems = MutableStateFlow<List<CartItemModel>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    // Сумма
    val totalPrice = _cartItems.map { list ->
        list.sumOf { it.product.price * it.quantity }
    }

    // Добавить товар (или увеличить количество)
    fun addToCart(product: Product) {
        val currentList = _cartItems.value

        // Проверяем, есть ли товар
        val existingItem = currentList.find { it.product.id == product.id }

        val newList = if (existingItem != null) {
            // Если товар есть = Создаем НОВЫЙ список, где у нужного товара обновлено количество
            currentList.map { item ->
                if (item.product.id == product.id) {
                    item.copy(quantity = item.quantity + 1)
                } else {
                    item
                }
            }
        } else {
            // Если товара нет = Добавляем новый в конец
            currentList + CartItemModel(product, 1)
        }

        _cartItems.value = newList
    }

    // Уменьшить количество
    fun decreaseQuantity(product: Product) {
        val currentList = _cartItems.value
        val item = currentList.find { it.product.id == product.id } ?: return

        if (item.quantity > 1) {
            // Уменьшаем копированием
            val newList = currentList.map { cartItem ->
                if (cartItem.product.id == product.id) {
                    cartItem.copy(quantity = cartItem.quantity - 1)
                } else {
                    cartItem
                }
            }
            _cartItems.value = newList
        } else {
            // Если 1 шт = удаляем
            removeFromCart(product)
        }
    }

    // Удалить товар
    fun removeFromCart(product: Product) {
        val currentList = _cartItems.value
        // filter создает новый список без удаленного элемента
        val newList = currentList.filter { it.product.id != product.id }
        _cartItems.value = newList
    }

    // Проверка наличия
    fun isInCart(productId: Int): Boolean {
        return _cartItems.value.any { it.product.id == productId }
    }
    fun clearCart() {
        _cartItems.value = emptyList()
    }
}