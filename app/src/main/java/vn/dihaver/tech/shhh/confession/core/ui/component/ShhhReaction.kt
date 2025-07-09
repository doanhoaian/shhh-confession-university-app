package vn.dihaver.tech.shhh.confession.core.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import vn.dihaver.tech.shhh.confession.core.ui.util.OdometerText
import vn.dihaver.tech.shhh.confession.core.util.FormatUtils.formatCountCompact
import kotlin.random.Random

enum class ReactionState {
    LIKED, DISLIKED, NONE
}

private data class Particle(
    val scale: Float,
    val alpha: Float,
    val direction: Offset,
    val initialColor: Color
)

@Composable
fun ReactionButtons(
    modifier: Modifier = Modifier,
    initialLikeCount: Long,
    initialDislikeCount: Long,
    isLikedInitially: Boolean,
    isDislikedInitially: Boolean,
    likeIconRes: Int,
    likeFilledIconRes: Int,
    dislikeIconRes: Int,
    dislikeFilledIconRes: Int
) {
    val context = LocalContext.current

    var currentReaction by remember {
        mutableStateOf(
            when {
                isLikedInitially -> ReactionState.LIKED
                isDislikedInitially -> ReactionState.DISLIKED
                else -> ReactionState.NONE
            }
        )
    }

    var likeCount by remember { mutableLongStateOf(initialLikeCount) }
    var dislikeCount by remember { mutableLongStateOf(initialDislikeCount) }

    val particleAnimationState = remember { mutableStateOf(false) }
    val particles = remember { mutableStateListOf<Particle>() }
    val coroutineScope = rememberCoroutineScope()


    // ---- ANIMATION STATES ----
    // Animation cho nút LIKE
    val likeScale by animateFloatAsState(
        targetValue = if (currentReaction == ReactionState.LIKED) 1.2f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "LikeScale"
    )
    val likeColor by animateColorAsState(
        targetValue = if (currentReaction == ReactionState.LIKED) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(durationMillis = 200),
        label = "LikeColor"
    )

    // Animation cho nút DISLIKE
    val dislikeScale by animateFloatAsState(
        targetValue = if (currentReaction == ReactionState.DISLIKED) 1.2f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "DislikeScale"
    )
    val dislikeColor by animateColorAsState(
        targetValue = if (currentReaction == ReactionState.DISLIKED) MaterialTheme.colorScheme.error.copy(alpha = .8f) else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(durationMillis = 200),
        label = "DislikeColor"
    )

    // Animation cho nền
    val bgColor by animateColorAsState(
        targetValue = when (currentReaction) {
            ReactionState.LIKED -> MaterialTheme.colorScheme.primary.copy(alpha = .1f)
            ReactionState.DISLIKED -> MaterialTheme.colorScheme.error.copy(alpha = .05f)
            ReactionState.NONE -> Color.Transparent
        },
        label = "ReactionBgColor"
    )

    // ---- CLICK HANDLERS ----
    fun onLikeClick() {
        val wasLiked = currentReaction == ReactionState.LIKED
        val wasDisliked = currentReaction == ReactionState.DISLIKED

        if (wasLiked) {
            // Đã like -> Bỏ like
            currentReaction = ReactionState.NONE
            likeCount--
        } else {
            // Chưa like -> Thực hiện like
            if (wasDisliked) {
                dislikeCount-- // Nếu trước đó đã dislike thì giảm dislike count
            }
            likeCount++
            currentReaction = ReactionState.LIKED
            // Kích hoạt hiệu ứng hạt
            particleAnimationState.value = true
            coroutineScope.launch {
                particles.clear()
                repeat(30) {
                    particles.add(
                        Particle(
                            scale = Random.nextFloat() * 0.8f + 0.5f,
                            alpha = 1f,
                            direction = Offset(
                                Random.nextFloat() * 2 - 1,
                                Random.nextFloat() * 2 - 1
                            ).let { it / (it.getDistance() + 0.0001f) },
                            initialColor = listOf(
                                Color(0xFFFFC107),
                                Color(0xFFFF5722),
                                Color(0xFF6366F1)
                            ).random()
                        )
                    )
                }
            }
        }
    }

    fun onDislikeClick() {
        val wasLiked = currentReaction == ReactionState.LIKED
        val wasDisliked = currentReaction == ReactionState.DISLIKED

        if (wasDisliked) {
            // Đã dislike -> Bỏ dislike
            currentReaction = ReactionState.NONE
            dislikeCount--
        } else {
            // Chưa dislike -> Thực hiện dislike
            if (wasLiked) {
                likeCount-- // Nếu trước đó đã like thì giảm like count
            }
            dislikeCount++
            currentReaction = ReactionState.DISLIKED
        }
    }

    // ---- UI COMPOSITION ----
    Box(contentAlignment = Alignment.CenterStart) {
        // Lớp vẽ hạt (chỉ hiển thị khi like)
        if (particleAnimationState.value) {
            particles.forEach { particle ->
                val progress = remember { Animatable(0f) }
                LaunchedEffect(particleAnimationState.value) {
                    progress.animateTo(
                        1f,
                        animationSpec = tween(
                            800 + (Math.random() * 300).toInt(),
                            easing = LinearOutSlowInEasing
                        )
                    )
                    if (progress.value == 1f) particleAnimationState.value = false
                }
                Box(
                    modifier = Modifier
                        .padding(start = 18.dp) // Căn giữa hạt vào icon like
                        .graphicsLayer {
                            val currentOffset = particle.direction * progress.value * 60.dp.toPx()
                            translationX = currentOffset.x
                            translationY = currentOffset.y
                            scaleX = particle.scale * (1 - progress.value); scaleY =
                            particle.scale * (1 - progress.value)
                            alpha = (1 - progress.value).coerceAtLeast(0f)
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(particle.initialColor, shape = CircleShape)
                    )
                }
            }
        }

        // Giao diện chính của các nút
        Row(
            modifier = modifier
                .background(bgColor, shape = MaterialTheme.shapes.large)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                    MaterialTheme.shapes.large
                )
                .clip(MaterialTheme.shapes.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Nút LIKE
            Row(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = ::onLikeClick
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(if (currentReaction == ReactionState.LIKED) likeFilledIconRes else likeIconRes),
                    contentDescription = "Like",
                    modifier = Modifier
                        .size(16.5.dp)
                        .scale(likeScale),
                    tint = likeColor
                )
                Spacer(Modifier.width(5.dp))

                // Animated Count
                if (likeCount < 999) {
                    OdometerText(
                        count = likeCount,
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = if (currentReaction == ReactionState.LIKED) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.W700
                        )
                    )
                } else {
                    Text(
                        text = likeCount.formatCountCompact(context),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.W700
                        )
                    )
                }
            }

            VerticalDivider(
                modifier = Modifier.height(16.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )

            // Nút DISLIKE
            Box(
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = ::onDislikeClick
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(if (currentReaction == ReactionState.DISLIKED) dislikeFilledIconRes else dislikeIconRes),
                    contentDescription = "Dislike",
                    modifier = Modifier
                        .size(16.dp)
                        .scale(dislikeScale),
                    tint = dislikeColor
                )
            }
        }
    }
}


@Composable
fun SimpleReactionButtons(
    initialLikeCount: Long,
    initialDislikeCount: Long,
    isLikedInitially: Boolean,
    isDislikedInitially: Boolean,
    likeIconRes: Int,
    likeFilledIconRes: Int,
    dislikeIconRes: Int,
    dislikeFilledIconRes: Int
) {
    val context = LocalContext.current

    var currentReaction by remember {
        mutableStateOf(
            when {
                isLikedInitially -> ReactionState.LIKED
                isDislikedInitially -> ReactionState.DISLIKED
                else -> ReactionState.NONE
            }
        )
    }

    var likeCount by remember { mutableLongStateOf(initialLikeCount) }
    var dislikeCount by remember { mutableLongStateOf(initialDislikeCount) }


    // ---- ANIMATION STATES ----
    // Animation cho nút LIKE
    val likeScale by animateFloatAsState(
        targetValue = if (currentReaction == ReactionState.LIKED) 1.2f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "LikeScale"
    )
    val likeColor by animateColorAsState(
        targetValue = if (currentReaction == ReactionState.LIKED) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .8f),
        animationSpec = tween(durationMillis = 200),
        label = "LikeColor"
    )

    // Animation cho nút DISLIKE
    val dislikeScale by animateFloatAsState(
        targetValue = if (currentReaction == ReactionState.DISLIKED) 1.2f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "DislikeScale"
    )
    val dislikeColor by animateColorAsState(
        targetValue = if (currentReaction == ReactionState.DISLIKED) MaterialTheme.colorScheme.error.copy(alpha = .8f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .8f),
        animationSpec = tween(durationMillis = 200),
        label = "DislikeColor"
    )

    // ---- CLICK HANDLERS ----
    fun onLikeClick() {
        val wasLiked = currentReaction == ReactionState.LIKED
        val wasDisliked = currentReaction == ReactionState.DISLIKED

        if (wasLiked) {
            // Đã like -> Bỏ like
            currentReaction = ReactionState.NONE
            likeCount--
        } else {
            // Chưa like -> Thực hiện like
            if (wasDisliked) {
                dislikeCount-- // Nếu trước đó đã dislike thì giảm dislike count
            }
            likeCount++
            currentReaction = ReactionState.LIKED
        }
    }

    fun onDislikeClick() {
        val wasLiked = currentReaction == ReactionState.LIKED
        val wasDisliked = currentReaction == ReactionState.DISLIKED

        if (wasDisliked) {
            // Đã dislike -> Bỏ dislike
            currentReaction = ReactionState.NONE
            dislikeCount--
        } else {
            // Chưa dislike -> Thực hiện dislike
            if (wasLiked) {
                likeCount-- // Nếu trước đó đã like thì giảm like count
            }
            dislikeCount++
            currentReaction = ReactionState.DISLIKED
        }
    }

    // ---- UI COMPOSITION ----
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Nút LIKE
        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = ::onLikeClick
                )
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(if (currentReaction == ReactionState.LIKED) likeFilledIconRes else likeIconRes),
                contentDescription = "Like",
                modifier = Modifier
                    .size(15.dp)
                    .scale(likeScale),
                tint = likeColor
            )
            Spacer(Modifier.width(4.dp))

            Text(
                text = likeCount.formatCountCompact(context),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .8f),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.W500
                )
            )
        }

        // Nút DISLIKE
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = ::onDislikeClick
                )
                .padding(horizontal = 6.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(if (currentReaction == ReactionState.DISLIKED) dislikeFilledIconRes else dislikeIconRes),
                contentDescription = "Dislike",
                modifier = Modifier
                    .size(14.5.dp)
                    .scale(dislikeScale),
                tint = dislikeColor
            )
        }
    }
}