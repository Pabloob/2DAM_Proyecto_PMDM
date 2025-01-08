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
    val usuarioActual = usuarioLogeado.usuarioActual
    val cargando by usuarioLogeado.cargando
    val rutinaDelDia by usuarioLogeado.rutinaDelDia
    val lineData by usuarioLogeado.lineData

    LaunchedEffect(usuarioActual) {
        if (usuarioActual != null) {
            usuarioLogeado.inicializarUsuario(usuarioActual.idPersona) {
                // Si ocurre un error, navegar al inicio de sesión
                navController.navigate("VentanaIniciarSesion") {
                    popUpTo(0) { inclusive = true }
                }
            }
        } else {
            navController.navigate("VentanaIniciarSesion") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    if (cargando) {
        // Pantalla de carga mientras se obtienen los datos
        Box(
            modifierBox,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = ColorTitulo)
        }
    } else {
        // Interfaz principal
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
                // Mensaje de bienvenida
                TextoCentrado("Bienvenido ${usuarioActual?.nombreUsuario}", color = ColorTitulo)
                Spacer(modifier = Modifier.height(30.dp))

                // Panel con la rutina del día
                PanelRutinaSimple(rutina = rutinaDelDia)

                Spacer(modifier = Modifier.height(40.dp))

                // Gráfico de progreso (mostrar solo si los datos están listos)
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
