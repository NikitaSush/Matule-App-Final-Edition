package com.aiden3630.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiden3630.presentation.components.*
import com.aiden3630.presentation.theme.*

@Composable
fun StoryBookScreen(onBack: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatuleWhite)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        MatuleButton(text = "Закрыть StoryBook", onClick = onBack)
        Text("STORYBOOK - UI KIT COMPONENTS", style = Title1)

        // Секция кнопок
        Text("Buttons", style = Title2)
        MatuleButton(text = "Primary Button", onClick = {})
        MatuleButton(text = "Disabled Button", onClick = {}, enabled = false)

        Divider()

        // Секция полей ввода
        Text("Inputs", style = Title2)
        var text1 by remember { mutableStateOf("") }
        MatuleTextField(value = text1, onValueChange = { text1 = it }, placeholder = "Default Input")

        var text2 by remember { mutableStateOf("Wrong input") }
        MatuleTextField(
            value = text2,
            onValueChange = { text2 = it },
            placeholder = "Error State",
            isError = true,
            errorMessage = "Это текст ошибки"
        )

        var pass by remember { mutableStateOf("123456") }
        MatuleTextField(value = pass, onValueChange = { pass = it }, placeholder = "Password", isPassword = true)

        Divider()

        // Секция поиска
        Text("Search", style = Title2)
        var search by remember { mutableStateOf("") }
        MatuleSearchField(value = search, onValueChange = { search = it })

        Divider()

        // Секция категорий
        Text("Chips / Categories", style = Title2)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MatuleChip(text = "Active", isSelected = true, onClick = {})
            MatuleChip(text = "Inactive", isSelected = false, onClick = {})
        }

        Divider()

        // Секция карточек
        Text("Product Cards", style = Title2)
        ProductCard(title = "Пример товара", price = "500 ₽", onClick = {})

        Spacer(modifier = Modifier.height(50.dp))
    }
}