package vn.dihaver.tech.shhh.confession.feature.auth.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
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
private fun Register3ScreenPreview() {
    ShhhTheme {
        Register5Screen(onBackClick = {}, onPreviousClick = {}, onLetGoClick = {})
    }
}
@Composable
fun Register5Screen(onBackClick: () -> Unit,onPreviousClick: () -> Unit,onLetGoClick: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        //TopAppBar
        Row(
            modifier = Modifier
                .height(130.dp)
                .fillMaxWidth()
                .padding(top = 64.dp),
        ) {
            Column{
                Row(
                    modifier = Modifier.fillMaxSize()
                        .weight(9f)
                ) {
                    //Button
                    Box(
                        modifier = Modifier
                            .weight(2.5f)
                            .padding(10.dp)
                            .fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ){
                        ShhhOutlinedButton(
                            label = "",
                            leadingIcon = ImageVector.vectorResource(id = R.drawable.svg_arrow_back),
                            onClick = {
                                onBackClick()
                            }
                        )
                    }
                    //Text
                    Box(
                        modifier = Modifier
                            .weight(5f)
                            .fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Register",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Text
                    Box(
                        modifier = Modifier
                            .weight(2.5f)
                            .fillMaxSize()
                            .padding(end = 10.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "Step 5/5",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.svg_process5),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
                    )
                }

            }
        }
        //Content
        Column(
            modifier = Modifier
                .height(639.dp)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            //Text
            Text(
                text = "Welcome, Xương rồng một mắt \uD83D\uDC4B",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            //Text
            Text(
                text = "You're now ready to explore and share your first confession.",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            //Space
            Spacer(Modifier.height(ShhhDimens.SpacerExtraSmall))
            //Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = RoundedCornerShape(16.dp))
                    .border(
                        1.dp,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                //Header
                Column(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Anonymous Student ID",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                    Divider(modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .2f))
                }
                //Body
                Row(
                    modifier = Modifier.fillMaxSize().padding(10.dp),

                ) {
                    //Avatar
                    Image(
                        painter = painterResource(R.drawable.svg_xuongrong),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(10.dp)
                    )
                    //Info
                    Column(
                        modifier = Modifier
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "Name: Xương rồng một mắt",
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondary,
                        )
                        Text(
                                text = "Username: @hellocacban123",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondary,
                        )
                        Text(
                        text = "Email: example@gmail.com",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondary,
                        )
                        Text(
                        text = "School: ĐH Giao Thông Vận Tải Tp. HCM",
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondary,
                        )

                    }
                }
            }
        }
        //BottomAppBar
        Row(
            modifier = Modifier
                .height(83.dp)
                .fillMaxWidth()
        ) {

            //Button
            Box(
                modifier = Modifier
                    .weight(2.5f)
                    .padding(10.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ){
                ShhhOutlinedButton(
                    label = "Previous",
                    onClick = {
                        onPreviousClick()
                    }
                )
            }
            //space
            Box()
            {
                Spacer(modifier = Modifier.width(110.dp))
            }
            //Button
            Box(
                modifier = Modifier
                    .weight(2.5f)
                    .fillMaxSize()
                    .padding(10.dp),
                contentAlignment = Alignment.CenterStart
            ){
                ShhhButton(
                    label = "Let's go",
                    trailingIcon = ImageVector.vectorResource(id = R.drawable.svg_nextstep),
                    modifier = Modifier
                ) {
                    onLetGoClick()
                }
            }
        }
    }
}
