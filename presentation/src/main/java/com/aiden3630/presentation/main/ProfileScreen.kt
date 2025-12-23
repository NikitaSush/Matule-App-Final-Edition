package com.aiden3630.presentation.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aiden3630.presentation.components.MatuleToggle
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.R as UiKitR
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.aiden3630.presentation.theme.MatuleBlack
import com.aiden3630.presentation.theme.MatuleError
import com.aiden3630.presentation.theme.MatuleInputBg
import com.aiden3630.presentation.theme.MatuleTextGray
import com.aiden3630.presentation.theme.MatuleWhite

@Composable
fun ProfileScreen( onLogoutClick: () -> Unit = {}, viewModel: ProfileViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var isNotificationsEnabled by remember { mutableStateOf(true) }
    val state by viewModel.state.collectAsState()

    // Функция для открытия PDF (ссылки)
    fun openPdf(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatuleWhite)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp)) // Отступ сверху

        // --- 1. Шапка (Имя и Почта) ---
        // Тут нет аватарки в CSS, только текст, но если хочешь - оставь
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Аватарка (если нужна по старому макету)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MatuleInputBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = UiKitR.drawable.ic_profile_black),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MatuleBlack
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 👇 3. ПОДСТАВЛЯЕМ РЕАЛЬНЫЕ ДАННЫЕ
            // Объединяем Имя и Фамилию
            Text(text = "${state.name} ${state.surname}", style = Title1)

            Spacer(modifier = Modifier.height(4.dp))

            // Почта
            Text(text = state.email, style = Headline, color = MatuleTextGray)
        }

        Spacer(modifier = Modifier.height(40.dp))

        // --- 2. Меню ---

        // Мои заказы
        ProfileMenuItem(
            title = "Мои заказы",
            iconRes = UiKitR.drawable.ic_notification
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Уведомления (с тогглом)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MatuleWhite, RoundedCornerShape(12.dp))
                // Тень можно добавить, но в CSS там белый фон
                .padding(horizontal = 10.dp), // Отступы внутри
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка
            Icon(
                painter = painterResource(id = UiKitR.drawable.ic_settings), // Временно профиль, нужна шестеренка
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Text(text = "Уведомления", style = Title3, modifier = Modifier.weight(1f))

            // 👇 ОБНОВЛЕННЫЙ ТОГГЛ
            MatuleToggle(
                checked = state.isNotificationsEnabled, // Берем из ViewModel
                onCheckedChange = { isEnabled ->
                    viewModel.toggleNotifications(isEnabled) // Сохраняем изменение
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Прижимаем остальное вниз

        // --- 3. Подвал (Footer) ---

        Text(
            text = "Политика конфиденциальности",
            style = Caption,
            color = MatuleTextGray,
            modifier = Modifier.clickable {
                // Ссылка на PDF (пока заглушка Google, на чемпионате дадут реальную)
                openPdf("https://google.com")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Пользовательское соглашение",
            style = Caption,
            color = MatuleTextGray,
            modifier = Modifier.clickable {
                openPdf("https://google.com")
            }
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Выход",
            style = Title3.copy(color = MatuleError),
            modifier = Modifier.clickable {
                // 👇 4. Очищаем токен при выходе
                viewModel.logout()
                onLogoutClick()
            }
        )

        Spacer(modifier = Modifier.height(100.dp)) // Отступ под BottomBar
    }
}

// Вспомогательный компонент для пункта меню
@Composable
fun ProfileMenuItem(title: String, iconRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MatuleWhite)
            .clickable { }
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, style = Title3, modifier = Modifier.weight(1f))

        // Стрелочка вправо
        Icon(
            painter = painterResource(id = UiKitR.drawable.ic_chevron_left), // Надо развернуть
            contentDescription = null,
            tint = MatuleBlack,
            modifier = Modifier.size(24.dp).rotate(180f)
        )
    }
}

