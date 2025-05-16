package vn.dihaver.tech.shhh.confession.feature.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import vn.dihaver.tech.shhh.confession.feature.auth.ui.StartScreen
import vn.dihaver.tech.shhh.confession.navigation.NavRoutes

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = NavRoutes.AUTH,
        startDestination = NavRoutes.START
    ) {
        composable(NavRoutes.START) {
            StartScreen(
                onLoginClick = { navController.navigate(NavRoutes.LOGIN) },
                onRegisterClick = { navController.navigate(NavRoutes.REGISTER) }
            )
        }

//        composable(NavRoutes.LOGIN) {
//            LoginScreen(
//                onLoginSuccess = {
//                    navController.navigate(NavRoutes.HOME) {
//                        popUpTo(NavRoutes.AUTH) { inclusive = true }
//                    }
//                }
//            )
//        }
//
//        composable(NavRoutes.REGISTER) {
//            RegisterScreen(
//                onRegisterSuccess = {
//                    navController.navigate(NavRoutes.HOME) {
//                        popUpTo(NavRoutes.AUTH) { inclusive = true }
//                    }
//                }
//            )
//        }
    }
}
