package vn.dihaver.tech.shhh.confession.core.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Composable
fun TextUnit.spToDp(): Dp {
    val density = LocalDensity.current
    require(this.type == TextUnitType.Sp) { "Only Sp is supported" }
    return (this.value * density.fontScale).dp
}