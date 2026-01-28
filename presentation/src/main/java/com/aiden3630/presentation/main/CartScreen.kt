package com.aiden3630.presentation.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aiden3630.presentation.components.CartItem
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.theme.MatuleBackground
import com.aiden3630.presentation.theme.MatuleBlack
import com.aiden3630.presentation.theme.MatuleBlue
import com.aiden3630.presentation.theme.MatuleWhite
import com.aiden3630.presentation.R as UiKitR
import androidx.hilt.navigation.compose.hiltViewModel
import com.aiden3630.presentation.main.CartViewModel

@Composable
fun CartScreen(
    onBackClick: () -> Unit = {},onGoHome: () -> Unit = {},
    viewModel: CartViewModel = hiltViewModel()
) {
    // Получаем данные из VM
    val context = LocalContext.current
    val items by viewModel.cartItems.collectAsState()
    val totalSum by viewModel.totalSum.collectAsState()

    LaunchedEffect(true) {
        viewModel.cartEvent.collect { event ->
            when (event) {
                is CartEvent.OrderSuccess -> {
                    Toast.makeText(context, "Заказ успешно оформлен!", Toast.LENGTH_LONG).show()
                    onGoHome() // Уходим на главную
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatuleBackground)
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        // Хедер
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MatuleWhite, RoundedCornerShape(8.dp))
                    .clickable { onBackClick() }
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = UiKitR.drawable.ic_chevron_left),
                    contentDescription = "Back",
                    tint = MatuleBlack
                )
            }
            Text(text = "Корзина", style = Title2, modifier = Modifier.align(Alignment.Center))

            // Иконка мусорки справа
            Icon(
                painter = painterResource(id = UiKitR.drawable.ic_delete),
                contentDescription = "Clear",
                tint = MatuleBlack,
                modifier = Modifier.align(Alignment.CenterEnd).size(24.dp) .clickable { viewModel.onClearCartClick() }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "${items.size} товаров", style = BodyText)
        Spacer(modifier = Modifier.height(16.dp))

        // Список товаров
        if (items.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Корзина пуста", style = Title3, color = MatuleTextGray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items.size) { index ->
                    val cartItem = items[index]
                    CartItem(
                        title = cartItem.product.title,
                        price = "${cartItem.product.price} ₽",
                        count = cartItem.quantity,
                        onPlusClick = { viewModel.onPlusClick(cartItem.product) },
                        onMinusClick = { viewModel.onMinusClick(cartItem.product) },
                        onDeleteClick = { viewModel.onDeleteClick(cartItem.product) }
                    )
                }
            }
        }

        // Итого
        if (items.isNotEmpty()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Сумма", style = Title2)
                    Text("$totalSum ₽", style = Title2)
                }
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = { viewModel.checkout() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MatuleBlue)
                ) {
                    Text("Перейти к оформлению заказа", style = ButtonText)
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}