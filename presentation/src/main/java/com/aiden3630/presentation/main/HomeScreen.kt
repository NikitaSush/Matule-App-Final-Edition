package com.aiden3630.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aiden3630.data.model.Product
import com.aiden3630.presentation.components.MatuleChip
import com.aiden3630.presentation.components.MatuleSearchField
import com.aiden3630.presentation.components.ProductCard
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.R as UiKitR
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.zIndex
import androidx.compose.foundation.lazy.items
import com.aiden3630.presentation.main.HomeViewModel

@Composable
fun HomeScreen(
    onCartClick: () -> Unit = {},
    cartViewModel: CartViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val searchText by homeViewModel.searchText.collectAsState()
    val selectedCategory by homeViewModel.selectedCategory.collectAsState()
    val products by homeViewModel.filteredProducts.collectAsState() // Это уже отфильтрованный список!

    // Данные для корзины (сумма и список покупок) остаются от CartViewModel
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartTotal by cartViewModel.totalSum.collectAsState()

    val categories = listOf("Все", "Мужчинам", "Женщинам", "Детям")

    // ВАЖНО: Box должен быть корневым, чтобы кнопка корзины легла поверх списка
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MatuleWhite)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // --- 1. Поиск ---
            item {
                Spacer(modifier = Modifier.height(20.dp))
                MatuleSearchField(
                    value = searchText,
                    onValueChange = {
                        homeViewModel.onSearchTextChange(it) // 👈 Передаем текст в VM
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- 2. Баннеры ---
            item {
                Text(text = "Акции и новости", style = Title3)
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        BannerItem(
                            title = "Шорты\nВторник",
                            price = "4000 ₽",
                            gradient = Brush.linearGradient(listOf(Color(0xFF97D9F0), Color(0xFF92E9D4))),
                            imageRes = UiKitR.drawable.im_banner_1 // Убедись, что картинка есть
                        )
                    }
                    item {
                        BannerItem(
                            title = "Рубашка\nВоскресенье",
                            price = "8000 ₽",
                            gradient = Brush.linearGradient(listOf(Color(0xFF76B3FF), Color(0xFFCDE3FF))),
                            imageRes = UiKitR.drawable.im_banner_1 // Убедись, что картинка есть
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- 3. Категории ---
            item {
                Text(text = "Каталог описаний", style = Title3)
                Spacer(modifier = Modifier.height(16.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(categories.size) { index ->
                        MatuleChip(
                            text = categories[index],
                            isSelected = selectedCategory == categories[index],
                            onClick = {
                                homeViewModel.onCategoryChange(categories[index]) // 👈 Передаем категорию в VM
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // --- 4. Товары ---
            if (products.isEmpty()) {
                // Если ничего не найдено
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                        Text("Ничего не найдено", style = BodyText, color = MatuleTextGray)
                    }
                }
            } else {
                items(products) { product ->
                    // Проверяем, есть ли товар в корзине
                    val isProductInCart = cartItems.any { it.product.id == product.id }

                    ProductCard(
                        title = product.title,
                        price = "${product.price} ₽",
                        category = product.category, // Показываем категорию
                        isInCart = isProductInCart,
                        onAddClick = { cartViewModel.onPlusClick(product) },
                        onRemoveClick = { cartViewModel.onDeleteClick(product) },
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // --- 5. ПЛАВАЮЩАЯ КНОПКА (ОБНОВЛЯЕТСЯ САМА) ---
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

                // Сумма обновляется сама, так как cartTotal - это State
                Text(
                    text = "$cartTotal ₽",
                    style = Title3.copy(color = MatuleWhite, fontWeight = FontWeight.SemiBold),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                )
            }
        }
    }
}

// Компонент баннера (должен быть вне функции HomeScreen)
@Composable
fun BannerItem(
    title: String,
    price: String,
    gradient: Brush,
    imageRes: Int
) {
    Box(
        modifier = Modifier
            .width(270.dp)
            .height(152.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(gradient)
            .clickable { }
    ) {
        // Слой 1: Текст
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
                .width(140.dp)
                .zIndex(2f) // Текст поверх картинки
        ) {
            Text(
                text = title,
                style = Title2.copy(color = MatuleWhite, fontSize = 20.sp),
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = price,
                style = Title2.copy(color = MatuleWhite, fontSize = 20.sp)
            )
        }

        // Слой 2: Картинка
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            // Используем Fit, чтобы банка не была "жирной", но занимала высоту
            contentScale = androidx.compose.ui.layout.ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomEnd) // Прижимаем к правому нижнему углу
                .height(160.dp) // Высота чуть больше карточки (152dp)
                // 👇 ИСПРАВЛЕНИЕ:
                // x = 15.dp -> Сдвигаем вправо (чтобы часть ушла за край)
                // y = 5.dp  -> Чуть-чуть вниз (чтобы дно не висело в воздухе)
                // Если хочешь ЕЩЕ ВЫШЕ, поставь y = -10.dp
                .offset(x = 5.dp, y = 5.dp)
        )
    }
}