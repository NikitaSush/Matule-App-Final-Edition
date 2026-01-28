package com.aiden3630.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aiden3630.presentation.theme.*

@Composable
fun CreatePinScreen(
    onPinCreated: () -> Unit = {},
    // Используем правильную ViewModel вместо прямого менеджера
    viewModel: CreatePinViewModel = hiltViewModel()
) {
    // Состояние введенного пин-кода
    var pinCode by remember { mutableStateOf("") }

    // Следим за вводом: как только 4 цифры — сохраняем и уходим
    LaunchedEffect(pinCode) {
        if (pinCode.length == 4) {
            viewModel.savePin(pinCode)
            onPinCreated()
        }
    }

    // Это ОБЫЧНЫЕ функции для обработки нажатий (БЕЗ @Composable)
    fun onNumberClick(number: String) {
        if (pinCode.length < 4) {
            pinCode += number
        }
    }

    fun onDeleteClick() {
        if (pinCode.isNotEmpty()) {
            pinCode = pinCode.dropLast(1)
        }
    }

    // ВЕРСТКА
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatuleWhite)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(103.dp))

        Text(
            text = "Создайте пароль",
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

        // Точки-индикаторы
        PinIndicator(codeLength = pinCode.length)

        Spacer(modifier = Modifier.weight(1f))

        // Клавиатура
        NumberKeyboard(
            onNumberClick = { number -> onNumberClick(number) },
            onDeleteClick = { onDeleteClick() }
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}