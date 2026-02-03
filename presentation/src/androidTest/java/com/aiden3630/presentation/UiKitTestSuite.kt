package com.aiden3630.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.aiden3630.presentation.components.*
import com.aiden3630.presentation.theme.SplashBackground
import com.aiden3630.presentation.R
import org.junit.Rule
import org.junit.Test

class UiKitTestSuite {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Тест b) Инпут ошибки
    @Test
    fun testInputErrorState() {
        composeTestRule.setContent {
            MatuleTextField(
                value = "test",
                onValueChange = {},
                placeholder = "Email",
                isError = true,
                errorMessage = "Ошибка формата"
            )
        }
        composeTestRule.onNodeWithText("Ошибка формата").assertIsDisplayed()
    }

    // Тест d) Кнопка chips status ON/OFF
    @Test
    fun testChipsStatus() {
        composeTestRule.setContent {
            MatuleChip(text = "Популярные", isSelected = true, onClick = {})
        }
        composeTestRule.onNodeWithText("Популярные").assertIsDisplayed()
    }

    // Тест e) Карточка Primary - состояние ДОБАВИТЬ
    @Test
    fun testProductCardAddStatus() {
        composeTestRule.setContent {
            ProductCard(title = "Товар 1", price = "100", isInCart = false)
        }
        composeTestRule.onNodeWithText("Добавить").assertIsDisplayed()
    }

    // Тест e) Карточка Primary - состояние УБРАТЬ
    @Test
    fun testProductCardRemoveStatus() {
        composeTestRule.setContent {
            ProductCard(title = "Товар 1", price = "100", isInCart = true)
        }
        composeTestRule.onNodeWithText("Убрать").assertIsDisplayed()
    }

    // Тест f) Tabbar (Нижнее меню) - проверка отображения элементов
    @Test
    fun testTabbarDisplay() {
        composeTestRule.setContent {
            // Тестируем сам компонент, передавая ему фейковые данные (без Hilt)
            MatuleBottomBar(
                currentRoute = "home",
                onNavigate = {},
                tabs = listOf(
                    BottomTab("home", "Главная", R.drawable.ic_home),
                    BottomTab("catalog", "Каталог", R.drawable.ic_catalog)
                )
            )
        }
        composeTestRule.onNodeWithText("Главная").assertIsDisplayed()
        composeTestRule.onNodeWithText("Каталог").assertIsDisplayed()
    }
}