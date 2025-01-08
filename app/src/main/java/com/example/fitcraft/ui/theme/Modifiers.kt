package com.example.fitcraft.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

val modifierColumna = Modifier
    .fillMaxWidth(0.9f)
    .clip(esquina50)
    .background(ColorFondoSecundario)
    .padding(30.dp)

val modifierBox = Modifier
    .fillMaxSize()
    .background(ColorFondo)
