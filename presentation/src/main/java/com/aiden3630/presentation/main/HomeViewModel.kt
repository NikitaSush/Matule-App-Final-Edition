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
class HomeViewModel @Inject constructor() : ViewModel() {

    // 1. Исходные данные (пока моки, потом заменим на БД/Сеть)
    private val _allProducts = MutableStateFlow(
        listOf(
            Product(1, "Рубашка Воскресенье", 300, "Мужчинам"),
            Product(2, "Шорты Вторник", 400, "Мужчинам"),
            Product(3, "Платье Среда", 800, "Женщинам"),
            Product(4, "Футболка Четверг", 450, "Детям"),
            Product(5, "Кепка Пятница", 150, "Мужчинам"),
            Product(6, "Блузка Суббота", 600, "Женщинам")
        )
    )

    // 2. Состояние поиска и категории
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Все")
    val selectedCategory = _selectedCategory.asStateFlow()

    // 3. УМНЫЙ СПИСОК (Результат фильтрации)
    // combine следит за изменениями в поиске, категории или списке
    val filteredProducts = combine(_allProducts, _searchText, _selectedCategory) { products, text, category ->
        products.filter { product ->
            // Условие 1: Поиск по названию (игнорируя регистр)
            val matchesSearch = product.title.contains(text, ignoreCase = true)

            // Условие 2: Фильтр по категории (если "Все", то берем всё)
            val matchesCategory = if (category == "Все") true else product.category == category

            matchesSearch && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _allProducts.value
    )

    // Методы для UI
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onCategoryChange(category: String) {
        _selectedCategory.value = category
    }
}