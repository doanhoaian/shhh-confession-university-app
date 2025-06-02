package vn.dihaver.tech.shhh.confession.core.ui.util

import androidx.compose.runtime.Composable
import vn.dihaver.tech.shhh.confession.core.ui.theme.ExtraColors
import vn.dihaver.tech.shhh.confession.core.ui.theme.LocalExtraColors

object ShhhThemeExtended {
    val extraColors: ExtraColors
        @Composable
        get() = LocalExtraColors.current
}