package com.example.fitcraft.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
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
    modifier: Modifier,
    numerico: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(placeholder, color = ColorTexto)
        },
        leadingIcon = leadingIcon,
        shape = esquina25,
        modifier = modifier,
        keyboardOptions = if (numerico) {
            KeyboardOptions(keyboardType = KeyboardType.Number)
        } else {
            KeyboardOptions.Default
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedTextColor = ColorTexto,
            focusedTextColor = ColorTexto,
            unfocusedBorderColor = Color.White,
            focusedBorderColor = Color.White,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White
        )
    )
}

@Composable
fun CampoTextoEditable(
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    placeholder: String,
    modifier: Modifier,
    numerico: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        placeholder = {
            Text(placeholder, color = ColorTexto)
        },
        modifier = modifier,
        textStyle = TextStyle(
            color = ColorTexto,
            fontSize = 18.sp
        ),
        singleLine = true,
        keyboardOptions = if (numerico) {
            KeyboardOptions(keyboardType = KeyboardType.Number)
        } else {
            KeyboardOptions.Default
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        )
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