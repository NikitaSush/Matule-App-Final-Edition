package com.aiden3630.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aiden3630.presentation.Route
import com.aiden3630.presentation.components.BottomTab
import com.aiden3630.presentation.components.MatuleBottomBar
import com.aiden3630.presentation.R as UiKitR
@Composable
fun MainScreen(onNavigateToCart: () -> Unit = {}, onNavigateToCreateProject: () -> Unit = {}, onLogout: () -> Unit = {}, onNavigateToProjectDetails: (String) -> Unit = {}) {
    // У главного экрана свой собственный NavController
    val bottomNavController = rememberNavController()


    // Получаем текущий маршрут, чтобы подсветить иконку
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Список вкладок
    val tabs = listOf(
        BottomTab(Route.HOME_TAB, "Главная", UiKitR.drawable.ic_home),
        BottomTab(Route.CATALOG_TAB, "Каталог", UiKitR.drawable.ic_catalog),
        BottomTab(Route.PROJECTS_TAB, "Проекты", UiKitR.drawable.ic_projects),
        BottomTab(Route.PROFILE_TAB, "Профиль", UiKitR.drawable.ic_profile)
    )

    Scaffold(
        bottomBar = {
            MatuleBottomBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    bottomNavController.navigate(route) {
                        popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                tabs = tabs
            )
        }
    ) { innerPadding ->
        // Контент вкладок
        NavHost(
            navController = bottomNavController,
            startDestination = Route.HOME_TAB,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Главная
            composable(Route.HOME_TAB) {
                HomeScreen(
                    onCartClick = onNavigateToCart
                )
            }

            // Каталог
            composable(Route.CATALOG_TAB) {
                CatalogScreen(
                    onCartClick = onNavigateToCart,
                    onProfileClick = {
                        bottomNavController.navigate(Route.PROFILE_TAB) {
                            popUpTo(bottomNavController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            // Проекты
            composable(Route.PROJECTS_TAB) {
                ProjectsScreen(
                    onAddProjectClick = onNavigateToCreateProject,
                    onProjectClick = { projectId ->
                        onNavigateToProjectDetails(projectId)
                    }
                )
            }

            // Профиль
            composable(Route.PROFILE_TAB) {
                ProfileScreen(
                    onLogoutClick = onLogout
                )
            }
        }
    }
}


@Composable
fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}