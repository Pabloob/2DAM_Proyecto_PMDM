package com.example.fitcraft.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.R
import com.example.fitcraft.data.firebase.ConexionRutina
import com.example.fitcraft.data.model.Ejercicio
import com.example.fitcraft.data.model.Rutina
import com.example.fitcraft.ui.NavegadorVentanas
import com.example.fitcraft.ui.components.Boton
import com.example.fitcraft.ui.components.CampoTexto
import com.example.fitcraft.ui.components.CartaEjercicioAnadido
import com.example.fitcraft.ui.components.SeleccionarDia
import com.example.fitcraft.ui.components.SeleccionarHora
import com.example.fitcraft.ui.components.TextoCentrado
import com.example.fitcraft.ui.theme.ColorError
import com.example.fitcraft.ui.theme.ColorFondo
import com.example.fitcraft.ui.theme.ColorFondoSecundario
import com.example.fitcraft.ui.theme.ColorTitulo
import com.example.fitcraft.ui.theme.FitCraftTheme
import com.example.fitcraft.viewmodel.DatosRutina
import com.example.fitcraft.viewmodel.UsuarioLogeado

class VentanaCrearRutina : ComponentActivity() {
    private val datosPersistentes: UsuarioLogeado by viewModels()
    private val datosRutina: DatosRutina by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitCraftTheme {
                val navController = rememberNavController()
                NavegadorVentanas(
                    navController, datosPersistentes, datosRutina
                )
            }
        }
    }
}

@Composable
fun CrearRutina(navController: NavController, usuario: UsuarioLogeado, datosRutina: DatosRutina) {
    var errorMensaje by remember { mutableStateOf("") }
    val conexionRutinas = ConexionRutina()

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
            TextoCentrado("Crear Rutina", color = ColorTitulo)

            Spacer(Modifier.height(16.dp))

            CampoTexto(
                value = datosRutina.nombreRutina.value,
                onValueChange = { datosRutina.nombreRutina.value = it },
                placeholder = "Nombre de la rutina",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.info_crear_rutina),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            CampoTexto(
                value = datosRutina.descripcionRutina.value,
                onValueChange = {
                    datosRutina.descripcionRutina.value = it
                }, // Actualización directa
                placeholder = "Descripción",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.info_crear_rutina),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            SeleccionarDia(datosRutina = datosRutina)

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                SeleccionarHora(
                    modifier = Modifier.weight(1f),
                    horaSeleccionada = datosRutina.horaInicio.value,
                    onHoraSeleccionadaChange = { newHora -> datosRutina.horaInicio.value = newHora }
                )

                Spacer(modifier = Modifier.width(16.dp))

                SeleccionarHora(
                    modifier = Modifier.weight(1f),
                    horaSeleccionada = datosRutina.horaFin.value,
                    onHoraSeleccionadaChange = { newHora -> datosRutina.horaFin.value = newHora }
                )
            }

            Spacer(Modifier.height(16.dp))

            datosRutina.ejerciciosSeleccionados.forEach { ejercicio ->
                val series = datosRutina.seriesYRepeticiones[ejercicio]?.first ?: ""
                val repeticiones = datosRutina.seriesYRepeticiones[ejercicio]?.second ?: ""

                CartaEjercicioAnadido(
                    ejercicio = ejercicio,
                    series = series,
                    repeticiones = repeticiones,
                    onSeriesChange = { newSeries ->
                        datosRutina.seriesYRepeticiones[ejercicio] =
                            newSeries to datosRutina.seriesYRepeticiones[ejercicio]?.second.orEmpty()
                    },
                    onRepeticionesChange = { newRepeticiones ->
                        datosRutina.seriesYRepeticiones[ejercicio] =
                            datosRutina.seriesYRepeticiones[ejercicio]?.first.orEmpty() to newRepeticiones
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            Boton("Añadir Ejercicio", {
                navController.navigate("VentanaEjercicios")
            })

            Spacer(Modifier.height(16.dp))

            Boton("Crear Rutina", {
                if (datosRutina.nombreRutina.value.isEmpty() || datosRutina.ejerciciosSeleccionados.isEmpty()) {
                    errorMensaje = "Por favor, completa todos los campos y añade ejercicios."
                    return@Boton
                }

                val nuevaRutina = Rutina(
                    idRutina = System.currentTimeMillis().toInt(),
                    idPersona = usuario.usuarioActual?.idPersona ?: 0,
                    nombreRutina = datosRutina.nombreRutina.value,
                    descripcion = datosRutina.descripcionRutina.value,
                    dias = datosRutina.diaRutina,
                    horaInicio = datosRutina.horaInicio.value,
                    horaFin = datosRutina.horaFin.value,
                    ejercicios = datosRutina.ejerciciosSeleccionados.map { ejercicio ->
                        Ejercicio(
                            nombreEjercicio = ejercicio.nombreEjercicio,
                            repeticiones = datosRutina.seriesYRepeticiones[ejercicio]?.second?.toIntOrNull()
                                ?: 0,
                            series = datosRutina.seriesYRepeticiones[ejercicio]?.first?.toIntOrNull()
                                ?: 0,
                            descripcion = ejercicio.descripcion,
                            tipoEjercicio = ejercicio.tipoEjercicio
                        )
                    }
                )

                conexionRutinas.agregarRutina(
                    idPersona = usuario.usuarioActual?.idPersona ?: 0,
                    nuevaRutina
                ) { exito ->
                    if (exito) {
                        datosRutina.resetearDatos()
                        navController.navigate("VentanaInicio")
                    } else {
                        errorMensaje = "Error al guardar la rutina. Intenta de nuevo."
                    }
                }
            })

            if (errorMensaje.isNotEmpty()) {
                Text(
                    text = errorMensaje,
                    color = ColorError,
                    modifier = Modifier.padding(top = 8.dp),
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Boton("Cancelar", {
                navController.navigate("VentanaMisRutinas")
                datosRutina.resetearDatos()
            })
        }
    }
}
