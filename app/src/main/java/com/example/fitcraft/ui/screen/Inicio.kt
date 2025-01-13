package com.example.fitcraft.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitcraft.ui.components.LineChartComponent
import com.example.fitcraft.ui.components.PanelNavegacionInferior
import com.example.fitcraft.ui.components.PanelRutinaSimple
import com.example.fitcraft.ui.components.TextoCentrado
import com.example.fitcraft.ui.theme.ColorTitulo
import com.example.fitcraft.ui.theme.modifierBox
import com.example.fitcraft.viewmodel.UsuarioLogeado

@Composable
fun Inicio(navController: NavController, usuarioLogeado: UsuarioLogeado) {
    // Datos del usuario actual
    val usuarioActual = usuarioLogeado.usuarioActual
    // Estado que indica si se está cargando datos
    val cargando by usuarioLogeado.cargando
    // Rutina del día asociada al usuario
    val rutinaDelDia by usuarioLogeado.rutinaDelDia
    // Datos para el gráfico de progreso
    val lineData by usuarioLogeado.lineData

    // Efecto para inicializar los datos del usuario
    LaunchedEffect(usuarioActual) {
        if (usuarioActual != null) {
            usuarioLogeado.inicializarUsuario(usuarioActual.idPersona) {
                // Si ocurre un error, navegar al inicio de sesión
                navController.navigate("VentanaIniciarSesion") {
                    popUpTo(0) { inclusive = true }
                }
            }
        } else {
            // Si no hay usuario actual, navegar al inicio de sesión
            navController.navigate("VentanaIniciarSesion") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (cargando) {
        // Mostrar pantalla de carga mientras se obtienen los datos
        Box(
            modifierBox,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = ColorTitulo)
        }
    } else {
        // Mostrar la interfaz principal
        Box(
            modifierBox
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth(0.9f)
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState())
            ) {
                // Saludo con el nombre del usuario
                TextoCentrado("Bienvenido ${usuarioActual?.nombreUsuario}", color = ColorTitulo)
                Spacer(modifier = Modifier.height(30.dp))

                // Mostrar panel con la rutina del día
                PanelRutinaSimple(rutina = rutinaDelDia)

                Spacer(modifier = Modifier.height(40.dp))

                // Mostrar gráfico de progreso solo si hay datos disponibles
                lineData?.let { LineChartComponent(it) }
            }

            // Panel de navegación inferior
            PanelNavegacionInferior(
                navController = navController,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}
