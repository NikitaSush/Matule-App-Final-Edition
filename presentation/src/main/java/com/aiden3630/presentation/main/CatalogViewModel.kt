package com.aiden3630.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor() : ViewModel() {

    // 1. Полный список товаров (потом заменим на загрузку с сервера)
    private val _allProducts = MutableStateFlow(
        listOf(
            Product(1, "Рубашка Воскресенье", 300, "Мужская одежда"),
            Product(2, "Шорты Вторник", 300, "Мужская одежда"),
            Product(3, "Платье Среда", 800, "Женская одежда"),
            Product(4, "Футболка Четверг", 450, "Унисекс"),
            Product(5, "Шарф Пятница", 150, "Аксессуары"),
            Product(6, "Кепка Суббота", 200, "Аксессуары")
        )
    )

    // 2. Текст поиска
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // 3. Выбранная категория
    private val _selectedCategory = MutableStateFlow("Все")
    val selectedCategory = _selectedCategory.asStateFlow()

    // 4. Логика фильтрации (Умный список)
    val filteredProducts = combine(_allProducts, _searchText, _selectedCategory) { products, text, category ->
        products.filter { product ->
            // Фильтр по названию
            val matchesSearch = product.title.contains(text, ignoreCase = true)
            // Фильтр по категории
            val matchesCategory = if (category == "Все") true else product.category == category

            matchesSearch && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _allProducts.value
    )

    // Методы для UI
    fun onSearchChange(text: String) {
        _searchText.value = text
    }

    fun onCategoryChange(category: String) {
        _selectedCategory.value = category
    }
}