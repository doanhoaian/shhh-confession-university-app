package vn.dihaver.tech.shhh.confession.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import vn.dihaver.tech.shhh.confession.feature.auth.navigation.authNavGraph
import vn.dihaver.tech.shhh.confession.feature.home.navigation.homeNavGraph

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    isLoggedIn: Boolean
) {

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) NavRoutes.HOME_GRAPH else NavRoutes.AUTH_GRAPH,
        enterTransition = {
            fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.95f)
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300))
        }
    ) {
        authNavGraph(navController)
        homeNavGraph(navController)
    }
}
