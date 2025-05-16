package vn.dihaver.tech.shhh.confession.navigation

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
        startDestination = if (isLoggedIn) NavRoutes.HOME else NavRoutes.AUTH
    ) {
        authNavGraph(navController)
        homeNavGraph(navController)
    }
}
