package com.aiden3630.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aiden3630.presentation.theme.*
import kotlinx.coroutines.delay
import com.aiden3630.presentation.R

@Composable
fun ErrorBanner(
    message: String?,
    onDismiss: () -> Unit
) {
    // Авто-скрытие через 5 секунд
    LaunchedEffect(message) {
        if (message != null) {
            delay(5000)
            onDismiss()
        }
    }

    AnimatedVisibility(
        visible = message != null,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding() // Чтобы не перекрывалось часами
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MatuleError, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = message ?: "",
                    color = Color.White,
                    style = BodyText,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }
        }
    }
}