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
    val ejercicios = remember { mutableStateListOf<Ejercicio>() }
    val conexionEjercicio = ConexionEjercicio()

    LaunchedEffect(Unit) {
        conexionEjercicio.cargarEjercicios { lista ->
            ejercicios.clear()
            ejercicios.addAll(lista)
        }
    }

    Column(
        modifierBox
    ) {
        Box(
            modifier = Modifier.weight(1f)
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
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    ejercicios.forEach { ejercicio ->
                        PanelRutinaSimple(
                            ejercicio = ejercicio,
                            onClick = {
                                datosRutina.ejercicios.add(ejercicio)
                                navController.popBackStack()
                            }
                        )
                        DividerConLinea()
                    }
                }
            }
        }
        Boton(
            text = "Cancelar",
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}