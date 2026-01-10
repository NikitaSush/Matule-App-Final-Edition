package com.aiden3630.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiden3630.presentation.components.MatuleButton
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.R as UiKitR

@Composable
fun ProductDetailsSheet(
    title: String,
    price: String,
    description: String, // 👈 ВОТ ЭТОГО НЕ ХВАТАЛО
    onDismiss: () -> Unit,
    onAddToCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .background(MatuleWhite, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        // Шапка
        Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, end = 16.dp)) {
            Icon(
                painter = painterResource(id = UiKitR.drawable.ic_close), // Или ic_dismiss
                contentDescription = "Close",
                tint = MatuleGrayIcon,
                modifier = Modifier.align(Alignment.CenterEnd).size(24.dp).clickable { onDismiss() }
            )
        }

        Text(
            text = title,
            style = Title1.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        // Контент
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Описание", // Это просто заголовок
                style = Caption,
                color = MatuleTextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 👇 ВОТ ЗДЕСЬ ДОЛЖНА БЫТЬ ПЕРЕМЕННАЯ
            Text(
                text = description, // 👈 УБЕДИСЬ, ЧТО ЗДЕСЬ ЭТО СЛОВО БЕЗ КАВЫЧЕК
                style = BodyText.copy(lineHeight = 22.sp),
                color = MatuleBlack
            )
        }
        // Кнопка
        Box(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            MatuleButton(text = "Добавить за $price", onClick = onAddToCart)
        }
    }
}