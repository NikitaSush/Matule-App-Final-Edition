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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.widget.Toast

@Composable
fun ProfileScreen( onLogoutClick: () -> Unit = {}, viewModel: ProfileViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var isNotificationsEnabled by remember { mutableStateOf(true) }
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    // Функция для открытия PDF (ссылки)
    fun openPdf(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatuleWhite)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Шапка (Имя и Почта)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Аватарка
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

            // Объединяем Имя и Фамилию
            Text(text = "${state.name} ${state.surname}", style = Title1)

            Spacer(modifier = Modifier.height(4.dp))

            // Почта
            Text(text = state.email, style = Headline, color = MatuleTextGray)
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Меню

        // Мои заказы
        ProfileMenuItem(
            title = "Мои заказы",
            iconRes = UiKitR.drawable.ic_cart,
            onClick = {
                Toast.makeText(context, "Раздел находится в разработке", Toast.LENGTH_SHORT).show()
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Уведомления (с тогглом)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(MatuleWhite, RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка
            Icon(
                painter = painterResource(id = UiKitR.drawable.ic_settings),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Text(text = "Уведомления", style = Title3, modifier = Modifier.weight(1f))

            MatuleToggle(
                checked = state.isNotificationsEnabled,
                onCheckedChange = { isEnabled ->
                    viewModel.toggleNotifications(isEnabled)
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Низ
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Политика конфиденциальности",
            style = Caption,
            color = MatuleTextGray,
            modifier = Modifier.clickable {
                // Ссылка на PDF
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
                //Очищаем токен при выходе
                viewModel.logout()
                onLogoutClick()
            }
        )

        Spacer(modifier = Modifier.height(110.dp))
    }
}

// Вспомогательный компонент для пункта меню
@Composable
fun ProfileMenuItem(title: String, iconRes: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MatuleWhite)
            .clickable { onClick() }
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
            painter = painterResource(id = UiKitR.drawable.ic_chevron_left),
            contentDescription = null,
            tint = MatuleBlack,
            modifier = Modifier.size(24.dp).rotate(180f)
        )
    }
}

