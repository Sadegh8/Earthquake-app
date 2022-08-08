package com.panda.app.earthquakeapp.ui.detail.components

import android.location.Location
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.panda.app.earthquakeapp.domain.model.Quake
import com.panda.app.earthquakeapp.utils.Utils

@Composable
fun MapCardInfo(
    modifier: Modifier = Modifier,
    currentLocation: Location?,
    quakeM: Quake
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
    ) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format("%.2f", quakeM.magnitude), textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 10.dp),

                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = quakeM.title,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    maxLines = 2

                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Depth")

                    Text(
                        text = String.format("%.2f", quakeM.depth),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Time")

                    Text(text = Utils.formatDate(quakeM.time), fontWeight = FontWeight.SemiBold)
                }

                currentLocation?.let { locationCoordinate ->
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Distance")

                        Text(
                            text =
                            (Utils.getDistanceBetween(
                                destinationCoordinate = quakeM.coordinate,
                                currentLocation = LatLng(
                                    locationCoordinate.latitude,
                                    locationCoordinate.longitude
                                )
                            ) / 1000).toString(), fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
