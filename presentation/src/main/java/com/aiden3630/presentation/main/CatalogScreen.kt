package com.aiden3630.feature_main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.aiden3630.presentation.main.CartViewModel
import com.aiden3630.presentation.main.ProductDetailsSheet
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.R as UiKitR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    onCartClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}, // 👈 1. Новый коллбек для профиля
    viewModel: CartViewModel = hiltViewModel() // 👈 2. Подключаем ViewModel корзины
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Все") }

    // 👇 3. Слушаем реальные данные корзины
    val cartItems by viewModel.cartItems.collectAsState()
    val cartTotal by viewModel.totalSum.collectAsState()

    // Состояние шторки
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val categories = listOf("Все", "Мужчинам", "Женщинам", "Детям", "Аксессуары")

    // 👇 4. Используем нормальные модели Product (с ID), чтобы корзина понимала, что это
    // ID должны совпадать с теми, что на главной, если товары одинаковые
    val products = listOf(
        Product(1, "Рубашка Воскресенье", 300, "Мужская одежда"),
        Product(2, "Шорты Вторник", 300, "Мужская одежда"),
        Product(3, "Платье Среда", 800, "Женская одежда"),
        Product(4, "Футболка Четверг", 450, "Унисекс"),
        Product(5, "Шарф Пятница", 150, "Аксессуары")
    )

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
                    MatuleSearchField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    // Иконка профиля
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { onProfileClick() }, // 👈 5. Вызываем переход в профиль
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
                            onClick = { selectedCategory = categories[index] }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- 3. Товары ---
            items(products.size) { index ->
                val product = products[index]

                // 👇 6. Проверяем наличие в корзине РЕАЛЬНО
                val isProductInCart = cartItems.any { it.product.id == product.id }

                ProductCard(
                    title = product.title,
                    price = "${product.price} ₽",
                    category = product.category,
                    isInCart = isProductInCart,
                    onAddClick = {
                        viewModel.onPlusClick(product) // 👈 Добавляем в общую корзину
                    },
                    onRemoveClick = {
                        viewModel.onDeleteClick(product) // 👈 Удаляем из общей корзины
                    },
                    onClick = { showBottomSheet = true }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // --- 4. ПЛАВАЮЩАЯ КНОПКА ---
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
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = UiKitR.drawable.ic_cart),
                        contentDescription = null,
                        tint = MatuleWhite,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "В корзину",
                        style = Title3.copy(color = MatuleWhite, fontWeight = FontWeight.SemiBold)
                    )
                }
                Text(
                    text = "$cartTotal ₽",
                    style = Title3.copy(color = MatuleWhite, fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp)
                )
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
                    onAddToCart = {
                        // TODO: Тут тоже можно вызвать viewModel.onPlusClick()
                        showBottomSheet = false
                    }
                )
            }
        }
    }
}