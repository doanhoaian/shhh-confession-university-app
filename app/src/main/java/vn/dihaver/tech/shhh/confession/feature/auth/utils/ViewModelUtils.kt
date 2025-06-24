package vn.dihaver.tech.shhh.confession.feature.auth.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import vn.dihaver.tech.shhh.confession.feature.auth.AuthViewModel
import vn.dihaver.tech.shhh.confession.navigation.NavRoutes

private fun getAuthBackStackEntry(navController: NavHostController): NavBackStackEntry? {
    return try {
        navController.getBackStackEntry(NavRoutes.Auth.GRAPH)
    } catch (e: IllegalArgumentException) {
        null
    }
}

@Composable
fun getAuthViewModel(navController: NavHostController): AuthViewModel? {
    val parentEntry = remember(navController.currentBackStackEntry) {
        getAuthBackStackEntry(navController)
    }
    return parentEntry?.let { hiltViewModel(it) }
}