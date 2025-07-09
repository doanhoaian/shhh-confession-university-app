package vn.dihaver.tech.shhh.confession.core.ui.component

import androidx.annotation.RawRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.R
import kotlin.math.roundToInt

/**
 * A Composable function that implements a custom pull-to-refresh behavior with
 * card-like animations for the content.
 *
 * This function wraps the provided `content` in a pull-to-refresh container.
 * When the user pulls down, the content animates with an offset and rotation,
 * and a custom refresh indicator is displayed.
 *
 * It uses haptic feedback to enhance the user experience during the pull gesture.
 *
 * @param isRefreshing A boolean state indicating whether the refresh operation is currently in progress.
 *                     This is typically controlled by a ViewModel or other state holder.
 * @param onRefresh A lambda function that will be invoked when the user triggers a refresh.
 *                  This is where you should initiate your data fetching or refresh logic.
 * @param modifier An optional [Modifier] to be applied to the root Box composable.
 * @param content A composable lambda that receives the `cardOffset` (Int) and `cardRotation` (Float)
 *                as parameters. This allows the child composables within the content slot
 *                to react to the pull gesture and apply animations.
 *                - `cardOffset`: The vertical offset (in pixels) to apply to the content cards
 *                                based on the pull distance.
 *                - `cardRotation`: The rotation angle (in degrees) to apply to the content cards
 *                                  based on the pull distance.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShhhPullToRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    indicatorPaddingTop: Dp = 0.dp,
    content: @Composable (
        cardOffset: () -> Int,
        cardRotation: () -> Float
    ) -> Unit
) {
    val hapticFeedback = LocalHapticFeedback.current

    val scope = rememberCoroutineScope()
    val pullRefreshState = rememberPullToRefreshState()
    var isRefreshingLocal by remember { mutableStateOf(false) }
    val willRefresh by remember {
        derivedStateOf { pullRefreshState.distanceFraction > 1f }
    }
    val currentOnRefresh by rememberUpdatedState(onRefresh)

    val isActuallyRefreshing = isRefreshingLocal || isRefreshing

    val cardOffset by animateIntAsState(
        targetValue = when {
            isActuallyRefreshing -> 250
            pullRefreshState.distanceFraction in 0f..1f -> (250 * pullRefreshState.distanceFraction).roundToInt()
            pullRefreshState.distanceFraction > 1f -> (250 + ((pullRefreshState.distanceFraction - 1f) * .1f) * 100).roundToInt()
            else -> 0
        },
        label = "cardOffset"
    )
    val cardRotation by animateFloatAsState(
        targetValue = when {
            isActuallyRefreshing || pullRefreshState.distanceFraction > 1f -> 5f
            pullRefreshState.distanceFraction > 0f -> 5 * pullRefreshState.distanceFraction
            else -> 0f
        },
        label = "cardRotation"
    )

    LaunchedEffect(willRefresh) {
        if (willRefresh) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            delay(70)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            delay(100)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            isRefreshingLocal = false
        }
    }

    Box(
        modifier = modifier
            .pullToRefresh(
                isRefreshing = isActuallyRefreshing,
                state = pullRefreshState,
                onRefresh = {
                    scope.launch {
                        isRefreshingLocal = true
                        currentOnRefresh()
                    }
                }
            )
    ) {
        AnimationPullRefreshIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = indicatorPaddingTop),
            isRefreshing = isActuallyRefreshing,
            pullFraction = pullRefreshState.distanceFraction
        )

        content(
            { cardOffset },
            { cardRotation }
        )

        if (isActuallyRefreshing) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent
            ) {}
        }
    }
}

fun Modifier.refreshEffect(
    index: Int,
    total: Int,
    rotationPerItem: Float = 5f,
    cardRotation: () -> Float,
    cardOffset: () -> Int,
) = this
    .zIndex((total - index).toFloat())
    .graphicsLayer {
        rotationZ = cardRotation() * if (index % 2 == 0) 1 else -1
        translationY = (cardOffset() * ((rotationPerItem - (index + 1)) / 5f))
    }

@Composable
private fun AnimationPullRefreshIndicator(
    isRefreshing: Boolean,
    pullFraction: Float,
    modifier: Modifier = Modifier,
    @RawRes lottieResId: Int = R.raw.animation_loading
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieResId))

    val infiniteProgress by rememberInfiniteTransition(label = "LottieInfinite").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "progress"
    )

    val effectiveProgress = if (isRefreshing) infiniteProgress else pullFraction.coerceIn(0f, 1f)

    val targetOffset by animateIntAsState(
        targetValue = when {
            isRefreshing -> 120
            pullFraction in 0f..1f -> (120 * pullFraction).roundToInt()
            pullFraction > 1f -> (120 + ((pullFraction - 1f) * .1f) * 100).roundToInt()
            else -> 0
        }, label = "targetOffset"
    )

    LottieAnimation(
        composition = composition,
        progress = { effectiveProgress },
        modifier = modifier
            .size(40.dp)
            .padding(5.dp)
            .offset(y = (-30).dp)
            .graphicsLayer {
                translationY = targetOffset.toFloat()
            }
    )
}