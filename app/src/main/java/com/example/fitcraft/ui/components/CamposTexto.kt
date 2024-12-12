package com.example.fitcraft.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import com.example.fitcraft.ui.theme.ColorTexto
import com.example.fitcraft.ui.theme.esquina25

@Composable
fun CampoTexto(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier
) {
    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedTextColor = ColorTexto,
            focusedTextColor = ColorTexto,
            unfocusedBorderColor = Color.White,
            focusedBorderColor = Color.White,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White
        ),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(placeholder, color = ColorTexto)
        },
        leadingIcon = leadingIcon,
        shape = esquina25,
        modifier = modifier,
    )
}

@Composable
fun CampoTextoConContrasena(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedTextColor = ColorTexto,
            focusedTextColor = ColorTexto,
            unfocusedBorderColor = Color.White,
            focusedBorderColor = Color.White,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White
        ),
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(placeholder, color = ColorTexto)
        },
        leadingIcon = leadingIcon,
        visualTransformation = PasswordVisualTransformation(),
        shape = esquina25,
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun CampoTextoEditable(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    placeholder: String,
    modifier: Modifier
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        placeholder = { Text(placeholder, color = ColorTexto) },
        singleLine = true,
        textStyle = TextStyle(color = ColorTexto, fontSize = 18.sp),
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledTextColor = ColorTexto,
            focusedIndicatorColor = ColorTexto,
            unfocusedIndicatorColor = ColorTexto,
            disabledIndicatorColor = Color.Transparent
        )
    )
}