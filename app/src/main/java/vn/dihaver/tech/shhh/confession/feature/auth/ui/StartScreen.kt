package vn.dihaver.tech.shhh.confession.feature.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vn.dihaver.tech.shhh.confession.R
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhButton
import vn.dihaver.tech.shhh.confession.core.ui.component.ShhhOutlinedButton
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme

@Preview(
    showSystemUi = true, showBackground = true,
    device = "id:pixel_9_pro_xl"
)
@Composable
private fun StartScreenPreview() {
    ShhhTheme {
        StartScreen(onRegisterClick = {}, onLoginClick = {})
    }
}

@Composable
fun StartScreen(onRegisterClick: () -> Unit, onLoginClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(WindowInsets.systemBars.asPaddingValues())
    ) {
        Image(
            painter = painterResource(R.drawable.svg_decore_start),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface
                        ),
                        startY = 0f,
                        endY = 500f
                    )
                )
                .padding(40.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "University Confession",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = "Login to start your anonymous journey and connect with the student community",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(ShhhDimens.SpacerExtraLarge))

            ShhhButton(label = "Register") {
                onRegisterClick()
            }

            Row(
                modifier = Modifier.padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f, true),
                    thickness = ShhhDimens.StrokeSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .2f)
                )
                Text(
                    text = "Or",
                    modifier = Modifier.padding(horizontal = ShhhDimens.PaddingExtraSmall),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f, true),
                    thickness = ShhhDimens.StrokeSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .2f)
                )
            }

            ShhhOutlinedButton(label = "Login") {
                onLoginClick()
            }

            Spacer(Modifier.height(ShhhDimens.SpacerLarge))
        }
    }
}