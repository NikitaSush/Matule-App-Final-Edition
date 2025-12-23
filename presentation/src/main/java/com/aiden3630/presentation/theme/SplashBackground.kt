package com.aiden3630.presentation.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SplashBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    // СЛОЙ 2 (Средний - вертикальный)
    val layer2Middle = Brush.verticalGradient(
        0.0f to Color(0xFF74C8E4),
        0.5052f to Color(0xFF73D5BC),
        1.0f to Color(0xFF74C8E4)
    )

    // СЛОЙ 1 (Верхний - вертикальный, с прозрачностью)
    val layer1Top = Brush.verticalGradient(
        0.0f to Color(red = 98, green = 105, blue = 240, alpha = 13),
        0.2917f to Color(red = 55, green = 64, blue = 245, alpha = 166),
        0.50f to Color(0xFF2254F5),
        0.7135f to Color(red = 55, green = 64, blue = 245, alpha = 166),
        1.0f to Color(red = 98, green = 105, blue = 240, alpha = 13)
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val layer3Bottom = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFA1CAFF),
                    Color(0xFF4D9CFF),
                    Color(0xFFA1CAFF)
                ),
                start = Offset(size.width, 0f),
                end = Offset(0f, size.height)
            )
            drawRect(brush = layer3Bottom)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(layer2Middle, alpha = 0.6f)
                .background(layer1Top)
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashPreview() {
    SplashBackground()
}