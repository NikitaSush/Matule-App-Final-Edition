package com.aiden3630.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aiden3630.presentation.theme.*
import com.aiden3630.presentation.theme.MatuleBlack
import com.aiden3630.presentation.theme.MatuleError
import com.aiden3630.presentation.theme.MatuleInputBg
import com.aiden3630.presentation.theme.MatuleWhite
import com.aiden3630.presentation.R as UiKitR

@Composable
fun CartItem(
    title: String,
    price: String,
    count: Int,
    onPlusClick: () -> Unit,
    onMinusClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(138.dp) // Высота по макету
            .shadow(4.dp, RoundedCornerShape(12.dp), spotColor = Color(0xFFE4E8F5))
            .background(MatuleWhite, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Название товара
                Text(
                    text = title,
                    style = Headline.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.weight(1f),
                    maxLines = 2
                )

                // Иконка удаления
                Icon(
                    painter = painterResource(id = UiKitR.drawable.ic_delete),
                    contentDescription = "Delete",
                    tint = MatuleError,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onDeleteClick() }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Цена и Счетчик
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = price,
                    style = Title3.copy(fontWeight = FontWeight.SemiBold),
                    color = MatuleBlack
                )

                Spacer(modifier = Modifier.weight(1f))

                // Счетчик
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MatuleInputBg, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        painter = painterResource(id = UiKitR.drawable.ic_minus),
                        contentDescription = "Minus",
                        modifier = Modifier.size(20.dp).clickable { onMinusClick() },
                        tint = MatuleBlack
                    )

                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "$count шт", style = BodyText)
                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        painter = painterResource(id = UiKitR.drawable.ic_plus),
                        contentDescription = "Plus",
                        modifier = Modifier.size(20.dp).clickable { onPlusClick() },
                        tint = MatuleBlack
                    )
                }
            }
        }
    }
}