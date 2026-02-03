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
import com.aiden3630.presentation.main.ProjectDetailsScreen
import java.util.concurrent.TimeUnit
import com.aiden3630.presentation.utils.NotificationWorker
import kotlinx.coroutines.launch
import com.aiden3630.presentation.main.StoryBookScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val scope = rememberCoroutineScope()
            // Используем ViewModel для доступа к TokenManager
            val splashViewModel: com.aiden3630.presentation.splash.SplashViewModel = hiltViewModel()
            val tokenManager = splashViewModel.tokenManager

            // Слушатель изменения экранов для сохранения состояния
            DisposableEffect(navController) {
                val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
                    val route = destination.route
                    if (route != null) {
                        // 👇 ЖЕСТКИЙ ФИЛЬТР: Что НЕЛЬЗЯ сохранять как "последний экран"
                        val isForbidden = route == Route.SPLASH ||
                                route == Route.SIGN_IN ||
                                route == Route.SIGN_UP ||
                                route == Route.SIGN_IN_PIN ||
                                route == Route.CREATE_PIN ||
                                route == Route.CREATE_PASSWORD ||
                                route == Route.STORYBOOK ||
                                route.contains("project") // Игнорируем создание, детали и редактирование

                        if (!isForbidden) {
                            scope.launch { tokenManager.saveLastRoute(route) }
                        }
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
                        onNextClick = { navController.navigate(Route.CREATE_PASSWORD) },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(Route.CREATE_PASSWORD) {
                    CreatePasswordScreen(onSaveClick = { navController.navigate(Route.CREATE_PIN) })
                }

                composable(Route.CREATE_PIN) {
                    CreatePinScreen(
                        onPinCreated = {
                            navController.navigate(Route.HOME) {
                                popUpTo(Route.SIGN_IN) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Route.SIGN_IN_PIN) {
                    SignInPinScreen(
                        onAuthSuccess = { lastRoute ->
                            // 👇 УМНЫЙ ВЫБОР ЭКРАНА ПОСЛЕ ПИНА
                            val dest = when {
                                lastRoute.isEmpty() -> Route.HOME
                                lastRoute.contains("project") -> Route.HOME
                                lastRoute == Route.STORYBOOK -> Route.HOME
                                // Если это вкладка меню
                                lastRoute.contains("_tab") -> Route.HOME
                                else -> lastRoute
                            }

                            navController.navigate(dest) {
                                popUpTo(Route.SIGN_IN_PIN) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Route.HOME) {
                    MainScreen(
                        onNavigateToCart = { navController.navigate(Route.CART) },
                        onNavigateToCreateProject = { navController.navigate(Route.CREATE_PROJECT) },
                        onLogout = {
                            navController.navigate(Route.SIGN_IN) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onNavigateToProjectDetails = { projectId ->
                            navController.navigate("${Route.PROJECT_DETAILS}/$projectId")
                        }
                    )
                }

                composable(Route.CART) {
                    CartScreen(
                        onBackClick = { navController.popBackStack() },
                        onGoHome = {
                            navController.navigate(Route.HOME) {
                                popUpTo(Route.HOME) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Route.CREATE_PROJECT) {
                    CreateProjectScreen(onBackClick = { navController.popBackStack() })
                }

                composable(
                    route = "${Route.PROJECT_DETAILS}/{projectId}",
                    arguments = listOf(navArgument("projectId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("projectId") ?: ""
                    ProjectDetailsScreen(
                        projectId = id,
                        onBackClick = { navController.popBackStack() },
                        onEditClick = { idToEdit ->
                            navController.navigate("edit_project/$idToEdit")
                        }
                    )
                }

                composable(
                    route = "edit_project/{projectId}",
                    arguments = listOf(navArgument("projectId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("projectId")
                    CreateProjectScreen(
                        projectId = id,
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
        WorkManager.getInstance(this).cancelUniqueWork("inactive_reminder")
    }
}