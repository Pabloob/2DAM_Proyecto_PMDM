package com.example.fitcraft.ventanas

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.Boton
import com.example.fitcraft.CartaEjercicio
import com.example.fitcraft.DividerConEspaciado
import com.example.fitcraft.NavegadorVentanas
import com.example.fitcraft.R
import com.example.fitcraft.TextoCentrado
import com.example.fitcraft.clases.Ejercicio
import com.example.fitcraft.conexiones.ConexionEjercicio
import com.example.fitcraft.ui.theme.*


class VentanaEjercicios : ComponentActivity() {
    private val datosPersistentes: DatosPersistentes by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitCraftTheme {
                val navController = rememberNavController()
                NavegadorVentanas(
                    navHostController = navController,
                    datosPersistentes = datosPersistentes
                )
            }
        }
    }
}


@Composable
fun MostrarEjercicios(navController: NavController, datosPersistentes: DatosPersistentes) {
    val conexionEjercicio = ConexionEjercicio() // Instancia para manejar ejercicios
    val ejercicios = remember { SnapshotStateList<Ejercicio>() }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(50.dp))
                    .background(ColorFondoSecundario)
                    .padding(30.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                TextoCentrado(
                    text = stringResource(id = R.string.app_name),
                        color= ColorTexto
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar lista de ejercicios
                ejercicios.forEach { ejercicio ->
                    CartaEjercicio(
                        ejercicio = ejercicio,
                        onClick = {
                            // Añadir ejercicio a la lista de seleccionados
                            datosPersistentes.ejerciciosSeleccionados.add(ejercicio)
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


@Preview
@Composable
private fun MostrarEjerciciosPreview() {
    val navController = rememberNavController()
    val datosPersistentes = DatosPersistentes()
    MostrarEjercicios(navController = navController, datosPersistentes = datosPersistentes)
}