package vn.dihaver.tech.shhh.confession.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import vn.dihaver.tech.shhh.confession.feature.home.ui.HomeScreen
import vn.dihaver.tech.shhh.confession.navigation.NavRoutes

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    composable(NavRoutes.HOME_GRAPH) {
        HomeScreen()
    }
}
