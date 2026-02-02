package com.aiden3630.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aiden3630.presentation.Route
import com.aiden3630.presentation.components.BottomTab
import com.aiden3630.presentation.components.MatuleBottomBar
import com.aiden3630.presentation.theme.MatuleBlue
import kotlinx.coroutines.flow.first
import com.aiden3630.presentation.R as UiKitR
import kotlinx.coroutines.launch

/**
 * Основной контейнер приложения после авторизации.
 * Управляет навигацией между вкладками и защищает от некорректных маршрутов из памяти.
 */
@Composable
fun MainScreen(
    onNavigateToCart: () -> Unit = {},
    onNavigateToCreateProject: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateToProjectDetails: (String) -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val bottomNavController = rememberNavController()
    val tokenManager = viewModel.tokenManager
    val scope = rememberCoroutineScope()

    // 👇 1. Храним начальную вкладку отдельно, чтобы она НЕ МЕНЯЛАСЬ при переключениях
    var initialTab by remember { mutableStateOf<String?>(null) }

    // 👇 2. Загружаем её из памяти ТОЛЬКО ОДИН РАЗ при старте экрана
    LaunchedEffect(Unit) {
        val savedRoute = tokenManager.getLastRoute().first()
        // Проверяем на валидность (вкладка это или нет)
        initialTab = when (savedRoute) {
            Route.HOME_TAB, Route.CATALOG_TAB, Route.PROJECTS_TAB, Route.PROFILE_TAB -> savedRoute
            else -> Route.HOME_TAB
        }
    }

    // 👇 3. Сохранение в память оставляем, но оно больше не будет дергать NavHost
    LaunchedEffect(bottomNavController) {
        bottomNavController.currentBackStackEntryFlow.collect { backStackEntry ->
            val currentRoute = backStackEntry.destination.route
            if (currentRoute != null && currentRoute.contains("_tab")) {
                tokenManager.saveLastRoute(currentRoute)
            }
        }
    }

    // Пока вкладка не загружена из памяти, показываем пустой экран или индикатор
    if (initialTab == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MatuleBlue)
        }
    } else {
        // РИСУЕМ ВЕСЬ ЭКРАН ТОЛЬКО КОГДА initialTab ГОТОВ
        Scaffold(
            bottomBar = {
                // Берем текущий путь для подсветки иконок
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val tabs = listOf(
                    BottomTab(Route.HOME_TAB, "Главная", UiKitR.drawable.ic_home),
                    BottomTab(Route.CATALOG_TAB, "Каталог", UiKitR.drawable.ic_catalog),
                    BottomTab(Route.PROJECTS_TAB, "Проекты", UiKitR.drawable.ic_projects),
                    BottomTab(Route.PROFILE_TAB, "Профиль", UiKitR.drawable.ic_profile)
                )

                MatuleBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (currentRoute != route) { // Защита от повторного нажатия на ту же вкладку
                            bottomNavController.navigate(route) {
                                popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    tabs = tabs
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = bottomNavController,
                startDestination = initialTab!!, // 👈 Используем зафиксированную при старте вкладку
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Route.HOME_TAB) { HomeScreen(onCartClick = onNavigateToCart) }
                composable(Route.CATALOG_TAB) {
                    CatalogScreen(
                        onCartClick = onNavigateToCart,
                        onProfileClick = { bottomNavController.navigate(Route.PROFILE_TAB) }
                    )
                }
                composable(Route.PROJECTS_TAB) {
                    ProjectsScreen(
                        onAddProjectClick = onNavigateToCreateProject,
                        onProjectClick = onNavigateToProjectDetails
                    )
                }
                composable(Route.PROFILE_TAB) { ProfileScreen(onLogoutClick = onLogout) }
            }
        }
    }
}