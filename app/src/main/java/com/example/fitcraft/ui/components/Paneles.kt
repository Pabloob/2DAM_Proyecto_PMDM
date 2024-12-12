package com.example.fitcraft.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.tooling.preview.Preview
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
fun PanelRutina(rutina: Rutina?) {
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
                text = "No hay una rutina programada para hoy",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = ColorTexto
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
                    text = rutina.nombreRutina,
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTexto
                    )
                )

                // Horario de la rutina
                Text(
                    text = "${rutina.horaInicio} - ${rutina.horaFin}",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTexto
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                rutina.ejercicios.forEach { ejercicio ->
                    Text(
                        text = "- ${ejercicio.nombreEjercicio}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = ColorTexto
                        ),
                        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
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
fun PanelRutinaEditar(
    rutina: Rutina,
    onGuardarCambios: (Rutina) -> Unit,
    onClickBorrar: () -> Unit
) {
    // Estado para alternar entre edición y visualización
    var enEdicion by remember { mutableStateOf(false) }

    // Estados locales para editar los datos de la rutina
    var nombreRutina by remember { mutableStateOf(rutina.nombreRutina) }
    var horaInicio by remember { mutableStateOf(rutina.horaInicio) }
    var horaFin by remember { mutableStateOf(rutina.horaFin) }
    val ejercicios = remember { mutableStateListOf(*rutina.ejercicios.toTypedArray()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(25.dp))
            .background(ColorFondoSecundario)
            .border(2.dp, ColorNegro, RoundedCornerShape(25.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Campo para el nombre de la rutina
                CampoTextoEditable(
                    value = nombreRutina,
                    onValueChange = { nombreRutina = it },
                    enabled = enEdicion,
                    "Nombre de la rutina",
                    Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Campos para las horas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CampoTextoEditable(
                        value = horaInicio,
                        onValueChange = { horaInicio = it },
                        enabled = enEdicion,
                        "Hora de inicio",
                        Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    CampoTextoEditable(
                        value = horaFin,
                        onValueChange = { horaFin = it },
                        enabled = enEdicion,
                        "Hora de fin",
                        Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Lista de ejercicios
                Text(
                    text = "Ejercicios:",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorTexto
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ejercicios.forEachIndexed { index, ejercicio ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Nombre del ejercicio (no editable)
                        Text(
                            text = ejercicio.nombreEjercicio,
                            style = TextStyle(color = ColorTexto, fontSize = 16.sp),
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 16.dp)
                        )

                        // Series (editable si está en modo edición)
                        TextField(
                            value = ejercicio.series.toString(),
                            onValueChange = { nuevasSeries ->
                                val seriesInt = nuevasSeries.toIntOrNull() ?: ejercicio.series
                                ejercicios[index] = ejercicio.copy(series = seriesInt)
                            },
                            enabled = enEdicion,
                            placeholder = { Text("Series", color = ColorTexto) },
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(horizontal = 8.dp),
                            textStyle = TextStyle(color = ColorTexto, fontSize = 14.sp),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            )
                        )

                        // Repeticiones (editable si está en modo edición)
                        TextField(
                            value = ejercicio.repeticiones.toString(),
                            onValueChange = { nuevasRepeticiones ->
                                val repeticionesInt = nuevasRepeticiones.toIntOrNull() ?: ejercicio.repeticiones
                                ejercicios[index] = ejercicio.copy(repeticiones = repeticionesInt)
                            },
                            enabled = enEdicion,
                            placeholder = { Text("Repeticiones", color = ColorTexto) },
                            modifier = Modifier
                                .weight(0.5f)
                                .padding(horizontal = 8.dp),
                            textStyle = TextStyle(color = ColorTexto, fontSize = 14.sp),
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            )
                        )

                        // Botón para eliminar ejercicio alineado completamente a la derecha
                        if (enEdicion) {
                            IconButton(
                                onClick = { ejercicios.removeAt(index) },
                                modifier = Modifier
                                    .size(32.dp) // Tamaño del botón para una mejor apariencia
                                    .padding(start = 8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_close), // Asegúrate de tener un ícono "X"
                                    contentDescription = "Eliminar",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }




                if (enEdicion) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Boton2(
                        text = "Añadir Ejercicio",
                        onClick = {
                            ejercicios.add(
                                Ejercicio(nombreEjercicio = "Nuevo Ejercicio")
                            )
                        }
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.rutina),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (enEdicion) {
                Boton2(
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
                    }
                )
                Boton2(
                    text = "Cancelar",
                    onClick = {
                        // Restaurar valores originales
                        nombreRutina = rutina.nombreRutina
                        horaInicio = rutina.horaInicio
                        horaFin = rutina.horaFin
                        ejercicios.clear()
                        ejercicios.addAll(rutina.ejercicios)
                        enEdicion = false
                    }
                )
            } else {
                Boton2(
                    text = "Editar",
                    onClick = { enEdicion = true }
                )
                Boton2(
                    text = "Borrar",
                    onClick = onClickBorrar
                )
            }
        }
    }

    Spacer(modifier = Modifier.size(18.dp))
}

@Composable
fun PanelNavegacionInferior(navController: NavController, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .height(70.dp)
            .fillMaxWidth()
            .background(ColorFondoSecundario),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(100.dp, Alignment.CenterHorizontally),
            modifier = Modifier.fillMaxWidth()
        ) {
            BotonNavegacion(
                imageRes = R.drawable.btn_inicio,
                onClick = { navController.navigate("VentanaInicio") }
            )
            BotonNavegacion(
                imageRes = R.drawable.btn_mas,
                onClick = { navController.navigate("VentanaMisRutinas") }
            )
            BotonNavegacion(
                imageRes = R.drawable.btn_configuracion,
                onClick = { navController.navigate("VentanaAjustes") }
            )
        }
    }
}

@Composable
fun PanelRutina(ejercicio: Ejercicio, onClick: () -> Unit) {
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
                .height(150.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = ejercicio.nombreEjercicio,
                    color = ColorTexto,
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = ejercicio.descripcion,
                    color = ColorTexto,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
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

        // Botón debajo de la tarjeta
        Boton(
            "añadir",
            onClick = onClick,
            modifier = Modifier
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
    onSeriesChange: (String) -> Unit,
    onRepeticionesChange: (String) -> Unit
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
        Text(
            text = ejercicio.nombreEjercicio,
            color = ColorTexto,
            modifier = Modifier
                .weight(1f)
        )

        CampoTexto(
            value = series,
            onValueChange = onSeriesChange,
            placeholder = "Series",
            modifier = Modifier.weight(1f)
        )

        CampoTexto(
            value = repeticiones,
            onValueChange = onRepeticionesChange,
            placeholder = "Repeticiones",
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun PanelRutinaEditarPreview() {
    val rutinaMock = Rutina(
        nombreRutina = "Rutina de Pierna",
        horaInicio = "07:00",
        horaFin = "08:30",
        ejercicios = listOf(
            Ejercicio(nombreEjercicio = "Sentadillas"),
            Ejercicio(nombreEjercicio = "Zancadas"),
            Ejercicio(nombreEjercicio = "Prensa de pierna")
        )
    )

    PanelRutinaEditar(
        rutina = rutinaMock,
        onClickBorrar = { println("Editar clicado") },
        onGuardarCambios = { println("Borrar clicado") }
    )
}
