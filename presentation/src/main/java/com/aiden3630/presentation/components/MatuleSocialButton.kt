package com.aiden3630.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.theme.MatuleBlack
import com.aiden3630.presentation.theme.MatuleInputStroke
import com.aiden3630.presentation.theme.MatuleWhite

@Composable
fun MatuleSocialButton(
    text: String,
    @DrawableRes iconRes: Int, // Ссылка на иконку (R.drawable.ic_vk)
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp), // В макете они чуть выше обычных (60px против 56px)
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MatuleWhite, // Белый фон
            contentColor = MatuleBlack    // Черный текст
        ),
        border = BorderStroke(1.dp, MatuleInputStroke) // Серая обводка (#EBEBEB)
    ) {
        // Ряд: Иконка + Текст
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = BodyText.copy(fontWeight = FontWeight.Medium), // Чуть жирнее обычного
            fontSize = 17.sp,
            color = MatuleBlack
        )
    }
}