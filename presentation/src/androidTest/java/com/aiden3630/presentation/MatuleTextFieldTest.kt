package com.aiden3630.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.aiden3630.presentation.components.MatuleTextField
import org.junit.Rule
import org.junit.Test

class MatuleTextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun textField_showsError() {
        // Рисуем компонент на виртуальном экране
        composeTestRule.setContent {
            MatuleTextField(
                value = "",
                onValueChange = {},
                placeholder = "Email",
                isError = true, // Включаем ошибку
                errorMessage = "Ошибка ввода" // Текст ошибки
            )
        }

        // Ищем текст "Ошибка ввода" и проверяем, что он виден
        composeTestRule.onNodeWithText("Ошибка ввода").assertIsDisplayed()
    }
}