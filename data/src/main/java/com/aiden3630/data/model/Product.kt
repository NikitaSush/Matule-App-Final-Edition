package com.aiden3630.data.model

data class Product(
    val id: Int,
    val title: String,
    val price: Int,
    val category: String = "Мужская одежда",
    val imageUrl: String = "" // Пока не используем, но пусть будет
)

// Класс для элемента корзины (Товар + Количество)
data class CartItemModel(
    val product: Product,
    var quantity: Int = 1
)