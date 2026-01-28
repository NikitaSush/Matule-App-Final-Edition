package com.aiden3630.presentation.splash

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aiden3630.presentation.Route
import com.aiden3630.presentation.theme.MatuleHeadingStyle
import com.aiden3630.presentation.theme.SplashBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel() // Класс, который умеет переключать экраны
) {
    // Логика таймера
    val destination by viewModel.startDestination.collectAsState()

    LaunchedEffect(destination) {
        if (destination != null) {
            navController.navigate(destination!!) {
                // Удаляем Сплэш из истории, чтобы нельзя было вернуться назад
                popUpTo(Route.SPLASH) { inclusive = true }
            }
        }
    }

    // Внешний вид
    SplashBackground {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Matule",
                style = MatuleHeadingStyle,
                        modifier = Modifier.clickable {
                    navController.navigate(Route.STORYBOOK)
                }
            )
        }
    }
}