package com.aiden3630.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.theme.MatuleBlack
import com.aiden3630.presentation.theme.MatuleTextGray
import com.aiden3630.presentation.theme.MatuleWhite

@Composable
fun CreatePinScreen(
    onPinCreated: () -> Unit = {}
) {
    // Состояние введенного пин-кода (строка)
    var pinCode by remember { mutableStateOf("") }

    // Функция обработки нажатий
    fun onNumberClick(number: String) {
        if (pinCode.length < 4) {
            pinCode += number
            // Если ввели 4-ю цифру -> готово
            if (pinCode.length == 4) {
                onPinCreated()
            }
        }
    }

    fun onDeleteClick() {
        if (pinCode.isNotEmpty()) {
            pinCode = pinCode.dropLast(1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatuleWhite)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. Заголовки ---
        Spacer(modifier = Modifier.height(103.dp)) // Отступ как везде

        // В макете тут нет руки, просто текст
        Text(
            text = "Создайте пароль", // На скрине "Создайте пароль", хотя это ПИН
            style = Title1,
            color = MatuleBlack
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Для защиты ваших персональных данных",
            style = BodyText,
            color = MatuleTextGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(50.dp))

        // --- 2. Индикатор (точки) ---
        PinIndicator(codeLength = pinCode.length)

        Spacer(modifier = Modifier.weight(1f)) // Толкаем клавиатуру вниз

        // --- 3. Клавиатура ---
        NumberKeyboard(
            onNumberClick = { onNumberClick(it) },
            onDeleteClick = { onDeleteClick() }
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}

