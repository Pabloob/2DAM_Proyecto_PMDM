package com.example.fitcraft.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.fitcraft.ui.theme.ColorTexto
import com.example.fitcraft.ui.theme.TitleTextStyle

@Composable
fun TextoCentrado(
    text: String,
    style: TextStyle = TitleTextStyle,
    modifier: Modifier = Modifier,
    color: Color
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
    ) {
        Text(text = text, style = style, modifier = modifier, color = color)
    }
}

@Composable
fun TextoInteractivo(texto: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Text(
        text = texto,
        color = ColorTexto,
        modifier = modifier
            .clickable { onClick() }
            .padding(top = 16.dp)
    )
}
