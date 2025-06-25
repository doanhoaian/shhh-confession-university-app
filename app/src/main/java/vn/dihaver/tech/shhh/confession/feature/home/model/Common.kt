package vn.dihaver.tech.shhh.confession.feature.home.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarTab(val title: String, val icon: ImageVector, val color: Color) {
    data object Home : BottomBarTab(
        title = "Home",
        icon = Icons.Rounded.Home,
        color = Color(0xFF6366F1)
    )
    data object Discover : BottomBarTab(
        title = "Discover",
        icon = Icons.Rounded.Search,
        color = Color(0xFF6366F1)
    )
    data object Create : BottomBarTab(
        title = "Create",
        icon = Icons.Rounded.AddCircle,
        color = Color(0xFF6366F1)
    )
}