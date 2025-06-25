package vn.dihaver.tech.shhh.confession.feature.post.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import vn.dihaver.tech.shhh.confession.feature.post.ui.CreatePostScreen
import vn.dihaver.tech.shhh.confession.feature.post.viewmodel.CreatePostViewModel
import vn.dihaver.tech.shhh.confession.navigation.NavRoutes

fun NavGraphBuilder.postNavGraph(navController: NavHostController) {
    composable(NavRoutes.Post.CREATE) {
        val viewModel: CreatePostViewModel = hiltViewModel()

        CreatePostScreen(
            viewModel = viewModel,
            onBack = {
                navController.popBackStack()
            }
        )
    }
}