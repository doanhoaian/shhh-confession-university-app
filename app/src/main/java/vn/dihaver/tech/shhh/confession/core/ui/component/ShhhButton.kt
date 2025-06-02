package vn.dihaver.tech.shhh.confession.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme

@Preview(showSystemUi = false, showBackground = false)
@Composable
private fun ButtonPreview() {
    ShhhTheme {
        ShhhButton(label = "Action", isLoading = false) {

        }
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
private fun OutlineButtonPreview() {
    ShhhTheme {
        ShhhOutlinedButton(label = "Action", isLoading = false) {

        }
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
private fun TextButtonPreview() {
    ShhhTheme {
        ShhhTextButton(label = "Action", isLoading = false, enabled = false) {

        }
    }
}

/**
 * @param label Văn bản nhãn hiển thị bên trong nút.
 * @param modifier Trình sửa đổi để áp dụng các thay đổi bố cục.
 * @param enabled Nút được bật hay tắt.
 * @param leadingIcon Biểu tượng tùy chọn hiển thị trước văn bản.
 * @param trailingIcon Biểu tượng tùy chọn hiển thị sau văn bản.
 * @param isLoading Hiển thị vòng quay tải thay vì nội dung nút.
 * @param onClick Gọi lại được gọi khi nhấp vào nút.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ShhhButton(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: Int? = null,
    isTintLeading: Boolean = true,
    trailingIcon: Int? = null,
    isTintTrailing: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val contentColor = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(alpha = .6f)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val maxButtonWidth = if (screenWidth < 600) Dp.Infinity else 600.dp

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.widthIn(max = maxButtonWidth)
            .fillMaxWidth()
            .height(ShhhDimens.ButtonHeight)
            .clickable(enabled = enabled && !isLoading) { onClick() },
        contentPadding = PaddingValues(horizontal = ShhhDimens.SpacerMedium)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(ShhhDimens.IconSizeSmall),
                strokeWidth = ShhhDimens.StrokeMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (leadingIcon != null) {
                    Image(
                        painter = painterResource(id = leadingIcon),
                        contentDescription = null,
                        modifier = Modifier.size(ShhhDimens.IconSizeSmall),
                        colorFilter = if (isTintLeading) ColorFilter.tint(contentColor) else null
                    )
                } else {
                    Spacer(modifier = Modifier.size(ShhhDimens.IconSizeSmall))
                }

                Spacer(modifier = Modifier.width(ShhhDimens.SpacerSmall))

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    textAlign = TextAlign.Center,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(ShhhDimens.SpacerSmall))

                if (trailingIcon != null) {
                    Image(
                        painter = painterResource(id = trailingIcon),
                        contentDescription = null,
                        modifier = Modifier.size(ShhhDimens.IconSizeSmall),
                        colorFilter = if (isTintTrailing) ColorFilter.tint(contentColor) else null
                    )
                } else {
                    Spacer(modifier = Modifier.size(ShhhDimens.IconSizeSmall))
                }
            }
        }
    }
}

/**
 * @param label Văn bản nhãn hiển thị bên trong nút.
 * @param modifier Trình sửa đổi để áp dụng các thay đổi bố cục.
 * @param enabled Nút được bật hay tắt.
 * @param leadingIcon Biểu tượng tùy chọn hiển thị trước văn bản.
 * @param trailingIcon Biểu tượng tùy chọn hiển thị sau văn bản.
 * @param isLoading Hiển thị vòng quay tải thay vì nội dung nút.
 * @param onClick Gọi lại được gọi khi nhấp vào nút.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ShhhOutlinedButton(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: Int? = null,
    isTintLeading: Boolean = true,
    trailingIcon: Int? = null,
    isTintTrailing: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val contentColor = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(alpha = .6f)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val maxButtonWidth = if (screenWidth < 600) Dp.Infinity else 600.dp

    OutlinedButton(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.widthIn(max = maxButtonWidth)
            .fillMaxWidth()
            .height(ShhhDimens.ButtonHeight)
            .clickable(enabled = enabled && !isLoading) { onClick() },
        contentPadding = PaddingValues(horizontal = ShhhDimens.SpacerMedium),
        border = BorderStroke(ShhhDimens.StrokeSmall, MaterialTheme.colorScheme.primary),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor,
            disabledContentColor = contentColor.copy(alpha = 0.4f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(ShhhDimens.IconSizeSmall),
                strokeWidth = ShhhDimens.StrokeMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (leadingIcon != null) {
                    Image(
                        painter = painterResource(id = leadingIcon),
                        contentDescription = null,
                        modifier = Modifier.size(ShhhDimens.IconSizeSmall),
                        colorFilter = if (isTintLeading) ColorFilter.tint(contentColor) else null
                    )
                } else {
                    Spacer(modifier = Modifier.size(ShhhDimens.IconSizeSmall))
                }

                Spacer(modifier = Modifier.width(ShhhDimens.SpacerSmall))

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentHeight(),
                    textAlign = TextAlign.Center,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(ShhhDimens.SpacerSmall))

                if (trailingIcon != null) {
                    Image(
                        painter = painterResource(id = trailingIcon),
                        contentDescription = null,
                        modifier = Modifier.size(ShhhDimens.IconSizeSmall),
                        colorFilter = if (isTintTrailing) ColorFilter.tint(contentColor) else null
                    )
                } else {
                    Spacer(modifier = Modifier.size(ShhhDimens.IconSizeSmall))
                }
            }

        }
    }
}

/**
 * @param label Văn bản nhãn hiển thị bên trong nút.
 * @param modifier Trình sửa đổi để áp dụng các thay đổi bố cục.
 * @param enabled Nút được bật hay tắt.
 * @param isLoading Hiển thị vòng quay tải thay vì nội dung nút.
 * @param onClick Gọi lại được gọi khi nhấp vào nút.
 */
@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun ShhhTextButton(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val contentColor = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = .6f)

    TextButton(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .height(ShhhDimens.ButtonHeight)
            .clickable(enabled = enabled && !isLoading) { onClick() }
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.textButtonColors(
            contentColor = contentColor
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(ShhhDimens.IconSizeSmall),
                strokeWidth = ShhhDimens.StrokeMedium,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Center,
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
