package com.example.fitcraft.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.data.firebase.ConexionEjercicio
import com.example.fitcraft.data.model.Ejercicio
import com.example.fitcraft.ui.NavegadorVentanas
import com.example.fitcraft.ui.components.Boton
import com.example.fitcraft.ui.components.DividerConEspaciado
import com.example.fitcraft.ui.components.PanelRutina
import com.example.fitcraft.ui.components.TextoCentrado
import com.example.fitcraft.ui.theme.ColorFondo
import com.example.fitcraft.ui.theme.ColorTexto
import com.example.fitcraft.ui.theme.FitCraftTheme
import com.example.fitcraft.viewmodel.DatosRutina
import com.example.fitcraft.viewmodel.UsuarioLogeado


class VentanaEjercicios : ComponentActivity() {
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
fun MostrarEjercicios(navController: NavController, datosRutina: DatosRutina) {
    val conexionEjercicio = ConexionEjercicio()
    val ejercicios = remember { mutableStateListOf<Ejercicio>() }

    // Cargar ejercicios desde Firebase
    LaunchedEffect(Unit) {
        conexionEjercicio.cargarEjercicios { listaEjercicios ->
            ejercicios.clear()
            ejercicios.addAll(listaEjercicios)
        }
    }

    Box(
        modifier = Modifier
            .background(ColorFondo)
            .fillMaxSize()
    ) {
        if (ejercicios.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TextoCentrado("Cargando ejercicios...", color = ColorTexto)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ejercicios.forEach { ejercicio ->
                    PanelRutina(
                        ejercicio = ejercicio,
                        onClick = {
                            // Añadir ejercicio a la lista de seleccionados
                            datosRutina.ejerciciosSeleccionados.add(ejercicio)
                            navController.navigate("VentanaCrearRutina")
                        }
                    )
                    DividerConEspaciado()
                }
            }
        }

        // Botón de cancelar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Boton(
                text = "Cancelar",
                onClick = {
                    navController.navigate("VentanaCrearRutina")
                }
            )
        }
    }
}