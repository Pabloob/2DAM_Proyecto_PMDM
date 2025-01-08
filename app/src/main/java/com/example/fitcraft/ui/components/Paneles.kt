package com.example.fitcraft.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fitcraft.R
import com.example.fitcraft.data.model.Ejercicio
import com.example.fitcraft.data.model.Rutina
import com.example.fitcraft.ui.theme.ColorFondoSecundario
import com.example.fitcraft.ui.theme.ColorNegro
import com.example.fitcraft.ui.theme.ColorTexto
import com.example.fitcraft.ui.theme.esquina25

@Composable
fun PanelRutinaSimple(rutina: Rutina?) {
    if (rutina == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(ColorFondoSecundario)
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No hay una rutina programada para hoy", style = TextStyle(
                    fontSize = 16.sp, color = ColorTexto
                )
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(ColorFondoSecundario)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                // Título de la rutina
                Text(
                    text = rutina.nombreRutina, style = TextStyle(
                        fontSize = 25.sp, fontWeight = FontWeight.Bold, color = ColorTexto
                    )
                )

                // Horario de la rutina
                Text(
                    text = "${rutina.horaInicio} - ${rutina.horaFin}", style = TextStyle(
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = ColorTexto
                    ), modifier = Modifier.padding(vertical = 8.dp)
                )

                rutina.ejercicios.forEach { ejercicio ->
                    Text(
                        text = "- ${ejercicio.nombreEjercicio}", style = TextStyle(
                            fontSize = 12.sp, color = ColorTexto
                        ), modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                    )
                }
            }

            // Imagen
            Image(
                painter = painterResource(id = R.drawable.rutina),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Top)
            )
        }
    }
}

@Composable
fun PanelRutinaEditable(
    rutina: Rutina,
    onGuardarCambios: (Rutina) -> Unit,
    onClickBorrar: () -> Unit
) {
    var enEdicion by remember { mutableStateOf(false) }

    var nombreRutina by remember { mutableStateOf(rutina.nombreRutina) }
    var horaInicio by remember { mutableStateOf(rutina.horaInicio) }
    var horaFin by remember { mutableStateOf(rutina.horaFin) }
    val ejercicios = remember { mutableStateListOf(*rutina.ejercicios.toTypedArray()) }
    var expandir by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .background(ColorFondoSecundario)
            .border(2.dp, ColorNegro, esquina25)
            .padding(16.dp)
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandir = !expandir }
                        .padding(8.dp)
                ) {
                    CampoTextoEditable(
                        value = nombreRutina,
                        onValueChange = { nombreRutina = it },
                        enabled = enEdicion,
                        placeholder = "Nombre de la rutina",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    CampoTextoEditable(
                        value = horaInicio,
                        onValueChange = { horaInicio = it },
                        enabled = enEdicion,
                        placeholder = "Hora de inicio",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    CampoTextoEditable(
                        value = horaFin,
                        onValueChange = { horaFin = it },
                        enabled = enEdicion,
                        placeholder = "Hora de fin",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = if (expandir) Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = null
                    )
                }

                // Selección de días
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandir = !expandir }
                        .padding(8.dp)
                ) {
                    SeleccionarDiaEditable(
                        rutina = rutina,
                        onActualizarDias = { nuevosDias ->
                            val rutinaActualizada = rutina.copy(dias = nuevosDias)
                            onGuardarCambios(rutinaActualizada)
                        },
                        clickable = enEdicion
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ejercicios
                if (expandir) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 0.dp, max = 500.dp)
                    ) {
                        Text(
                            text = "Ejercicios:",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorTexto
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            itemsIndexed(ejercicios) { index, ejercicio ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = ejercicio.nombreEjercicio,
                                        style = TextStyle(
                                            color = ColorTexto,
                                            fontSize = 16.sp
                                        ),
                                        modifier = Modifier.weight(0.5f)
                                    )

                                    CampoTextoEditable(
                                        value = ejercicio.series.toString(),
                                        onValueChange = { nuevasSeries ->
                                            val seriesInt =
                                                nuevasSeries.toIntOrNull() ?: ejercicio.series
                                            ejercicios[index] = ejercicio.copy(series = seriesInt)
                                        },
                                        enabled = enEdicion,
                                        placeholder = "Series",
                                        modifier = Modifier
                                            .weight(0.3f)
                                            .padding(horizontal = 4.dp),
                                        numerico = true
                                    )

                                    CampoTextoEditable(
                                        value = ejercicio.repeticiones.toString(),
                                        onValueChange = { nuevasRepeticiones ->
                                            val repeticionesInt = nuevasRepeticiones.toIntOrNull()
                                                ?: ejercicio.repeticiones
                                            ejercicios[index] =
                                                ejercicio.copy(repeticiones = repeticionesInt)
                                        },
                                        enabled = enEdicion,
                                        placeholder = "Repeticiones",
                                        modifier = Modifier
                                            .weight(0.3f)
                                            .padding(horizontal = 4.dp),
                                        numerico = true
                                    )

                                    CampoTextoEditable(
                                        value = ejercicio.rir.toString(),
                                        onValueChange = { nuevoRir ->
                                            val rirInt = nuevoRir.toIntOrNull() ?: ejercicio.rir
                                            ejercicios[index] = ejercicio.copy(rir = rirInt)
                                        },
                                        enabled = enEdicion,
                                        placeholder = "Rir",
                                        modifier = Modifier
                                            .weight(0.3f)
                                            .padding(horizontal = 4.dp),
                                        numerico = true
                                    )

                                    IconButton(
                                        onClick = {
                                            if (enEdicion) ejercicios.removeAt(index)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Clear,
                                            contentDescription = null,
                                            tint = if (enEdicion) Color.Red else Color.Transparent
                                        )
                                    }
                                }
                            }
                        }

//                        if (enEdicion) {
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Boton(
//                                text = "Añadir Ejercicio",
//                                onClick = {
//                                    navController.navigate("VentanaEjercicios")
//                                },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                            )
//                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (enEdicion) {
                                Boton(
                                    text = "Guardar",
                                    onClick = {
                                        val rutinaActualizada = rutina.copy(
                                            nombreRutina = nombreRutina,
                                            horaInicio = horaInicio,
                                            horaFin = horaFin,
                                            ejercicios = ejercicios.toList()
                                        )
                                        onGuardarCambios(rutinaActualizada)
                                        enEdicion = false
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Boton(
                                    text = "Cancelar",
                                    onClick = {
                                        nombreRutina = rutina.nombreRutina
                                        horaInicio = rutina.horaInicio
                                        horaFin = rutina.horaFin
                                        ejercicios.clear()
                                        ejercicios.addAll(rutina.ejercicios)
                                        enEdicion = false
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                )
                            } else {
                                Boton(
                                    text = "Editar",
                                    onClick = { enEdicion = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Boton(
                                    text = "Borrar",
                                    onClick = onClickBorrar,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Composable
fun PanelNavegacionInferior(navController: NavController, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .height(70.dp)
            .fillMaxWidth()
            .background(ColorFondoSecundario), contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(100.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            BotonNavegacion(imageRes = R.drawable.btn_inicio,
                onClick = { navController.navigate("VentanaInicio") })
            BotonNavegacion(imageRes = R.drawable.btn_mas,
                onClick = { navController.navigate("VentanaMisRutinas") })
            BotonNavegacion(imageRes = R.drawable.btn_configuracion,
                onClick = { navController.navigate("VentanaAjustes") })
        }
    }
}

@Composable
fun PanelRutinaSimple(ejercicio: Ejercicio, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Tarjeta con los detalles del ejercicio
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(esquina25)
                .background(ColorFondoSecundario)
                .padding(16.dp)
                .height(150.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = ejercicio.nombreEjercicio, color = ColorTexto, style = TextStyle(
                        fontSize = 25.sp, fontWeight = FontWeight.Bold
                    ), modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = ejercicio.descripcion,
                    color = ColorTexto,
                    style = TextStyle(
                        fontSize = 18.sp, fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .verticalScroll(rememberScrollState())
                )
            }

            Image(
                painter = painterResource(
                    id = when (ejercicio.tipoEjercicio) {
                        "Pierna" -> R.drawable.ejercicio_pierna
                        "Espalda" -> R.drawable.ejercicio_espalda
                        "Pecho" -> R.drawable.ejercicico_pecho
                        "Brazo" -> R.drawable.ejercicio_brazo
                        else -> R.drawable.def
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Boton(
            "añadir", onClick = onClick, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

@Composable
fun CartaEjercicioAnadido(
    ejercicio: Ejercicio,
    series: String,
    repeticiones: String,
    rir: String,
    onSeriesChange: (String) -> Unit,
    onRepeticionesChange: (String) -> Unit,
    onRirChange: (String) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(ColorFondoSecundario)
            .padding(horizontal = 8.dp)
    ) {
        // Nombre del ejercicio
        Text(
            text = ejercicio.nombreEjercicio,
            color = ColorTexto,
            modifier = Modifier.weight(1f)
        )

        // Campo editable para las series
        CampoTexto(
            value = series,
            onValueChange = onSeriesChange,
            placeholder = "Series",
            modifier = Modifier.weight(1f),
            numerico = true
        )

        // Campo editable para las repeticiones
        CampoTexto(
            value = repeticiones,
            onValueChange = onRepeticionesChange,
            placeholder = "Repeticiones",
            modifier = Modifier.weight(1f),
            numerico = true
        )

        // Campo editable para el RIR
        CampoTexto(
            value = rir,
            onValueChange = onRirChange,
            placeholder = "RIR",
            modifier = Modifier.weight(1f),
            numerico = true
        )
    }
}
