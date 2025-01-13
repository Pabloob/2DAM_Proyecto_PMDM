package com.example.fitcraft.ui.screen

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitcraft.data.firebase.ConexionEjercicio
import com.example.fitcraft.data.model.Ejercicio
import com.example.fitcraft.ui.components.Boton
import com.example.fitcraft.ui.components.DividerConLinea
import com.example.fitcraft.ui.components.PanelRutinaSimple
import com.example.fitcraft.ui.components.TextoCentrado
import com.example.fitcraft.ui.theme.ColorTexto
import com.example.fitcraft.ui.theme.modifierBox
import com.example.fitcraft.viewmodel.DatosRutina

@Composable
fun MostrarEjercicios(navController: NavController, datosRutina: DatosRutina) {
    // Lista de ejercicios, inicializada vacía
    val ejercicios = remember { mutableStateListOf<Ejercicio>() }
    // Referencia a la clase de conexión con Firebase para ejercicios
    val conexionEjercicio = ConexionEjercicio()

    // Efecto para cargar los ejercicios al entrar a la pantalla
    LaunchedEffect(Unit) {
        conexionEjercicio.cargarEjercicios { lista ->
            ejercicios.clear()  // Limpiar lista anterior
            ejercicios.addAll(lista)  // Agregar nuevos ejercicios a la lista
        }
    }

    Column(
        modifierBox
    ) {
        Box(
            modifier = Modifier.weight(1f)  // Contenedor para los ejercicios
        ) {
            if (ejercicios.isEmpty()) {
                // Mensaje de carga si no hay ejercicios disponibles
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TextoCentrado("Cargando ejercicios...", color = ColorTexto)
                }
            } else {
                // Mostrar lista de ejercicios cargados
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    ejercicios.forEach { ejercicio ->
                        // Panel para cada ejercicio con opción de agregar a la rutina
                        PanelRutinaSimple(
                            ejercicio = ejercicio,
                            onClick = {
                                datosRutina.ejercicios.add(ejercicio)  // Agregar ejercicio a la rutina
                                navController.popBackStack()  // Volver a la pantalla anterior
                            }
                        )
                        DividerConLinea()  // Divisor entre ejercicios
                    }
                }
            }
        }
        // Botón para cancelar y regresar
        Boton(
            text = "Cancelar",
            onClick = {
                navController.popBackStack()  // Regresar a la pantalla anterior
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
