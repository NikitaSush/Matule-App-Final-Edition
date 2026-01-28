package com.aiden3630.presentation.theme

import androidx.compose.ui.graphics.Color

// ---  Brand Colors ---
val MatuleBlue = Color(0xFF1A6FEE)         // Accent (Основной синий)
val MatuleBlueDisable = Color(0xFFC5D2FF)  // Accent Inactive (Бледно-синий)
val MatuleSuccess = Color(0xFF00B412)      // Success (Зеленый)
val MatuleError = Color(0xFFFF4646)        // Error (Красный)

// ---  Text Colors ---
val MatuleBlack = Color(0xFF2B2B2B)        // Black (Основной текст) - оставим как было, в палитре #2D2C2C это тоже Black
val MatuleTextGray = Color(0xFF8787A1)     // Description (Серый текст)
val MatulePlaceholder = Color(0xFF98989A)  // Placeholder (Заглушка)
val MatuleWhite = Color(0xFFFFFFFF)        // White

// ---  Backgrounds & Strokes ---
val MatuleInputBg = Color(0xFFF7F7FA)      // Инпут бг (Фон поля ввода)
val MatuleInputStroke = Color(0xFFE6E6E6)  // Инпут строк (Рамка поля)
val MatuleInputIcon = Color(0xFFBFC7D1)    // Инпут иконки
val MatuleCardStroke = Color(0xFFF2F2F2) // Кард строк (Рамка карточек?)
val MatuleGrayIcon = Color(0xFFB8C1CC)
// Остальное (что не в палитре, но нужно)
val MatuleBackground = Color(0xFFF7F7F9)   // Общий фон
val MatuleBlueFocused = MatuleBlue         // Фокус (обычно равен акценту)
val MatuleRedBg = Color(0x1AFF4646)        // Фон ошибки (10% прозрачность)