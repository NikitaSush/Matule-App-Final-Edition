package com.aiden3630.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.theme.MatuleBlack
import com.aiden3630.presentation.theme.MatuleBlue
import com.aiden3630.presentation.theme.MatuleInputBg
import com.aiden3630.presentation.theme.MatuleWhite
import com.aiden3630.presentation.R as UiKitR

// --- Компонент Индикатора (OOOO) ---
@Composable
fun PinIndicator(codeLength: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(4) { index ->
            val isFilled = index < codeLength
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(
                        color = if (isFilled) MatuleBlue else MatuleWhite,
                        shape = CircleShape
                    )
                    .then(
                        if (!isFilled) Modifier.border(1.dp, MatuleBlue, CircleShape) else Modifier
                    )
            )
        }
    }
}

// --- Компонент Клавиатуры ---
@Composable
fun NumberKeyboard(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(null, "0", "del")
    )

    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        keys.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                row.forEach { key ->
                    if (key == null) {
                        Spacer(modifier = Modifier.size(80.dp))
                    } else if (key == "del") {
                        Box(
                            modifier = Modifier.size(80.dp).clickable { onDeleteClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = UiKitR.drawable.ic_delete),
                                contentDescription = "Delete",
                                tint = MatuleBlack
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .background(MatuleInputBg)
                                .clickable { onNumberClick(key) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = key, style = Title1, color = MatuleBlack)
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}