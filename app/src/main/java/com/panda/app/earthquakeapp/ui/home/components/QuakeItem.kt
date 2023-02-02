package com.panda.app.earthquakeapp.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.panda.app.earthquakeapp.domain.model.Quake
import com.panda.app.earthquakeapp.utils.Utils

@Composable
fun QuakeItem(modifier: Modifier = Modifier, quake: Quake) {
    Card(
        modifier = modifier,

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = String.format("%.2f", quake.magnitude),
                    modifier = Modifier.padding(4.dp),
                    textAlign = TextAlign.Start,
                    color = Color.Red.copy(0.8f),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 20.sp
                )

                Text(
                    text = quake.title,
                    modifier = Modifier.padding(4.dp),
                    textAlign = TextAlign.End,
                    fontSize = 17.sp
                )
            }

            Text(
                text = Utils.formatDate(quake.time),
                modifier = Modifier.padding(4.dp),
                textAlign = TextAlign.Start,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuakeItemPreview() {
    QuakeItem(
        quake = Quake(
            "0", title = "Los angeles", 1673539779, "https://en.wikipedia.org/wiki/Los_Angeles",
            5.4, LatLng(34.0522, 118.2437), 13.4
        )
    )
}