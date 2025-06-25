package vn.dihaver.tech.shhh.confession.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme

@Composable
fun CustomTabRow(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // State để lưu kích thước và vị trí của các tab
    val tabWidths = remember { mutableStateListOf<Dp>() }
    val tabPositions = remember { mutableStateListOf<Dp>() }
    val density = LocalDensity.current
    val scrollState = rememberScrollState()

    // Tính toán vị trí và chiều rộng của indicator
    val indicatorWidth by animateDpAsState(
        targetValue = if (tabWidths.size > selectedTabIndex) tabWidths[selectedTabIndex] * 0.8f else 0.dp,
        animationSpec = tween(durationMillis = 300)
    )
    val indicatorPosition by animateDpAsState(
        targetValue = if (tabPositions.size > selectedTabIndex && tabWidths.size > selectedTabIndex) {
            // Căn giữa indicator: vị trí tab + (chiều rộng tab - chiều rộng indicator) / 2
            tabPositions[selectedTabIndex] + (tabWidths[selectedTabIndex] - indicatorWidth) / 2
        } else 0.dp,
        animationSpec = tween(durationMillis = 300)
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier
                        .clickable { onTabSelected(index) }
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .onGloballyPositioned { coordinates ->
                            with(density) {
                                tabWidths.addOrUpdate(index, coordinates.size.width.toDp())
                                // Căn giữa vị trí của tab
                                tabPositions.addOrUpdate(index, coordinates.positionInParent().x.toDp())
                            }
                        }
                )
            }
        }
        // Indicator
        Box(
            modifier = Modifier
                .offset(x = indicatorPosition)
                .width(indicatorWidth)
                .height(4.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
    }

    // Tự động cuộn đến tab được chọn
    LaunchedEffect(selectedTabIndex) {
        if (tabPositions.size > selectedTabIndex) {
            scrollState.animateScrollTo(tabPositions[selectedTabIndex].value.toInt())
        }
    }
}

// Hàm tính chiều rộng của indicator (có thể tùy chỉnh)
private fun indicatorWidth(selectedTabIndex: Int, tabWidths: List<Dp>): Dp {
    return if (tabWidths.size > selectedTabIndex) tabWidths[selectedTabIndex] * 0.8f else 0.dp
}

// Hàm tiện ích để cập nhật danh sách
private fun <T> MutableList<T>.addOrUpdate(index: Int, element: T) {
    while (size <= index) add(element)
    set(index, element)
}


@Preview(showBackground = true)
@Composable
fun CustomTabRowExample() {
    // Danh sách các tab
    val tabs = listOf("Tab 1", "Tab 2", "Tab 3")
    // State để theo dõi tab được chọn
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    ShhhTheme {
        Column {
            CustomTabRow(
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index -> selectedTabIndex = index },
                modifier = Modifier.fillMaxWidth()
            )
            // Nội dung hiển thị khi chọn tab
            Text(
                text = "Content of ${tabs[selectedTabIndex]}",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}