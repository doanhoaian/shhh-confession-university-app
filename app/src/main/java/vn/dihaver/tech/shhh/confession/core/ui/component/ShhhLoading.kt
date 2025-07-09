package vn.dihaver.tech.shhh.confession.core.ui.component

import androidx.annotation.RawRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme

@Composable
fun PulsePlaceholder(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    highlightColor: Color = Color(0xFF45475F),
    durationMillis: Int = 1000
) {
    val infiniteTransition = rememberInfiniteTransition()

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .background(
                color = highlightColor.copy(alpha = alpha),
                shape = shape
            )
    )
}

@Composable
fun AnimationLoading(
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

    LottieAnimation(
        composition = composition,
        progress = { infiniteProgress },
        modifier = modifier
            .size(40.dp)
            .padding(5.dp)
    )
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PulsePlaceholderPreview() {
    ShhhTheme {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PulsePlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            PulsePlaceholder(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(20.dp)
            )
        }
    }
}
