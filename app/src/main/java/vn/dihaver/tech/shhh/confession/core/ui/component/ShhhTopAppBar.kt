package vn.dihaver.tech.shhh.confession.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun ShhhTopAppBarPreview() {
    ShhhTheme {
        ShhhTopAppBar(showNavigation = true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShhhTopAppBar(
    title: @Composable () -> Unit = {},
    showNavigation: Boolean = false,
    navigationIcon: Painter = painterResource(R.drawable.svg_arrow_back),
    actions: (@Composable RowScope.() -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        scrolledContainerColor = MaterialTheme.colorScheme.surface,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor = MaterialTheme.colorScheme.onSurface
    ),
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigationClick: () -> Unit = {}
) {
    TopAppBar(
        title = { title() },
        navigationIcon = {
            if (showNavigation) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        painter = navigationIcon,
                        contentDescription = stringResource(R.string.action_back)
                    )
                }
            }
        },
        actions = {
            actions?.invoke(this)
        },
        modifier = modifier,
        colors = colors,
        scrollBehavior = scrollBehavior
    )
}
