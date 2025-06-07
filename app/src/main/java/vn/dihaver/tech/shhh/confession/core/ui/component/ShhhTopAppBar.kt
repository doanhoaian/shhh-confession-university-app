package vn.dihaver.tech.shhh.confession.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        ShhhTopAppBar(showBack = true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShhhTopAppBar(
    title: String? = null,
    showBack: Boolean = false,
    actions: (@Composable RowScope.() -> Unit)? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.surface,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor = MaterialTheme.colorScheme.onSurface
    ),
    onBackClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.svg_arrow_back),
                        contentDescription = stringResource(R.string.action_back)
                    )
                }
            }
        },
        actions = {
            actions?.invoke(this)
        },
        modifier = modifier,
        colors = colors
    )
}
