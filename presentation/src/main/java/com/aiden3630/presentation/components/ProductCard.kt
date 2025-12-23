package com.aiden3630.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiden3630.presentation.theme.*

@Composable
fun ProductCard(
    title: String,
    price: String,
    category: String = "Мужская одежда", // Добавил поле для категории (как на скрине)
    isInCart: Boolean = false,
    onAddClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(136.dp) // Высота карточки
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp),
                spotColor = Color(0xFFE4E8F5)
            )
            .background(MatuleWhite, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        // Верхняя часть: Название и Категория
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp) // Оставляем место внизу для цены и кнопки
        ) {
            // Название товара
            Text(
                text = title,
                style = Headline.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Категория (серый текст)
            Text(
                text = category,
                style = Caption.copy(color = MatuleTextGray)
            )
        }

        // Нижняя часть: Цена (слева) и Кнопка (справа)

        // Цена
        Text(
            text = price,
            style = Title3.copy(fontWeight = FontWeight.SemiBold),
            color = MatuleBlack,
            modifier = Modifier.align(Alignment.BottomStart)
        )

        // Кнопка
        val buttonModifier = Modifier
            .align(Alignment.BottomEnd)
            .height(40.dp)
            .width(110.dp) // Чуть шире, чтобы влезло "Добавить"

        if (isInCart) {
            // Если в корзине -> Белая кнопка "Убрать"
            Button(
                onClick = onRemoveClick,
                modifier = buttonModifier,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MatuleWhite),
                border = BorderStroke(1.dp, MatuleBlue),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Убрать",
                    style = Caption.copy(fontWeight = FontWeight.SemiBold, color = MatuleBlue)
                )
            }
        } else {
            // Если нет -> Синяя кнопка "Добавить"
            Button(
                onClick = onAddClick,
                modifier = buttonModifier,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MatuleBlue),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Добавить",
                    style = Caption.copy(fontWeight = FontWeight.SemiBold, color = MatuleWhite)
                )
            }
        }
    }
}