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
import com.aiden3630.presentation.theme.MatuleError
import com.aiden3630.presentation.theme.MatuleWhite

@Composable
fun SignInPinScreen(
    correctPin: String = "1234", // Временно: правильный пароль для теста
    onAuthSuccess: () -> Unit = {}
) {
    var pinCode by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    fun onNumberClick(number: String) {
        if (pinCode.length < 4) {
            isError = false // Сбрасываем ошибку при вводе
            pinCode += number

            if (pinCode.length == 4) {
                // ПРОВЕРКА ПАРОЛЯ
                if (pinCode == correctPin) {
                    onAuthSuccess()
                } else {
                    // Ошибка
                    isError = true
                    pinCode = "" // Очищаем поле
                }
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
        Spacer(modifier = Modifier.height(103.dp))

        Text(
            text = "Вход", // Заголовок по макету
            style = Title1,
            color = MatuleBlack
        )

        // Показываем текст ошибки, если пароль неверный
        if (isError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Неверный пароль",
                style = BodyText,
                color = MatuleError,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        PinIndicator(codeLength = pinCode.length)

        Spacer(modifier = Modifier.weight(1f))

        NumberKeyboard(
            onNumberClick = { onNumberClick(it) },
            onDeleteClick = { onDeleteClick() }
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}