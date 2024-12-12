package com.example.fitcraft.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.data.firebase.ConexionRutina
import com.example.fitcraft.data.model.Rutina
import com.example.fitcraft.data.model.Usuario
import com.example.fitcraft.ui.NavegadorVentanas
import com.example.fitcraft.ui.components.LineChartComponent
import com.example.fitcraft.ui.components.PanelNavegacionInferior
import com.example.fitcraft.ui.components.PanelRutina
import com.example.fitcraft.ui.components.TextoCentrado
import com.example.fitcraft.ui.components.createLineData
import com.example.fitcraft.ui.theme.ColorFondo
import com.example.fitcraft.ui.theme.ColorTitulo
import com.example.fitcraft.ui.theme.FitCraftTheme
import com.example.fitcraft.viewmodel.DatosRutina
import com.example.fitcraft.viewmodel.UsuarioLogeado
import com.github.mikephil.charting.data.LineData


class VentanaInicio : ComponentActivity() {
    private val usuarioLogeado: UsuarioLogeado by viewModels()
    private val datosRutina: DatosRutina by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitCraftTheme {
                val navController = rememberNavController()
                NavegadorVentanas(
                    navController,
                    usuarioLogeado,
                    datosRutina,
                )
            }
        }
    }
}

@Composable
fun Inicio(navController: NavController, usuarioLogeado: UsuarioLogeado) {
    val conexionRutinas = ConexionRutina()
    val rutinaDelDia = remember { mutableStateOf<Rutina?>(null) }
    val rutinasUsuario = remember { mutableStateOf<List<Rutina>>(emptyList()) }
    val lineData = remember { mutableStateOf<LineData?>(null) }
    var cargando by remember { mutableStateOf(true) }
    var usuarioActual: Usuario? by remember { mutableStateOf(null) }

    // Cargar datos relacionados con el usuario
    LaunchedEffect(usuarioLogeado.usuarioActual) {
        val usuario = usuarioLogeado.usuarioActual
        if (usuario != null) {
            usuarioActual = usuario
            cargando = true

            conexionRutinas.cargarRutinasPorDia(usuario.idPersona) { rutinas ->
                rutinaDelDia.value = rutinas.firstOrNull()
                cargando = false
            }

            conexionRutinas.cargarTodasLasRutinas(usuario.idPersona) { rutinas ->
                rutinasUsuario.value = rutinas
                lineData.value = createLineData(rutinas)
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
            modifier = Modifier
                .fillMaxSize()
                .background(ColorFondo),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = ColorTitulo)
        }
    } else {
        // Interfaz principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorFondo)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth(0.9f)
                    .align(Alignment.TopCenter)
            ) {
                // Mensaje de bienvenida
                TextoCentrado("Bienvenido ${usuarioActual?.nombreUsuario}", color = ColorTitulo)
                Spacer(modifier = Modifier.height(30.dp))

                // Panel con la rutina del día
                PanelRutina(rutina = rutinaDelDia.value)

                Spacer(modifier = Modifier.height(40.dp))

                // Gráfico de progreso (mostrar solo si los datos están listos)
                lineData.value?.let { LineChartComponent(it) }
            }

            // Panel de navegación inferior
            PanelNavegacionInferior(
                navController = navController,
                modifier = Modifier.align(Alignment.BottomEnd)
            )
        }
    }
}