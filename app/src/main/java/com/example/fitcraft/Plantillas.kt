package com.example.fitcraft

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.example.fitcraft.clases.Ejercicio
import com.example.fitcraft.ui.theme.*
import com.example.fitcraft.ventanas.Ajustes
import com.example.fitcraft.ventanas.Borrar
import com.example.fitcraft.ventanas.CrearRutina
import com.example.fitcraft.ventanas.IniciarSesion
import com.example.fitcraft.ventanas.DatosPersistentes
import com.example.fitcraft.ventanas.Inicio
import com.example.fitcraft.ventanas.MostrarEjercicios
import com.example.fitcraft.ventanas.Registrar
import com.example.fitcraft.ventanas.createLineData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter


@Composable
fun NavegadorVentanas(
    navHostController: NavHostController,
    datosPersistentes: DatosPersistentes
) {

    NavHost(
        navController = navHostController,
        startDestination = "VentanaIniciarSesion"
    ) {
        composable("VentanaIniciarSesion") {
            IniciarSesion(navHostController, datosPersistentes)
        }
        composable("VentanaRegistrar") {
            Registrar(navHostController)
        }
        composable("VentanaInicio") {
            Inicio(navHostController, datosPersistentes)
        }
        composable("VentanaBorrarCuentas") {
            Borrar(navHostController)
        }
        composable("VentanaEjercicios") {
            MostrarEjercicios(navHostController, datosPersistentes)
        }
        composable("VentanaCrearRutina") {
            CrearRutina(navHostController, datosPersistentes)
        }
        composable("VentanaAjustes") {
            Ajustes(navHostController, datosPersistentes)
        }
    }
}

@Composable
fun TextoCentrado(
    text: String,
    style: TextStyle = TitleTextStyle,
    modifier: Modifier = Modifier,
    color: Color
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp)
    ) {
        Text(text = text, style = style, modifier = modifier, color = color)
    }
}

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
fun TextoInteractivo(texto: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Text(
        text = texto,
        color = ColorTexto,
        modifier = modifier
            .clickable { onClick() }
            .padding(top = 16.dp)
    )
}

@Composable
fun Boton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = ColorBoton,
            contentColor = ColorTextoBoton
        )
    ) {
        Text(text)
    }
}


@Composable
fun PanelEjercicio(nombreRutina: String, horaInicio: String, horaFin: String, imageRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(esquina25)
            .background(ColorFondoSecundario)
            .height(180.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp, top = 20.dp)
        ) {
            Text(
                text = nombreRutina,
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTexto
                )
            )
            Text(
                text = "$horaInicio - $horaFin",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorTexto
                )
            )
        }
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(end = 16.dp, bottom = 16.dp)
                .align(Alignment.Bottom)
        )
    }
}

@Composable
fun LineChartComponent() {
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
                data = createLineData()
                animateX(1500)
            }
        }
    )
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
                onClick = { navController.navigate("VentanaCrearRutina") }
            )
            BotonNavegacion(
                imageRes = R.drawable.btn_configuracion,
                onClick = { navController.navigate("VentanaAjustes") }
            )
        }
    }
}

@Composable
fun BotonNavegacion(imageRes: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier
            .size(20.dp)
            .clickable { onClick() }
    )
}

@Composable
fun CampoTexto(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: Int? = null,
    modifier: Modifier
) {

    // Muestra el icono si está definido
    leadingIcon?.let {
        Image(
            painter = painterResource(id = it),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp)
        )
    }

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = ColorTexto) },
        singleLine = true,
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        modifier = modifier
            .height(60.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun CampoTextoConContrasena(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: Int? = null // Acepta un recurso de imagen opcional
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Muestra el icono si está definido
        leadingIcon?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
        }

        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = ColorTexto) },
            singleLine = true,
            visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon =
            {
                Text(
                    text = if (passwordVisible) "Ocultar" else "Mostrar",
                    color = ColorTexto,
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            },
            textStyle = TextStyle(color = ColorTexto, fontSize = 18.sp),
            modifier = Modifier
                .weight(1f)
                .height(60.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
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
fun CartaEjercicio(ejercicio: Ejercicio, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Tarjeta con los detalles del ejercicio
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
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
fun CartaEjercicioAñadido(
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
            .height(50.dp) // Altura ajustada para mantener la fila compacta
            .background(ColorFondoSecundario)
            .padding(horizontal = 8.dp) // Espaciado lateral
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
            modifier = Modifier
                .weight(1f)

        )

        CampoTexto(
            value = repeticiones,
            onValueChange = onRepeticionesChange,
            placeholder = "Repeticiones",
            modifier = Modifier
                .weight(1.5f)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun CartaEjercicioAñadidoPreview() {
    val ejercicioDemo = Ejercicio(
        nombreEjercicio = "Sentadilla",
        repeticiones = 0,
        series = 0,
        descripcion = "Ejercicio para piernas",
        tipoEjercicio = "Pierna"
    )
    var series by remember { mutableStateOf("3") }
    var repeticiones by remember { mutableStateOf("12") }

    CartaEjercicioAñadido(
        ejercicio = ejercicioDemo,
        series = series,
        repeticiones = repeticiones,
        onSeriesChange = { series = it },
        onRepeticionesChange = { repeticiones = it }
    )
}
