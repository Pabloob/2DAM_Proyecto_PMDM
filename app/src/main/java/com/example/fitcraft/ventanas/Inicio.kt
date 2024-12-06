package com.example.fitcraft.ventanas


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.LineChartComponent
import com.example.fitcraft.NavegadorVentanas
import com.example.fitcraft.PanelEjercicio
import com.example.fitcraft.PanelNavegacionInferior
import com.example.fitcraft.R
import com.example.fitcraft.TextoCentrado
import com.example.fitcraft.clases.Rutina
import com.example.fitcraft.conexiones.ConexionRutina
import com.example.fitcraft.ui.theme.ColorFondo
import com.example.fitcraft.ui.theme.ColorFondoSecundario
import com.example.fitcraft.ui.theme.ColorTitulo
import com.example.fitcraft.ui.theme.FitCraftTheme


class VentanaInicio : ComponentActivity() {
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
fun Inicio(navController: NavController, datosPersistentes: DatosPersistentes) {
    val conexionRutinas = ConexionRutina() // Instancia para manejar las rutinas
    val rutinaDelDia = remember { mutableStateOf<Rutina?>(null) }
    val nombreUsuario = datosPersistentes.usuarioActual?.nombreUsuario ?: "Usuario"

    // Estados para mostrar la rutina
    var ejercicio by remember { mutableStateOf("Sin rutina") }
    var horaInicio by remember { mutableStateOf("No programado") }
    var horaFin by remember { mutableStateOf("No programado") }

    // Cargar rutina del día
    LaunchedEffect(Unit) {
        datosPersistentes.usuarioActual?.let { usuario ->
            conexionRutinas.cargarRutinasPorDia(usuarioId = usuario.nombreUsuario) { rutinas ->
                rutinaDelDia.value = rutinas.firstOrNull()
                rutinaDelDia.value?.let {
                    ejercicio = it.nombreRutina
                    horaInicio = it.horaInicio
                    horaFin = it.horaFin
                }
            }
        }
    }

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
            // Texto de bienvenida
            TextoCentrado("Bienvenido $nombreUsuario", color = ColorTitulo)
            Spacer(modifier = Modifier.height(30.dp))

            // Panel con la rutina del día
            PanelEjercicio(
                nombreRutina = ejercicio,
                horaInicio = horaInicio,
                horaFin = horaFin,
                imageRes = R.drawable.ejercicio_pierna // Puedes personalizar esto
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Gráfico de progreso
            LineChartComponent()
        }

        // Panel de navegación inferior
        PanelNavegacionInferior(
            navController = navController,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
    }
}


fun createLineData(): LineData {
    val entries = listOf(
        Entry(0f, 1f),
        Entry(1f, 2f),
        Entry(2f, 3f),
        Entry(3f, 4f),
        Entry(4f, 5f),
        Entry(5f, 6f),
        Entry(6f, 7f)
    )

    val lineDataSet = LineDataSet(entries, "Gráfico horas").apply {
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

@Preview
@Composable
private fun AndroidMedium1Preview() {
    val navController = rememberNavController()
    val datosPersistentes = DatosPersistentes()
    Inicio(navController = navController, datosPersistentes)
}