package com.example.fitcraft.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.data.firebase.ConexionRutina
import com.example.fitcraft.data.model.Rutina
import com.example.fitcraft.ui.NavegadorVentanas
import com.example.fitcraft.ui.components.Boton
import com.example.fitcraft.ui.components.DividerConEspaciado
import com.example.fitcraft.ui.components.PanelRutinaEditar
import com.example.fitcraft.ui.components.TextoCentrado
import com.example.fitcraft.ui.theme.ColorFondoSecundario
import com.example.fitcraft.ui.theme.ColorTexto
import com.example.fitcraft.ui.theme.FitCraftTheme
import com.example.fitcraft.viewmodel.DatosRutina
import com.example.fitcraft.viewmodel.UsuarioLogeado


class MisRutinas : ComponentActivity() {
    private val usuarioLogeado: UsuarioLogeado by viewModels()
    private val datosRutina: DatosRutina by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitCraftTheme {
                val navController = rememberNavController()
                NavegadorVentanas(
                    navController,
                    usuarioLogeado,
                    datosRutina,
                )
            }
        }
    }
}

@Composable
fun Misrutinas(navController: NavController, usuarioLogeado: UsuarioLogeado) {

    val conexionRutina = ConexionRutina() // Instancia para manejar ejercicios
    val rutinas = remember { SnapshotStateList<Rutina>() }
    var errorMensaje by remember { mutableStateOf("") }

    var idUsuario by remember { mutableIntStateOf(0) }

    // Cargar ejercicios desde Firebase
    LaunchedEffect(Unit) {
        usuarioLogeado.usuarioActual?.let { usuario ->
            idUsuario = usuario.idPersona
            conexionRutina.cargarTodasLasRutinas(idUsuario) { rutina ->
                if (rutina.isNotEmpty()) {
                    rutinas.clear()
                    rutinas.addAll(rutina)
                } else {
                    errorMensaje = "No tienes rutinas creadas aún."
                }
            }
        } ?: run {
            errorMensaje = "No se pudo cargar el usuario."
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(ColorFondoSecundario)
                .padding(30.dp)
        ) {

            TextoCentrado(
                text = "Rutinas",
                color = ColorTexto
            )

            Spacer(modifier = Modifier.height(10.dp))

            Boton("Crear rutina", onClick = {
                navController.navigate("VentanaCrearRutina")
            })
            DividerConEspaciado()

            // Mostrar lista de ejercicios
            rutinas.forEach { rutina ->
                PanelRutinaEditar(
                    rutina,
                    onGuardarCambios = { rutinaActualizada ->
                        if (validarRutina(rutinaActualizada)) {
                            conexionRutina.actualizarRutina(
                                idPersona = idUsuario,
                                rutina = rutinaActualizada
                            ) { exito ->
                                errorMensaje = if (exito) {
                                    "Rutina actualizada con éxito."
                                } else {
                                    "Error al actualizar la rutina."
                                }
                            }
                        } else {
                            errorMensaje = "La rutina debe tener al menos un ejercicio."
                        }
                    },
                    onClickBorrar = {
                        usuarioLogeado.usuarioActual?.let { usuario ->
                            conexionRutina.borrarRutina(
                                idPersona = usuario.idPersona,
                                rutinaId = rutina.idRutina.toString()
                            ) { exito ->
                                if (exito) {
                                    navController.navigate("VentanaInicio")
                                } else {
                                    errorMensaje =
                                        "Error al borrar la rutina. Intenta de nuevo."
                                }
                            }
                        }
                    },
                )
            }
            Spacer(modifier = Modifier.size(25.dp))
        }

        // Botón de cancelar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Boton(
                text = "Cancelar",
                onClick = {
                    navController.navigate("VentanaInicio")
                }
            )
        }

    }

}

fun validarRutina(rutina: Rutina): Boolean {
    return rutina.nombreRutina.isNotEmpty() && rutina.ejercicios.isNotEmpty()
}