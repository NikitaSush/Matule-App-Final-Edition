package com.aiden3630.presentation.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.aiden3630.presentation.R


// --- 1. Определение семейств шрифтов ---

// Специальный шрифт для заставки (SF Pro Display)
val SfProDisplay = FontFamily(
    // Если файла нет, замени R.font.sf_pro_display на R.font.roboto_regular временно
    Font(R.font.sf_pro_display, FontWeight.Normal),
    Font(R.font.sf_pro_display, FontWeight.Medium),
    Font(R.font.sf_pro_display, FontWeight.SemiBold)
)

// Основной шрифт приложения (Roboto)
val Roboto = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),   // 400
    Font(R.font.roboto_medium, FontWeight.Medium),    // 500
    Font(R.font.roboto_bold, FontWeight.SemiBold),    // 600 (Используем Bold файл для SemiBold)
    Font(R.font.roboto_bold, FontWeight.Bold),        // 700
    Font(R.font.roboto_bold, FontWeight.ExtraBold)    // 800
)

// --- 2. Стиль Заставки (Спринт 3) ---

val MatuleHeadingStyle = TextStyle(
    fontFamily = SfProDisplay,       // Используем спец. шрифт
    fontWeight = FontWeight.Normal,  // 400
    fontSize = 40.sp,
    lineHeight = 64.sp,
    letterSpacing = 1.04.sp,
    color = MatuleWhite,
    textAlign = TextAlign.Center
)

// --- 3. Стили UI Kit (Спринт 1) ---

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
    color = MatuleTextGray // Серый цвет для подписей
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