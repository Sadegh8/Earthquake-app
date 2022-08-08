package com.panda.app.earthquakeapp.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.panda.app.earthquakeapp.domain.model.Quake
import com.panda.app.earthquakeapp.utils.Utils

@Composable
fun QuakeItem(modifier: Modifier = Modifier, quake: Quake) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp
    ) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp), verticalArrangement = Arrangement.SpaceBetween) {


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