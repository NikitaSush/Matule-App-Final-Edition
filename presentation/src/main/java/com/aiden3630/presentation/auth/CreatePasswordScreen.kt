package com.aiden3630.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aiden3630.presentation.components.MatuleButton
import com.aiden3630.presentation.components.MatuleTextField
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.R as UiKitR

@Composable
fun CreatePasswordScreen(
    onSaveClick: () -> Unit = {}
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Состояния ошибок
    var isPasswordError by remember { mutableStateOf(false) }
    var passwordErrorText by remember { mutableStateOf<String?>(null) }

    // Функция проверки надежности
    fun validatePassword(): Boolean {
        if (password != confirmPassword) {
            isPasswordError = true
            passwordErrorText = "Пароли не совпадают"
            return false
        }

        if (password.length < 8) {
            isPasswordError = true
            passwordErrorText = "Пароль должен быть не менее 8 символов"
            return false
        }

        val passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$")

        if (!password.matches(passwordRegex)) {
            isPasswordError = true
            passwordErrorText = "Пароль должен содержать A-Z, a-z, 0-9 и символы @#$%^&+=!"
            return false
        }

        isPasswordError = false
        passwordErrorText = null
        return true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatuleWhite)
            .padding(horizontal = 20.dp)
    ) {
        // --- Заголовок с Рукой ---
        Spacer(modifier = Modifier.height(103.dp))

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = UiKitR.drawable.im_hand),
                contentDescription = null,
                modifier = Modifier.size(32.dp).padding(end = 8.dp)
            )
            Text("Создание пароля", style = Title1, color = MatuleBlack)
        }

        Spacer(modifier = Modifier.height(23.dp))

        Text(
            text = "Введите новый пароль",
            style = BodyText,
            color = MatuleBlack
        )

        Spacer(modifier = Modifier.height(35.dp))

        // --- Поля ввода ---

        // Новый пароль
        Text("Новый Пароль", style = Caption, color = MatuleBlack)
        Spacer(modifier = Modifier.height(4.dp))
        MatuleTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordError = false // Сброс ошибки при вводе
            },
            placeholder = "********",
            isPassword = true,
            isError = isPasswordError,
            errorMessage = passwordErrorText
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Повторите пароль
        Text("Повторите пароль", style = Caption, color = MatuleBlack)
        Spacer(modifier = Modifier.height(4.dp))
        MatuleTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                isPasswordError = false
            },
            placeholder = "********",
            isPassword = true,
            isError = isPasswordError
        )

        Spacer(modifier = Modifier.height(40.dp))

        // --- Кнопка "Сохранить" ---
        MatuleButton(
            text = "Сохранить", // В макете кнопка перекрыта, но скорее всего там "Сохранить" или "Далее"
            onClick = {
                if (validatePassword()) {
                    onSaveClick()
                }
            },
            enabled = password.isNotEmpty() && confirmPassword.isNotEmpty()
        )
    }
}