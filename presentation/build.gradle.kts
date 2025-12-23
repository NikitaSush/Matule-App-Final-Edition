
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler) // 👈 1. ПЛАГИН КОМПИЛЯТОРА
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.aiden3630.presentation"
    compileSdk = 35

    defaultConfig {
        minSdk = 33
    }

    // 👇 2. ВКЛЮЧЕНИЕ ФУНКЦИИ COMPOSE
    buildFeatures {
        compose = true
    }

    // 👇 ЭТО ТОЖЕ ВАЖНО (настройки Java)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}


dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    // 2. Базовые Android библиотеки
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // 3. 🔥 COMPOSE (Вот этого не хватало!)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3) // Тут живут Text, Button, Column...

    // 4. Навигация и Hilt
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)
    implementation("io.coil-kt:coil-compose:2.5.0")
}