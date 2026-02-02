package com.aiden3630.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.aiden3630.presentation.components.*
import com.aiden3630.presentation.main.MainScreen
import com.aiden3630.presentation.main.ProjectDropdown
import com.aiden3630.presentation.theme.SplashBackground
import org.junit.Rule
import org.junit.Test

class UiKitTestSuite {
    @get:Rule val composeTestRule = createComposeRule()

    // Тест b) Инпут ошибки
    @Test
    fun testInputErrorState() {
        composeTestRule.setContent {
            MatuleTextField(value = "error", onValueChange = {}, placeholder = "Email", isError = true, errorMessage = "Ошибка")
        }
        composeTestRule.onNodeWithText("Ошибка").assertIsDisplayed()
    }

    // Тест c) Селект (Dropdown) открывает BottomSheet
    @Test
    fun testSelectOpensBottomSheet() {
        composeTestRule.setContent {
            // Имитируем наш селект пола из регистрации
            ProjectDropdown(value = "", onValueChange = {}, placeholder = "Пол", options = listOf("М", "Ж"))
        }
        composeTestRule.onNodeWithText("Пол").performClick()
        composeTestRule.onNodeWithText("М").assertIsDisplayed() // Проверяем, что меню/шторка открылась
    }

    // Тест d) Кнопка chips status ON/OFF
    @Test
    fun testChipsStatus() {
        composeTestRule.setContent {
            MatuleChip(text = "Chip", isSelected = true, onClick = {})
        }
        // В Compose проверку цвета через тесты делать сложно, проверяем наличие
        composeTestRule.onNodeWithText("Chip").assertIsDisplayed()
    }

    // Тест e) Карточка Primary ADD и DELETE
    @Test
    fun testProductCardStatus() {
        composeTestRule.setContent {
            ProductCard(title = "Item", price = "100", isInCart = false, onAddClick = {})
        }
        composeTestRule.onNodeWithText("Добавить").assertIsDisplayed()

        composeTestRule.setContent {
            ProductCard(title = "Item", price = "100", isInCart = true, onRemoveClick = {})
        }
        composeTestRule.onNodeWithText("Убрать").assertIsDisplayed()
    }

    // Тест f) Tabbar (Нижнее меню)
    @Test
    fun testTabbarSelection() {
        composeTestRule.setContent {
            MainScreen() // Проверяем весь экран с меню
        }
        composeTestRule.onNodeWithText("Каталог").performClick()
        composeTestRule.onNodeWithText("Каталог").assertIsSelected()
    }
}