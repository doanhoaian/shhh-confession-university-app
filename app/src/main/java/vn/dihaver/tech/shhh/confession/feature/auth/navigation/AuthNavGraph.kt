package vn.dihaver.tech.shhh.confession.feature.auth.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import vn.dihaver.tech.shhh.confession.feature.auth.model.AuthStep
import vn.dihaver.tech.shhh.confession.feature.auth.ui.ConfirmInfoScreen
import vn.dihaver.tech.shhh.confession.feature.auth.ui.CreatePassScreen
import vn.dihaver.tech.shhh.confession.feature.auth.ui.InputEmailScreen
import vn.dihaver.tech.shhh.confession.feature.auth.ui.InputOtpScreen
import vn.dihaver.tech.shhh.confession.feature.auth.ui.InputPassScreen
import vn.dihaver.tech.shhh.confession.feature.auth.ui.SelectAliasScreen
import vn.dihaver.tech.shhh.confession.feature.auth.ui.SelectSchoolScreen
import vn.dihaver.tech.shhh.confession.feature.auth.ui.StartScreen
import vn.dihaver.tech.shhh.confession.feature.auth.utils.getAuthViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.ConfirmInfoViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.CreatePassViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.InputEmailViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.InputOtpViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.InputPassViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.SelectAliasViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.SelectSchoolViewModel
import vn.dihaver.tech.shhh.confession.feature.auth.viewmodel.StartViewModel
import vn.dihaver.tech.shhh.confession.navigation.NavRoutes

private const val TAG = "AAA-AuthNavGraph"

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {

    navigation(
        route = NavRoutes.Auth.GRAPH,
        startDestination = NavRoutes.Auth.START,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            )
        },
        popEnterTransition = {
            fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
        }
    ) {

        composable(NavRoutes.Auth.START) {
            val authViewModel = getAuthViewModel(navController) ?: return@composable
            val startViewModel = hiltViewModel(
                creationCallback = { factory: StartViewModel.Factory ->
                    factory.create(authViewModel)
                }
            )

            StartScreen(
                viewModel = startViewModel,
                onNext = {
                    val nextRoute = authViewModel.getNextRoute()
                    navController.navigate(nextRoute) {
                        if (nextRoute == NavRoutes.Home.GRAPH) {
                            popUpTo(NavRoutes.Auth.GRAPH) {
                                inclusive = true
                            }
                        }
                    }
//                    navController.navigate(NavRoutes.SELECT_ALIAS)
                }
            )
        }

        composable(NavRoutes.Auth.INPUT_EMAIL) {
            val authViewModel = getAuthViewModel(navController) ?: return@composable
            val inputEmailViewModel = hiltViewModel(
                creationCallback = { factory: InputEmailViewModel.Factory ->
                    factory.create(authViewModel)
                }
            )

            InputEmailScreen(
                viewModel = inputEmailViewModel,
                onNext = { navController.navigate(authViewModel.getNextRoute()) },
                onBack = {
                    authViewModel.popBackStep()
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Auth.INPUT_PASSWORD) {
            val authViewModel = getAuthViewModel(navController) ?: return@composable
            val inputPassViewModel: InputPassViewModel = hiltViewModel(
                creationCallback = { factory: InputPassViewModel.Factory ->
                    factory.create(authViewModel)
                }
            )
            InputPassScreen(
                viewModel = inputPassViewModel,
                onNext = {
                    val nextRoute = authViewModel.getNextRoute()
                    navController.navigate(nextRoute) {
                        if (nextRoute == NavRoutes.Home.GRAPH) {
                            popUpTo(NavRoutes.Auth.GRAPH) {
                                inclusive = true
                            }
                        }
                    }
                },
                onBack = {
                    authViewModel.popBackStep()
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Auth.INPUT_OTP) {
            val authViewModel = getAuthViewModel(navController) ?: return@composable
            val inputOtpViewModel: InputOtpViewModel = hiltViewModel(
                creationCallback = { factory: InputOtpViewModel.Factory ->
                    factory.create(authViewModel)
                }
            )

            InputOtpScreen(
                viewModel = inputOtpViewModel,
                onNext = {
                    navController.navigate(authViewModel.getNextRoute()) {
                        popUpTo(NavRoutes.Auth.INPUT_OTP) {
                            inclusive = true
                        }
                    }
                    authViewModel.removeStepsUpTo(
                        AuthStep.INPUT_OTP,
                        inclusive = true,
                        updateCurrent = false
                    )
                },
                onBack = {
                    authViewModel.popBackStep()
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Auth.CREATE_PASSWORD) {
            val authViewModel = getAuthViewModel(navController) ?: return@composable
            val createPassViewModel: CreatePassViewModel = hiltViewModel(
                creationCallback = { factory: CreatePassViewModel.Factory ->
                    factory.create(authViewModel)
                }
            )

            CreatePassScreen(
                viewModel = createPassViewModel,
                onRestPass = {
                    authViewModel.removeStepsUpTo(
                        AuthStep.INPUT_PASSWORD,
                        inclusive = true,
                        updateCurrent = false
                    )
                    authViewModel.updateCurrentStep(AuthStep.INPUT_PASSWORD)
                    authViewModel.removeStepsUpTo(
                        AuthStep.CREATE_PASSWORD,
                        inclusive = true,
                        updateCurrent = false
                    )

                    navController.popBackStack(NavRoutes.Auth.INPUT_PASSWORD, inclusive = false)
                },
                onNext = {
                    val nextRoute = authViewModel.getNextRoute()
                    navController.navigate(nextRoute) {
                        if (nextRoute == NavRoutes.Home.GRAPH) {
                            popUpTo(NavRoutes.Auth.GRAPH) {
                                inclusive = true
                            }
                        }
                    }
                },
                onBack = {
                    authViewModel.popBackStep()
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Auth.SELECT_ALIAS) {
            val authViewModel = getAuthViewModel(navController) ?: return@composable
            val selectAliasViewModel: SelectAliasViewModel = hiltViewModel(
                creationCallback = { factory: SelectAliasViewModel.Factory ->
                    factory.create(authViewModel)
                }
            )

            SelectAliasScreen(
                viewModel = selectAliasViewModel,
                onNext = { navController.navigate(authViewModel.getNextRoute()) },
                onBack = {
                    authViewModel.popBackStep()
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Auth.SELECT_SCHOOL) {
            val authViewModel = getAuthViewModel(navController) ?: return@composable
            val selectSchoolViewModel: SelectSchoolViewModel = hiltViewModel(
                creationCallback = { factory: SelectSchoolViewModel.Factory ->
                    factory.create(authViewModel)
                }
            )

            SelectSchoolScreen(
                viewModel = selectSchoolViewModel,
                onNext = { navController.navigate(authViewModel.getNextRoute()) },
                onBack = {
                    authViewModel.popBackStep()
                    navController.popBackStack()
                }
            )
        }

        composable(NavRoutes.Auth.CONFIRM_INFO) {
            val authViewModel = getAuthViewModel(navController) ?: return@composable
            val confirmInfoViewModel: ConfirmInfoViewModel = hiltViewModel(
                creationCallback = { factory: ConfirmInfoViewModel.Factory ->
                    factory.create(authViewModel)
                }
            )

            ConfirmInfoScreen(
                viewModel = confirmInfoViewModel,
                onNext = {
                    navController.navigate(NavRoutes.Home.GRAPH) {
                        popUpTo(NavRoutes.Auth.GRAPH) {
                            inclusive = true
                        }
                    }
                },
                onBack = {
                    authViewModel.popBackStep()
                    navController.popBackStack()
                }
            )
        }


    }
}
