package com.aiden3630.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiden3630.presentation.theme.MatuleBlue
import com.aiden3630.presentation.theme.MatuleInputStroke
import com.aiden3630.presentation.theme.MatuleWhite

@Composable
fun MatuleToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    // Используем стандартный Switch, но красим его под макет
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MatuleWhite,
            checkedTrackColor = MatuleBlue,
            uncheckedThumbColor = MatuleWhite,
            uncheckedTrackColor = MatuleInputStroke,
            uncheckedBorderColor = Color.Transparent,
            checkedBorderColor = Color.Transparent
        ),
        modifier = Modifier
            .width(48.dp) // Размеры из CSS
            .height(28.dp)
    )
}