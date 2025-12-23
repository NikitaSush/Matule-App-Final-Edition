package com.aiden3630.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.theme.MatuleBlue
import com.aiden3630.presentation.theme.MatuleBlueDisable
import com.aiden3630.presentation.theme.MatuleWhite

@Composable
fun MatuleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth() // Кнопка на всю ширину
            .height(56.dp), // Высота по макету
        shape = RoundedCornerShape(14.dp), // Закругление (как у полей ввода)
        colors = ButtonDefaults.buttonColors(
            containerColor = MatuleBlue,         // Синий фон
            contentColor = MatuleWhite,          // Белый текст
            disabledContainerColor = MatuleBlueDisable, // Бледно-синий (когда выключена)
            disabledContentColor = MatuleWhite
        )
    ) {
        Text(
            text = text,
            style = ButtonText // Стиль текста кнопки из Type.kt
        )
    }
}