package com.example.fitcraft.ui.components

import android.app.TimePickerDialog
import android.graphics.drawable.GradientDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.fitcraft.R
import com.example.fitcraft.data.firebase.procesarRutinasSemanal
import com.example.fitcraft.data.model.Rutina
import com.example.fitcraft.ui.theme.ColorCorrecto
import com.example.fitcraft.ui.theme.ColorFondoSecundario
import com.example.fitcraft.ui.theme.ColorNegro
import com.example.fitcraft.ui.theme.ColorTexto
import com.example.fitcraft.ui.theme.esquina25
import com.example.fitcraft.viewmodel.DatosRutina
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun DividerConEspaciado() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        color = ColorNegro
    )
}

@Composable
fun LineChartComponent(lineData: LineData) {
    AndroidView(
        modifier = Modifier
            .padding(top = 30.dp)
            .width(400.dp)
            .height(200.dp),
        factory = { context ->
            LineChart(context).apply {
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(false)
                description.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textColor = Color.White.toArgb()
                xAxis.setDrawGridLines(false)
                xAxis.valueFormatter = IndexAxisValueFormatter(
                    listOf("Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom")
                )

                axisLeft.textColor = Color.White.toArgb()
                axisLeft.setDrawGridLines(true)
                axisLeft.axisMinimum = 0f
                axisLeft.axisMaximum = 6f
                axisLeft.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()}h"
                    }
                }

                axisRight.isEnabled = false
                legend.isEnabled = false

                data = lineData
                animateX(1500)
            }
        }
    )
}


fun createLineData(rutinas: List<Rutina>): LineData {
    val tiempos = procesarRutinasSemanal(rutinas)

    // Crear las entradas para el gráfico
    val entries = tiempos.mapIndexed { index, tiempo ->
        Entry(index.toFloat(), tiempo)
    }

    val lineDataSet = LineDataSet(entries, "Horas dedicadas").apply {
        color = Color.Blue.toArgb()
        setDrawCircles(true)
        setCircleColor(Color.Blue.toArgb())
        lineWidth = 2f
        circleRadius = 5f
        setDrawValues(false)
        setDrawHighlightIndicators(false)
        highLightColor = Color.White.toArgb()
        highlightLineWidth = 1f
        setDrawFilled(true)
        fillDrawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.Blue.copy(alpha = 0.5f).toArgb(),
                Color.Transparent.toArgb()
            )
        )
    }

    return LineData(lineDataSet)
}


@Composable
fun ClickableRow(text: String, color: Color, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            color = color,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun SeleccionarDia(datosRutina: DatosRutina) {
    var expanded by remember { mutableStateOf(false) }
    val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    // Mostrar los días seleccionados
    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = ColorTexto,
            disabledBorderColor = ColorTexto,
            disabledLeadingIconColor = ColorTexto,
            disabledContainerColor = Color.Transparent
        ),
        value = datosRutina.diaRutina.joinToString(", "),
        onValueChange = {},
        enabled = false,
        placeholder = {
            Text(
                text = if (datosRutina.diaRutina.isEmpty()) "Seleccionar día" else "",
                color = ColorTexto
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.DateRange,
                contentDescription = null,
                tint = Color.White
            )
        },
        shape = esquina25,
        modifier = Modifier
            .clickable { expanded = !expanded }
            .fillMaxWidth()
    )

    // Menú desplegable
    Box(modifier = Modifier.fillMaxWidth()) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(ColorFondoSecundario)
        ) {
            dias.forEach { dia ->
                val diaEnMinusculas = dia.lowercase()
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = dia, color = ColorTexto)
                            if (diaEnMinusculas in datosRutina.diaRutina) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = "Seleccionado",
                                    tint = ColorCorrecto
                                )
                            }
                        }
                    },
                    onClick = {
                        if (diaEnMinusculas in datosRutina.diaRutina) {
                            datosRutina.diaRutina.remove(diaEnMinusculas)
                        } else {
                            datosRutina.diaRutina.add(diaEnMinusculas)
                        }
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun SeleccionarHora(
    modifier: Modifier,
    horaSeleccionada: String,
    onHoraSeleccionadaChange: (String) -> Unit
) {

    val context = LocalContext.current
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            onHoraSeleccionadaChange(String.format("%02d:%02d", hourOfDay, minute))
        },
        12,
        0,
        true
    )

    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = ColorTexto,
            disabledBorderColor = ColorTexto,
            disabledLeadingIconColor = ColorTexto,
            disabledContainerColor = Color.Transparent
        ),
        value = horaSeleccionada,
        onValueChange = {},
        enabled = false,
        modifier = modifier
            .clickable { timePickerDialog.show() },
        placeholder = {
            Text(
                text = if (horaSeleccionada.isEmpty()) "Hora" else "",
                color = ColorTexto
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.hora),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        shape = esquina25,
    )
}
