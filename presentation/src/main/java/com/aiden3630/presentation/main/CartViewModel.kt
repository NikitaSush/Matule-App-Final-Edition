package com.aiden3630.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.data.manager.CartManager
import com.aiden3630.domain.model.Product
import com.aiden3630.presentation.utils.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartManager: CartManager,
    private val notificationService: NotificationService
) : ViewModel() {

    // Подписываемся на изменения в менеджере
    val cartItems = cartManager.cartItems
    private val _cartEvent = Channel<CartEvent>()
    val cartEvent = _cartEvent.receiveAsFlow()

    val totalSum = cartManager.totalPrice
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    fun onPlusClick(product: Product) {
        cartManager.addToCart(product)
    }

    fun onMinusClick(product: Product) {
        cartManager.decreaseQuantity(product)
    }

    fun onDeleteClick(product: Product) {
        cartManager.removeFromCart(product)
    }

    fun onClearCartClick() {
        cartManager.clearCart()
    }
    fun checkout() {
        viewModelScope.launch {
            // Запоминаем сумму перед очисткой, чтобы показать в уведомлении
            val currentSum = totalSum.value

            // Чистим корзину
            cartManager.clearCart()

            // 👇 2. ВЫЗЫВАЕМ УВЕДОМЛЕНИЕ
            notificationService.showNotification(
                title = "Заказ успешно оформлен!",
                message = "Сумма заказа: $currentSum ₽. Спасибо за покупку!"
            )

            // Отправляем событие для навигации
            _cartEvent.send(CartEvent.OrderSuccess)
        }
    }
}
sealed class CartEvent {
    object OrderSuccess : CartEvent()
}