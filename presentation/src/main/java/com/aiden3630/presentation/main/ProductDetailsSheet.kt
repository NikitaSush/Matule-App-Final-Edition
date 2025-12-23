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
    title: String = "Рубашка воскресенье для машинного вязания",
    price: String = "690 ₽",
    onDismiss: () -> Unit,
    onAddToCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f) // Шторка высокая (90% экрана)
            .background(MatuleWhite, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        // --- 1. Шапка (Крестик и Заголовок) ---
        // Крестик справа сверху
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 16.dp)
        ) {
            Icon(
                painter = painterResource(id = UiKitR.drawable.ic_close), // Убедись, что есть ic_close или ic_dismiss
                contentDescription = "Close",
                tint = MatuleGrayIcon,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
                    .clickable { onDismiss() }
            )
        }

        // Заголовок
        Text(
            text = title,
            style = Title1.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        // --- 2. Скроллящийся контент ---
        Column(
            modifier = Modifier
                .weight(1f) // Занимает все место между шапкой и кнопкой
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // Подзаголовок "Описание"
            Text(
                text = "Описание",
                style = Caption.copy(fontSize = 14.sp),
                color = MatuleTextGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Текст описания
            Text(
                text = "Мой выбор для этих шапок – кардные составы, которые раскрываются деликатным пушком. Кашемиры, мериносы, смесовки с ними отлично подойдут на шапку.\n" +
                        "Кардные составы берите в большое количество сложений, вязать будем резинку 1х1, плотненько.\n" +
                        "Пряжу 1400-1500м в 100г в 4 сложения, пряжу 700м в 2 сложения. Ориентир для конечной толщины – 300-350м в 100г.\n" +
                        "Артикулы, из которых мы вязали эту модель: Zermatt Zegna Baruffa, Cashfive, Baby Cashmere Loro Piana, Soft Donegal и другие.\n" +
                        "Примерный расход на шапку с подгибом 70-90г.",
                style = BodyText.copy(lineHeight = 22.sp), // Чуть больше межстрочный интервал для читаемости
                color = MatuleBlack
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Примерный расход
            Text(
                text = "Примерный расход:",
                style = Caption.copy(fontSize = 14.sp),
                color = MatuleTextGray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "80-90 г",
                style = Headline.copy(fontWeight = FontWeight.SemiBold),
                color = MatuleBlack
            )
        }

        // --- 3. Кнопка "Добавить" (Прибита к низу) ---
        // Добавим тень сверху кнопки, как на макете
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            MatuleButton(
                text = "Добавить за $price",
                onClick = onAddToCart
            )
        }
    }
}