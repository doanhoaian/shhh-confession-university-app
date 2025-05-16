package vn.dihaver.tech.shhh.confession.core.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhDimens
import vn.dihaver.tech.shhh.confession.core.ui.theme.ShhhTheme

@Preview(showSystemUi = false, showBackground = false)
@Composable
private fun ButtonPreview() {
    ShhhTheme {
        ShhhButton(label = "Action", leadingIcon = Icons.Default.Call, isLoading = false) {

        }
    }
}

@Preview(showSystemUi = false, showBackground = false)
@Composable
private fun OutlineButtonPreview() {
    ShhhTheme {
        ShhhOutlinedButton(label = "Action", leadingIcon = Icons.Default.Call, isLoading = false) {

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
@Composable
fun ShhhButton(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
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
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(ShhhDimens.IconSizeSmall)
                )
                Spacer(modifier = Modifier.width(ShhhDimens.SpacerSmall))
            }

            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge
            )

            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(ShhhDimens.SpacerSmall))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(ShhhDimens.IconSizeSmall)
                )
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
@Composable
fun ShhhOutlinedButton(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    val contentColor = MaterialTheme.colorScheme.onPrimary

    OutlinedButton(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
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
                color = contentColor
            )
        } else {
            if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(ShhhDimens.IconSizeSmall)
                )
                Spacer(modifier = Modifier.width(ShhhDimens.SpacerSmall))
            }

            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor
            )

            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(ShhhDimens.SpacerSmall))
                Icon(
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(ShhhDimens.IconSizeSmall)
                )
            }
        }
    }
}
