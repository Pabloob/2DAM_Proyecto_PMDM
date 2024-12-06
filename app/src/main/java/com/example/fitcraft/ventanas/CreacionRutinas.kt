package com.example.fitcraft.ventanas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.NavegadorVentanas
import com.example.fitcraft.R
import com.example.fitcraft.*
import com.example.fitcraft.clases.Ejercicio
import com.example.fitcraft.clases.Rutina
import com.example.fitcraft.conexiones.ConexionRutina
import com.example.fitcraft.ui.theme.FitCraftTheme
import com.example.fitcraft.ui.theme.ColorCorrecto
import com.example.fitcraft.ui.theme.ColorError
import com.example.fitcraft.ui.theme.ColorFondo
import com.example.fitcraft.ui.theme.ColorFondoSecundario
import com.example.fitcraft.ui.theme.ColorTexto
import com.example.fitcraft.ui.theme.ColorTitulo


class VentanaCrearRutina : ComponentActivity() {
    private val datosPersistentes: DatosPersistentes by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitCraftTheme {
                val navController = rememberNavController()
                NavegadorVentanas(
                    navController, datosPersistentes
                )
            }
        }
    }
}

@Composable
fun CrearRutina(navController: NavController, datosPersistentes: DatosPersistentes) {
    // Estados para los datos de la rutina
    var nombreRutina by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var horaFin by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf("") }

    val conexionRutinas = ConexionRutina() // Conexión a Firebase para rutinas

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(RoundedCornerShape(50.dp))
                .background(ColorFondoSecundario)
                .padding(30.dp)
        ) {
            TextoCentrado("Crear Rutina",color= ColorTitulo)

            Spacer(Modifier.height(16.dp))

            // Campo para el nombre de la rutina
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                CampoTexto(
                    value = nombreRutina,
                    onValueChange = { nombreRutina = it },
                    placeholder = "Nombre de la rutina",
                    leadingIcon = R.drawable.info_crear_rutina,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(16.dp))

            // Campo para la descripción de la rutina
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                CampoTexto(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = "Descripción",
                    leadingIcon = R.drawable.info_crear_rutina,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(16.dp))

            // Selección de días
            SeleccionarDia(datosPersistentes = datosPersistentes)

            Spacer(Modifier.height(16.dp))

            // Campos para las horas
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CampoTexto(
                    value = horaInicio,
                    onValueChange = { horaInicio = it },
                    placeholder = "Hora inicio (HH:mm)",
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                CampoTexto(
                    value = horaFin,
                    onValueChange = { horaFin = it },
                    placeholder = "Hora fin (HH:mm)",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Mostrar ejercicios seleccionados
            datosPersistentes.ejerciciosSeleccionados.forEach { ejercicio ->
                val series = datosPersistentes.seriesYRepeticiones[ejercicio]?.first ?: ""
                val repeticiones = datosPersistentes.seriesYRepeticiones[ejercicio]?.second ?: ""

                CartaEjercicioAñadido(
                    ejercicio = ejercicio,
                    series = series,
                    repeticiones = repeticiones,
                    onSeriesChange = { newSeries ->
                        datosPersistentes.seriesYRepeticiones[ejercicio] =
                            newSeries to datosPersistentes.seriesYRepeticiones[ejercicio]?.second.orEmpty()
                    },
                    onRepeticionesChange = { newRepeticiones ->
                        datosPersistentes.seriesYRepeticiones[ejercicio] =
                            datosPersistentes.seriesYRepeticiones[ejercicio]?.first.orEmpty() to newRepeticiones
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Botón para añadir ejercicio
            Boton("Añadir Ejercicio", {
                navController.navigate("VentanaEjercicios")
            })

            Spacer(Modifier.height(16.dp))

            // Botón para crear la rutina
            Boton("Crear Rutina", {
                if (nombreRutina.isEmpty() || datosPersistentes.ejerciciosSeleccionados.isEmpty()) {
                    errorMensaje = "Por favor, completa todos los campos y añade ejercicios."
                    return@Boton
                }

                val nuevaRutina = Rutina(
                    idRutina = System.currentTimeMillis().toInt(),
                    idPersona = datosPersistentes.usuarioActual?.idPersona ?: 0,
                    nombreRutina = nombreRutina,
                    descripcion = descripcion,
                    dias = datosPersistentes.diaRutina,
                    horaInicio = horaInicio,
                    horaFin = horaFin,
                    ejercicios = datosPersistentes.ejerciciosSeleccionados.map { ejercicio ->
                        Ejercicio(
                            nombreEjercicio = ejercicio.nombreEjercicio,
                            repeticiones = datosPersistentes.seriesYRepeticiones[ejercicio]?.second?.toIntOrNull()
                                ?: 0,
                            series = datosPersistentes.seriesYRepeticiones[ejercicio]?.first?.toIntOrNull()
                                ?: 0,
                            descripcion = ejercicio.descripcion,
                            tipoEjercicio = ejercicio.tipoEjercicio
                        )
                    }
                )

                // Guardar rutina en Firebase
                datosPersistentes.usuarioActual?.let { usuario ->
                    conexionRutinas.agregarRutina(
                        usuarioId = usuario.nombreUsuario,
                        nuevaRutina
                    ) { exito ->
                        if (exito) {
                            navController.navigate("VentanaInicio")
                        } else {
                            errorMensaje = "Error al guardar la rutina. Intenta de nuevo."
                        }
                    }
                }
            })

            // Mostrar mensaje de error
            if (errorMensaje.isNotEmpty()) {
                Text(
                    text = errorMensaje,
                    color = ColorError,
                    modifier = Modifier.padding(top = 8.dp),
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Botón para cancelar
            Boton("Cancelar", {
                navController.navigate("VentanaInicio")
            })
        }
    }
}

@Composable
fun SeleccionarDia(datosPersistentes: DatosPersistentes) {
    var expanded by remember { mutableStateOf(false) }
    val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Mostrar los días seleccionados
        Text(
            text = "Días seleccionados: ${datosPersistentes.diaRutina.joinToString(", ")}",
            color = ColorTexto,
            fontSize = 18.sp,
            modifier = Modifier
                .clickable { expanded = !expanded }
        )

        // Menú desplegable
        Box(modifier = Modifier.fillMaxWidth()) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(ColorFondoSecundario)
            ) {
                dias.forEach { dia ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = dia)
                                // Indicar si el día está seleccionado
                                if (dia in datosPersistentes.diaRutina) {
                                    Text(text = "✔", color = ColorCorrecto)
                                }
                            }
                        },
                        onClick = {
                            val diaEnMinusculas = dia.lowercase()
                            if (diaEnMinusculas in datosPersistentes.diaRutina) {
                                datosPersistentes.diaRutina.remove(diaEnMinusculas) // Deseleccionar
                            } else {
                                datosPersistentes.diaRutina.add(diaEnMinusculas) // Seleccionar
                            }
                            expanded = false
                        }

                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun CrearRutinaPreview() {
    val navController = rememberNavController()
    val datosPersistentes = DatosPersistentes()
    CrearRutina(navController = navController, datosPersistentes = datosPersistentes)
}