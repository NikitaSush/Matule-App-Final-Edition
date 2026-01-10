package com.aiden3630.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val title: String,
    val price: Int,
    val category: String,
    val description: String = ""
)
data class CartItemModel(
    val product: Product,
    var quantity: Int = 1
)