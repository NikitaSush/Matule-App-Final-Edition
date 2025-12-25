package com.aiden3630.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aiden3630.data.model.Product
import com.aiden3630.presentation.components.MatuleChip
import com.aiden3630.presentation.components.MatuleSearchField
import com.aiden3630.presentation.components.ProductCard
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.R as UiKitR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onCartClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}, // 👈 1. Новый коллбек для профиля
    cartViewModel: CartViewModel = hiltViewModel(), // VM для корзины
    catalogViewModel: CatalogViewModel = hiltViewModel() // 👈 2. Подключаем ViewModel корзины
) {
    val searchText by catalogViewModel.searchText.collectAsState()
    val selectedCategory by catalogViewModel.selectedCategory.collectAsState()
    val products by catalogViewModel.filteredProducts.collectAsState() // Это отфильтрованный список

    // Данные корзины
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartTotal by cartViewModel.totalSum.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val categories = listOf("Все", "Мужчинам", "Женщинам", "Детям", "Аксессуары")

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MatuleWhite)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // --- 1. Хедер ---
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // 👇 ПОИСК РАБОТАЕТ ЧЕРЕЗ VIEWMODEL
                    MatuleSearchField(
                        value = searchText,
                        onValueChange = { catalogViewModel.onSearchChange(it) }, // Передаем текст
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { onProfileClick() },
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            painter = painterResource(id = UiKitR.drawable.ic_profile_black),
                            contentDescription = "Profile",
                            tint = MatuleBlack,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- 2. Категории ---
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(categories.size) { index ->
                        MatuleChip(
                            text = categories[index],
                            isSelected = selectedCategory == categories[index],
                            onClick = {
                                // 👇 ФИЛЬТР ПО КАТЕГОРИИ
                                catalogViewModel.onCategoryChange(categories[index])
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- 3. Товары ---
            if (products.isEmpty()) {
                item {
                    Text(
                        text = "Ничего не найдено",
                        style = BodyText,
                        color = MatuleTextGray,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            } else {
                items(products) { product ->
                    val isProductInCart = cartItems.any { it.product.id == product.id }

                    ProductCard(
                        title = product.title,
                        price = "${product.price} ₽",
                        category = product.category,
                        isInCart = isProductInCart,
                        onAddClick = { cartViewModel.onPlusClick(product) },
                        onRemoveClick = { cartViewModel.onDeleteClick(product) },
                        onClick = { showBottomSheet = true }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // --- 4. Кнопка Корзины ---
        if (cartTotal > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(10.dp, RoundedCornerShape(12.dp), spotColor = Color(0x40000000))
                    .background(MatuleBlue, RoundedCornerShape(12.dp))
                    .clickable { onCartClick() }
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = UiKitR.drawable.ic_cart),
                        contentDescription = null,
                        tint = MatuleWhite,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("В корзину", style = Title3.copy(color = MatuleWhite, fontWeight = FontWeight.SemiBold))
                }
                Text("$cartTotal ₽", style = Title3.copy(color = MatuleWhite, fontWeight = FontWeight.SemiBold), modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp))
            }
        }

        // --- 5. Шторка ---
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = MatuleWhite,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                ProductDetailsSheet(
                    onDismiss = { showBottomSheet = false },
                    onAddToCart = { showBottomSheet = false }
                )
            }
        }
    }
}