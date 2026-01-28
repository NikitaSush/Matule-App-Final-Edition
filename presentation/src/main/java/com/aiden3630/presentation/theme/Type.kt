package com.aiden3630.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.aiden3630.presentation.R


// Определение семейств шрифтов

// Специальный шрифт для заставки
val SfProDisplay = FontFamily(
    Font(R.font.sf_pro_display, FontWeight.Normal))
// Основной шрифт приложения
val Roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),   // 400
    Font(R.font.roboto_medium, FontWeight.Medium),    // 500
    Font(R.font.roboto_bold, FontWeight.SemiBold),    // 600
    Font(R.font.roboto_bold, FontWeight.Bold),        // 700
    Font(R.font.roboto_bold, FontWeight.ExtraBold)    // 800
)

// Стиль Заставки (Спринт 3)

val MatuleHeadingStyle = TextStyle(
    fontFamily = SfProDisplay,
    fontWeight = FontWeight.Normal,
    fontSize = 40.sp,
    lineHeight = 64.sp,
    letterSpacing = 1.04.sp,
    color = MatuleWhite,
    textAlign = TextAlign.Center
)

// Стили UI Kit (Спринт 1)

// Title 1 (24px)
val Title1 = TextStyle(
    fontFamily = Roboto,
    fontWeight = FontWeight.SemiBold, // 600
    fontSize = 24.sp,
    lineHeight = 28.sp,
    color = MatuleBlack
)

// Title 2 (20px)
val Title2 = TextStyle(
    fontFamily = Roboto,
    fontWeight = FontWeight.SemiBold, // 600
    fontSize = 20.sp,
    lineHeight = 28.sp,
    color = MatuleBlack
)

// Title 3 (17px)
val Title3 = TextStyle(
    fontFamily = Roboto,
    fontWeight = FontWeight.Medium,   // 500
    fontSize = 17.sp,
    lineHeight = 24.sp,
    color = MatuleBlack
)

// Headline (16px) - Заголовки разделов
val Headline = TextStyle(
    fontFamily = Roboto,
    fontWeight = FontWeight.Medium,   // 500
    fontSize = 16.sp,
    lineHeight = 20.sp,
    color = MatuleBlack
)

// Body / Text (15px) - Основной текст
val BodyText = TextStyle(
    fontFamily = Roboto,
    fontWeight = FontWeight.Normal,   // 400
    fontSize = 15.sp,
    lineHeight = 20.sp,
    color = MatuleBlack
)

// Caption (14px) - Подписи / Hint
val Caption = TextStyle(
    fontFamily = Roboto,
    fontWeight = FontWeight.Normal,   // 400
    fontSize = 14.sp,
    lineHeight = 20.sp,
    color = MatuleTextGray
)

// Caption 2 (12px) - Мелкие подписи
val Caption2 = TextStyle(
    fontFamily = Roboto,
    fontWeight = FontWeight.Normal,   // 400
    fontSize = 12.sp,
    lineHeight = 16.sp,
    color = MatuleTextGray
)

// Кнопки (Button Text) - обычно 17px или 14px Semibold
val ButtonText = TextStyle(
    fontFamily = Roboto,
    fontWeight = FontWeight.SemiBold,
    fontSize = 17.sp,
    lineHeight = 24.sp,
    color = MatuleWhite
)