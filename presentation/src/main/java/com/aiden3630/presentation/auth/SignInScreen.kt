package com.aiden3630.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aiden3630.presentation.components.MatuleButton
import com.aiden3630.presentation.components.MatuleSocialButton
import com.aiden3630.presentation.components.MatuleTextField
import com.aiden3630.presentation.components.ErrorBanner
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.R as UiKitR

@Composable
fun SignInScreen(
    onSignInClick: () -> Unit = {}, // Навигация на главный экран или ПИН
    onSignUpClick: () -> Unit = {}, // Навигация на экран регистрации
    viewModel: SignInViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Состояние для отображения текста ошибки в баннере
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Подписка на данные из ViewModel
    val email by viewModel.emailState.collectAsState()
    val password by viewModel.passwordState.collectAsState()

    var isEmailError by remember { mutableStateOf(false) }

    fun validateEmail(mail: String): Boolean {
        val emailRegex = "^[a-z0-9_]+@[a-z0-9_]+\\.ru$".toRegex()
        return mail.matches(emailRegex)
    }

    // Обработка событий от ViewModel (Успех или Ошибка сервера)
    LaunchedEffect(Unit) {
        viewModel.authEvent.collect { event ->
            when (event) {
                is AuthEvent.Success -> {
                    Toast.makeText(context, "Успешный вход!", Toast.LENGTH_SHORT).show()
                    onSignInClick()
                }
                is AuthEvent.Error -> {
                    errorMessage = event.message
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Основной контент экрана
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MatuleWhite)
                .padding(horizontal = 20.dp)
        ) {
            // Секция заголовка
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

            // Секция полей ввода
            Spacer(modifier = Modifier.height(35.dp))

            // Поле Email
            Text(text = "Вход по E-mail", style = Caption, color = MatuleBlack)
            Spacer(modifier = Modifier.height(4.dp))

            MatuleTextField(
                value = email,
                onValueChange = {
                    viewModel.onEmailChange(it)
                    isEmailError = false
                },
                placeholder = "example@mail.com",
                isError = isEmailError,
                errorMessage = if (isEmailError) "Неверный формат E-mail" else null
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Поле Пароль
            Text(text = "Пароль", style = Caption, color = MatuleBlack)
            Spacer(modifier = Modifier.height(4.dp))
            MatuleTextField(
                value = password,
                onValueChange = {
                    viewModel.onPasswordChange(it)
                },
                placeholder = "••••••••",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Кнопка входа
            MatuleButton(
                text = "Далее",
                onClick = {
                    if (validateEmail(email)) {
                        viewModel.onSignInClick()
                    } else {
                        isEmailError = true
                        errorMessage = "Проверьте корректность E-mail"
                    }
                },
                // Кнопка активна только если оба поля не пустые
                enabled = email.isNotEmpty() && password.isNotEmpty()
            )

            // Переход на регистрацию
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

            // Социальные сети
            Spacer(modifier = Modifier.height(30.dp))

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

        ErrorBanner(
            message = errorMessage,
            onDismiss = { errorMessage = null }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    SignInScreen()
}