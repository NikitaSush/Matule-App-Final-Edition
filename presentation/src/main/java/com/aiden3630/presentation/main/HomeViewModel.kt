package com.aiden3630.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiden3630.domain.model.Product
import com.aiden3630.domain.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val shopRepository: ShopRepository // 👈 Инжектим репозиторий
) : ViewModel() {

    // 1. Исходные данные теперь загружаются из Репозитория
    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())

    // 2. Состояние поиска и категории
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Все")
    val selectedCategory = _selectedCategory.asStateFlow()

    // 3. УМНЫЙ СПИСОК (Результат фильтрации)
    // combine следит за изменениями в поиске, категории или списке товаров
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
        initialValue = emptyList()
    )

    // При создании ViewModel загружаем данные
    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            // Читаем товары из JSON-файла через репозиторий
            shopRepository.getProducts().collect { list ->
                _allProducts.value = list
            }
        }
    }

    // Методы для UI
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onCategoryChange(category: String) {
        _selectedCategory.value = category
    }
}