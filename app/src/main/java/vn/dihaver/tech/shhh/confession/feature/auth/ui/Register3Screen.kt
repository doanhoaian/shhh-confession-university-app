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
        Register3Screen(onBackClick = {}, onPreviousClick = {}, onNextStepClick = {})
    }
}
@Composable
fun Register3Screen(onBackClick: () -> Unit,onPreviousClick: () -> Unit,onNextStepClick: () -> Unit){
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
                            text = "Step 3/5",
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
                        painter = painterResource(R.drawable.svg_processcontainer),
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
                text = "Pick your anonymous identity",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            //Text
            Text(
                text = "Choose an avatar and display name. This is how others will see you.",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            //Space
            Spacer(Modifier.height(ShhhDimens.SpacerExtraSmall))
            //Lazy
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .border(2.dp, color = MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_xuongrong),
                            contentDescription = "Xương rồng một mắt"
                        )
                        Text(
                            text = "Xương rồng một mắt",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_dienvienhai),
                            contentDescription = "Diễn viên hài Chaplin"
                        )
                        Text(
                            text = "Diễn viên hài Chaplin",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_onggianoel),
                            contentDescription = "Ông già Noel"
                        )
                        Text(
                            text = "Ông già Noel",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_nghesimarilyn),
                            contentDescription = "Nghệ sĩ Marilyn"
                        )
                        Text(
                            text = "Nghệ sĩ Marilyn",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_dammayhaykhoc),
                            contentDescription = "Đám mây hay khóc"
                        )
                        Text(
                            text = "Đám mây hay khóc",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_butchi),
                            contentDescription = "Bút chì say nắng"
                        )
                        Text(
                            text = "Bút chì say nắng",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_congnhan),
                            contentDescription = "Công nhân may mắn"
                        )
                        Text(
                            text = "Công nhân may mắn",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_ongchu),
                            contentDescription = "Ông chú Ấn Độ"
                        )
                        Text(
                            text = "Ông chú Ấn Độ",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_caube),
                            contentDescription = "Cậu bé Afro"
                        )
                        Text(
                            text = "",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_quyong),
                            contentDescription = "Quý ông Coffee"
                        )
                        Text(
                            text = "Quý ông Coffee",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_conma),
                            contentDescription = "Con ma hay sad"
                        )
                        Text(
                            text = "Con ma hay sad",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_conluoi),
                            contentDescription = "Con lười bị lười"
                        )
                        Text(
                            text = "Con lười bị lười",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_xuongrong),
                            contentDescription = "Xương rồng một mắt"
                        )
                        Text(
                            text = "Xương rồng một mắt",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_xuongrong),
                            contentDescription = "Xương rồng một mắt"
                        )
                        Text(
                            text = "Xương rồng một mắt",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_xuongrong),
                            contentDescription = "Xương rồng một mắt"
                        )
                        Text(
                            text = "Xương rồng một mắt",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_xuongrong),
                            contentDescription = "Xương rồng một mắt"
                        )
                        Text(
                            text = "Xương rồng một mắt",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_xuongrong),
                            contentDescription = "Xương rồng một mắt"
                        )
                        Text(
                            text = "Xương rồng một mắt",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    //Item
                    Column(
                        modifier = Modifier
                            .height(150.dp)
                            .width(132.dp)
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.svg_xuongrong),
                            contentDescription = "Xương rồng một mắt"
                        )
                        Text(
                            text = "Xương rồng một mắt",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
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
                    .padding(10.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ){
                ShhhButton(
                    label = "Next step",
                    trailingIcon = ImageVector.vectorResource(id = R.drawable.svg_nextstep),
                    modifier = Modifier
                ) {
                    onNextStepClick()
                }
            }
        }
    }
}