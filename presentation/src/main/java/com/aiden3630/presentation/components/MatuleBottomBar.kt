package com.aiden3630.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.theme.MatuleBlue
import com.aiden3630.presentation.theme.MatuleGrayIcon
import com.aiden3630.presentation.theme.MatuleWhite

// Модель данных для одной вкладки
data class BottomTab(
    val route: String,
    val title: String,
    @DrawableRes val icon: Int
)

@Composable
fun MatuleBottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    tabs: List<BottomTab>
) {
    NavigationBar(
        containerColor = MatuleWhite, // Белый фон
        contentColor = MatuleBlue,    // Цвет эффектов
        tonalElevation = 8.dp         // Тень
    ) {
        tabs.forEach { tab ->
            val selected = currentRoute == tab.route

            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(tab.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = tab.icon),
                        contentDescription = tab.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        style = Caption2.copy(fontSize = 10.sp) // Маленький текст
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MatuleBlue,       // Синяя иконка, если выбрано
                    selectedTextColor = MatuleBlue,       // Синий текст
                    indicatorColor = MatuleWhite,         // Убираем серый овал при выделении (делаем его белым)
                    unselectedIconColor = MatuleGrayIcon, // Серая иконка
                    unselectedTextColor = MatuleGrayIcon  // Серый текст
                )
            )
        }
    }
}