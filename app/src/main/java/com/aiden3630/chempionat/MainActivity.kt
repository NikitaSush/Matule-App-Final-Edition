package com.aiden3630.chempionat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aiden3630.presentation.Route
import com.aiden3630.presentation.main.CreateProjectScreen
import com.aiden3630.presentation.auth.SignInScreen
import com.aiden3630.presentation.splash.SplashScreen
import com.aiden3630.presentation.auth.CreatePasswordScreen
import com.aiden3630.presentation.auth.CreatePinScreen
import com.aiden3630.presentation.auth.SignInPinScreen
import com.aiden3630.presentation.auth.SignUpScreen
import com.aiden3630.presentation.main.CartScreen
import com.aiden3630.presentation.main.MainScreen
import com.aiden3630.presentation.main.ProjectsScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavType
import androidx.navigation.navArgument
import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import com.aiden3630.presentation.utils.NotificationWorker
import kotlinx.coroutines.launch
import com.aiden3630.presentation.main.StoryBookScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            val tokenManager = hiltViewModel<com.aiden3630.presentation.splash.SplashViewModel>().tokenManager

            DisposableEffect(navController) {
                val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
                    val route = destination.route
                    if (route != null &&
                        route != Route.SPLASH &&
                        route != Route.SIGN_IN &&
                        route != Route.SIGN_IN_PIN &&
                        route != Route.STORYBOOK
                    ) {
                        scope.launch { tokenManager.saveLastRoute(route) }
                    }
                }
                navController.addOnDestinationChangedListener(listener)
                onDispose { navController.removeOnDestinationChangedListener(listener) }
            }
            NavHost(
                navController = navController,
                startDestination = Route.SPLASH
            ) {

                composable(Route.SPLASH) {
                    SplashScreen(navController = navController)
                }

                composable(Route.SIGN_IN) {
                    SignInScreen(
                        onSignInClick = {
                            navController.navigate(Route.HOME) {
                                popUpTo(Route.SIGN_IN) { inclusive = true }
                            }
                        },
                        onSignUpClick = {
                            navController.navigate(Route.SIGN_UP)
                        }
                    )
                }

                composable(Route.SIGN_UP) {
                    SignUpScreen(
                        onNextClick = {
                            navController.navigate(Route.CREATE_PASSWORD)
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(Route.CREATE_PASSWORD) {
                    CreatePasswordScreen(
                        onSaveClick = {
                            navController.navigate(Route.CREATE_PIN)
                        }
                    )
                }

                composable(Route.CREATE_PIN) {
                    CreatePinScreen(
                        onPinCreated = {
                            navController.navigate(Route.HOME) {
                                // Очищаем всё до входа, чтобы нельзя было вернуться назад
                                popUpTo(Route.SIGN_IN) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Route.SIGN_IN_PIN) {
                    SignInPinScreen(
                        onAuthSuccess = { lastRoute ->
                            navController.navigate(lastRoute) {
                                popUpTo(Route.SIGN_IN_PIN) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Route.HOME) {
                    MainScreen(
                        onNavigateToCart = {
                            navController.navigate(Route.CART)
                        },
                        onNavigateToCreateProject = {
                            navController.navigate(Route.CREATE_PROJECT)
                        },
                        // Логика выхода
                        onLogout = {
                            navController.navigate(Route.SIGN_IN) {
                                // Удаляем всё из стека, чтобы нельзя было вернуться назад
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToProjectDetails = { projectId ->
                            navController.navigate("${Route.PROJECT_DETAILS}/$projectId")
                        }
                    )

                }
                composable(Route.CART) {
                    com.aiden3630.presentation.main.CartScreen(
                        onBackClick = { navController.popBackStack() },
                        // Если заказ оформлен = идем на ГЛАВНУЮ
                        onGoHome = {
                            navController.navigate(Route.HOME) {
                                // Очищаем стек, чтобы по кнопке "Назад" не вернуться в корзину
                                popUpTo(Route.HOME) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Route.PROJECTS_TAB) {
                    ProjectsScreen(
                        onAddProjectClick = {
                            navController.navigate(Route.CREATE_PROJECT)
                        }
                    )
                }

                // Экран Создания Проекта
                composable(Route.CREATE_PROJECT) {
                    CreateProjectScreen(
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
                composable(
                    route = Route.PROJECT_DETAILS,
                    arguments = listOf(androidx.navigation.navArgument("projectId") {
                        type = androidx.navigation.NavType.StringType
                    })
                ) { backStackEntry ->
                    val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
                    com.aiden3630.presentation.main.ProjectDetailsScreen(
                        projectId = projectId,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable(
                    route = "${Route.PROJECT_DETAILS}/{projectId}",
                    arguments = listOf(navArgument("projectId") { type = NavType.StringType })
                ) { backStackEntry ->
                    // Достаем ID из аргументов
                    val projectId = backStackEntry.arguments?.getString("projectId") ?: ""

                    com.aiden3630.presentation.main.ProjectDetailsScreen(
                        projectId = projectId,
                        onBackClick = { navController.popBackStack() }
                    )
                }
                composable(Route.STORYBOOK) {
                    StoryBookScreen(onBack = { navController.popBackStack() })
                }

            }
        }
    }
    override fun onStop() {
        super.onStop()
        // Создаем задачу "на один раз"
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .addTag("reminder_work")
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "inactive_reminder",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    override fun onStart() {
        super.onStart()
        // Отменяем уведомление, так как он зашел раньше, чем прошла минута
        WorkManager.getInstance(this).cancelUniqueWork("inactive_reminder")
    }
}