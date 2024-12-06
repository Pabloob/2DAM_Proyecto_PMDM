package com.example.fitcraft.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Esquema de colores para tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = ColorBoton,
    secondary = ColorFondoSecundario,
    background = ColorFondo,
    surface = ColorFondoSecundario,
    onPrimary = ColorTextoBoton,
    onSecondary = ColorTexto,
    onBackground = ColorTexto,
    onSurface = ColorTexto
)

// Esquema de colores para tema claro
private val LightColorScheme = lightColorScheme(
    primary = ColorBoton,
    secondary = ColorFondoSecundario,
    background = Color.White,
    surface = Color(0xFFF5F5F5), // Fondo claro secundario
    onPrimary = Color.Black,
    onSecondary = Color.DarkGray,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun FitCraftTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detecta si el sistema está en modo oscuro
    dynamicColor: Boolean = true, // Usa colores dinámicos en Android 12+
    content: @Composable () -> Unit
) {
    // Determina el esquema de colores según la configuración
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        // typography = Typography, // Puedes añadir tipografía personalizada si la tienes
        // shapes = Shapes,         // Puedes añadir formas personalizadas si las tienes
        content = content
    )
}
