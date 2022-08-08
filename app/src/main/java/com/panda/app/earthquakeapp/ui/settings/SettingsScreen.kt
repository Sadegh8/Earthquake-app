package com.panda.app.earthquakeapp.ui.settings

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.panda.app.earthquakeapp.utils.UiEvent

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onTheme: (UiEvent.ChangeTheme) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Column {

                Spacer(modifier = Modifier.height(6.dp))

                SwitchItem(
                    title = "Dark theme",
                    check = viewModel.darkTheme ?: isSystemInDarkTheme(),
                    onChange = {
                        viewModel.changeDarkTheme(it)
                        onTheme(UiEvent.ChangeTheme(it))
                    }
                )
            }
        }
    }
}


@Composable
fun SwitchItem(
    modifier: Modifier = Modifier,
    title: String,
    check: Boolean,
    onChange: (Boolean) -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(0.dp),
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = title, fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentSize(align = Alignment.CenterStart)
            )


            Spacer(modifier = Modifier.weight(1f))

            Switch(
                checked = check, onCheckedChange = {
                    onChange(it)
                }, modifier = Modifier
                    .wrapContentSize(align = Alignment.CenterEnd)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(onTheme = {UiEvent.ChangeTheme(false)})
}