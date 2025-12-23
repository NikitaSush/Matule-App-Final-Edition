package com.aiden3630.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

import com.aiden3630.presentation.components.MatuleButton
import com.aiden3630.presentation.components.MatuleSocialButton
import com.aiden3630.presentation.components.MatuleTextField
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.theme.MatuleBlack
import com.aiden3630.presentation.theme.MatuleBlue
import com.aiden3630.presentation.theme.MatuleWhite
import com.aiden3630.presentation.R as UiKitR

@Composable
fun SignInScreen(
    onSignInClick: () -> Unit = {}, // Навигация (переход на следующий экран)
    onSignUpClick: () -> Unit = {},
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // 👇 1. БЕРЕМ ДАННЫЕ ИЗ VIEWMODEL (они сами подгрузятся из памяти)
    // collectAsState превращает поток данных (Flow) в состояние Compose
    val email by viewModel.emailState.collectAsState()
    val password by viewModel.passwordState.collectAsState()

    // Состояние ошибки валидации (локальное для UI)
    var isEmailError by remember { mutableStateOf(false) }

    fun validateEmail(mail: String): Boolean {
        // Строгое требование: маленькие буквы, цифры, домен .ru
        val emailRegex = "^[a-z0-9_]+@[a-z0-9_]+\\.ru$".toRegex()
        return mail.matches(emailRegex)
    }

    // Слушаем события (Успех/Ошибка)
    LaunchedEffect(key1 = true) {
        viewModel.authEvent.collect { event ->
            when (event) {
                is AuthEvent.Success -> {
                    Toast.makeText(context, "Успешный вход!", Toast.LENGTH_SHORT).show()
                    onSignInClick() // Переходим дальше
                }
                is AuthEvent.Error -> {
                    Toast.makeText(context, "Ошибка: ${event.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatuleWhite)
            .padding(horizontal = 20.dp)
    ) {
        // --- 1. Заголовок с Рукой ---
        Spacer(modifier = Modifier.height(103.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = UiKitR.drawable.im_hand),
                    contentDescription = "Hello Hand",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp)
                )

                Text(
                    text = "Добро пожаловать!",
                    style = Title1,
                    color = MatuleBlack
                )
            }

            Spacer(modifier = Modifier.height(23.dp))

            Text(
                text = "Войдите, чтобы пользоваться функциями приложения",
                style = BodyText,
                color = MatuleBlack
            )
        }

        // --- 2. Поля ввода ---
        Spacer(modifier = Modifier.height(35.dp))

        // Email
        Text("Вход по E-mail", style = Caption, color = MatuleBlack)
        Spacer(modifier = Modifier.height(4.dp))

        MatuleTextField(
            value = email, // 👇 Значение из ViewModel
            onValueChange = {
                // 👇 Передаем изменения во ViewModel
                viewModel.onEmailChange(it)
                isEmailError = false
            },
            placeholder = "example@mail.com",
            isError = isEmailError,
            errorMessage = if (isEmailError) "Неверный формат E-mail" else null
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- ПАРОЛЬ ---
        Text("Пароль", style = Caption, color = MatuleBlack)
        Spacer(modifier = Modifier.height(4.dp))
        MatuleTextField(
            value = password, // 👇 Значение из ViewModel
            onValueChange = {
                viewModel.onPasswordChange(it) // 👇 Передаем изменения
            },
            placeholder = "••••••••",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(30.dp))

        // --- КНОПКА С ПРОВЕРКОЙ ---
        MatuleButton(
            text = "Далее",
            onClick = {
                if (validateEmail(email)) {
                    // 👇 Вызываем метод входа БЕЗ аргументов (VM сама возьмет их из state)
                    viewModel.onSignInClick()
                } else {
                    isEmailError = true
                }
            },
            // Кнопка активна, если поля не пустые
            enabled = email.isNotEmpty() && password.isNotEmpty()
        )

        // --- 4. Кнопка "Зарегистрироваться" ---
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Зарегистрироваться",
                style = BodyText.copy(color = MatuleBlue),
                modifier = Modifier.clickable { onSignUpClick() }
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        // --- Соцсети ---
        Text(
            text = "Или войдите с помощью",
            style = Caption,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        MatuleSocialButton(
            text = "Войти с VK",
            iconRes = UiKitR.drawable.ic_vk,
            onClick = { viewModel.onSocialLogin() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        MatuleSocialButton(
            text = "Войти с Yandex",
            iconRes = UiKitR.drawable.im_yandex,
            onClick = { viewModel.onSocialLogin() }
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    SignInScreen()
}