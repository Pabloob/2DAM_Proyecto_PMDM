package com.example.fitcraft.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.R
import com.example.fitcraft.data.firebase.ConexionRutina
import com.example.fitcraft.data.model.Rutina
import com.example.fitcraft.ui.NavegadorVentanas
import com.example.fitcraft.ui.components.Boton
import com.example.fitcraft.ui.components.CampoTexto
import com.example.fitcraft.ui.components.CartaEjercicioAnadido
import com.example.fitcraft.ui.components.SeleccionarDia
import com.example.fitcraft.ui.components.SeleccionarHora
import com.example.fitcraft.ui.components.TextoCentrado
import com.example.fitcraft.ui.theme.ColorError
import com.example.fitcraft.ui.theme.ColorTitulo
import com.example.fitcraft.ui.theme.FitCraftTheme
import com.example.fitcraft.ui.theme.modifierBox
import com.example.fitcraft.ui.theme.modifierColumna
import com.example.fitcraft.viewmodel.DatosRutina
import com.example.fitcraft.viewmodel.EjercicioValores
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
    var errorMensaje by rememberSaveable { mutableStateOf("") }
    val conexionRutinas = ConexionRutina()

    Box(
        modifierBox,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifierColumna.verticalScroll(rememberScrollState())

        ) {
            TextoCentrado("Crear Rutina", color = ColorTitulo)

            Spacer(Modifier.height(16.dp))

            // Campo para el nombre de la rutina
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

            // Campo para la descripción de la rutina
            CampoTexto(
                value = datosRutina.descripcionRutina.value,
                onValueChange = { datosRutina.descripcionRutina.value = it },
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

            // Selección de días
            SeleccionarDia(datosRutina = datosRutina)

            Spacer(Modifier.height(16.dp))

            // Selección de horas
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

            datosRutina.ejercicios.forEach { ejercicio ->
                val valoresEjercicio =
                    datosRutina.datosEjercicio[ejercicio] ?: EjercicioValores("", "", "")

                CartaEjercicioAnadido(
                    ejercicio = ejercicio,
                    series = valoresEjercicio.series,
                    repeticiones = valoresEjercicio.repeticiones,
                    rir = valoresEjercicio.rir,
                    onSeriesChange = { newSeries ->
                        datosRutina.datosEjercicio[ejercicio] =
                            valoresEjercicio.copy(series = newSeries)
                    },
                    onRepeticionesChange = { newRepeticiones ->
                        datosRutina.datosEjercicio[ejercicio] =
                            valoresEjercicio.copy(repeticiones = newRepeticiones)
                    },
                    onRirChange = { newRir ->
                        datosRutina.datosEjercicio[ejercicio] =
                            valoresEjercicio.copy(rir = newRir)
                    }
                )
            }




            Spacer(Modifier.height(16.dp))

            // Botón para añadir un nuevo ejercicio
            Boton(
                text = "Añadir Ejercicio",
                onClick = { navController.navigate("VentanaEjercicios") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Botón para crear la rutina
            Boton(
                text = "Crear Rutina",
                onClick = {
                    if (datosRutina.nombreRutina.value.isEmpty() || datosRutina.ejercicios.isEmpty()) {
                        errorMensaje = "Por favor, completa todos los campos y añade ejercicios."
                        return@Boton
                    }

                    val nuevaRutina = Rutina(
                        idRutina = System.currentTimeMillis().toInt(),
                        idPersona = usuario.usuarioActual?.idPersona ?: 0,
                        nombreRutina = datosRutina.nombreRutina.value,
                        descripcion = datosRutina.descripcionRutina.value,
                        dias = datosRutina.dias,
                        horaInicio = datosRutina.horaInicio.value,
                        horaFin = datosRutina.horaFin.value,
                        ejercicios = datosRutina.ejercicios.map { ejercicio ->
                            ejercicio.copy() // Crea una copia del objeto ejercicio
                        }
                    )

                    // Guardar la rutina en Firebase
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
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Mostrar mensaje de error si hay algún problema
            if (errorMensaje.isNotEmpty()) {
                Text(
                    text = errorMensaje,
                    color = ColorError,
                    modifier = Modifier.padding(top = 8.dp),
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Botón para cancelar y regresar
            Boton(
                text = "Cancelar",
                onClick = {
                    navController.navigate("VentanaMisRutinas")
                    datosRutina.resetearDatos()
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
