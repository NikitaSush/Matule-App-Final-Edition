package com.aiden3630.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.aiden3630.presentation.Route
import com.aiden3630.presentation.theme.MatuleHeadingStyle
import com.aiden3630.presentation.theme.SplashBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController // Класс, который умеет переключать экраны
) {
    // Логика таймера
    LaunchedEffect(key1 = true) {
        delay(2000)

        val isUserAuthorized = false // <-- ПОМЕНЯЙ НА true, ЧТОБЫ ПРОВЕРИТЬ ВХОД ПО ПИНУ

        if (isUserAuthorized) {
            navController.navigate(Route.SIGN_IN_PIN) {
                popUpTo(Route.SPLASH) { inclusive = true }
            }
        } else {
            navController.navigate(Route.SIGN_IN) {
                popUpTo(Route.SPLASH) { inclusive = true }
            }
        }
    }
    // Внешний вид (фон + текст)
    SplashBackground {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Matule",
                style = MatuleHeadingStyle
            )
        }
    }
}