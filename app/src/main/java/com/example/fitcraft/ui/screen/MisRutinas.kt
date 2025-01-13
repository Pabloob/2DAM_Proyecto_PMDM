package com.example.fitcraft.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitcraft.data.firebase.ConexionRutina
import com.example.fitcraft.data.model.Rutina
import com.example.fitcraft.ui.components.Boton
import com.example.fitcraft.ui.components.DividerConLinea
import com.example.fitcraft.ui.components.PanelRutinaEditable
import com.example.fitcraft.ui.components.TextoCentrado
import com.example.fitcraft.ui.theme.ColorTexto
import com.example.fitcraft.ui.theme.modifierBox
import com.example.fitcraft.viewmodel.UsuarioLogeado

@Composable
fun Misrutinas(navController: NavController, usuarioLogeado: UsuarioLogeado) {
    val usuarioActual = usuarioLogeado.usuarioActual  // Usuario logueado actual
    val conexionRutina = ConexionRutina()  // Clase para manejo de rutinas en Firebase
    val rutinas by usuarioLogeado.rutinasUsuario  // Rutinas asociadas al usuario actual
    var errorMensaje by rememberSaveable { mutableStateOf("") }  // Mensaje de error (si existe)

    // Carga de ejercicios desde Firebase
    LaunchedEffect(usuarioActual) {
        if (usuarioActual != null) {
            usuarioLogeado.inicializarUsuario(usuarioActual.idPersona) {
                // Si ocurre un error, navegar al inicio de sesión
                navController.navigate("VentanaIniciarSesion") {
                    popUpTo(0) { inclusive = true }
                }
            }
        } else {
            navController.navigate("VentanaIniciarSesion") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Column(
        modifierBox  // Modificador del contenedor principal
    ) {
        // Contenido desplazable
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())  // Scroll vertical para el contenido
                .padding(30.dp)  // Espaciado interior
        ) {
            // Título centralizado
            TextoCentrado(
                text = "Rutinas",
                color = ColorTexto
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Botón para crear rutina
            Boton(
                text = "Crear rutina",
                onClick = {
                    navController.navigate("VentanaCrearRutina")  // Navegar a la creación de rutina
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            DividerConLinea()  // Divisor con línea

            rutinas.forEach { rutina ->  // Iterar sobre cada rutina
                PanelRutinaEditable(
                    rutina = rutina,
                    onGuardarCambios = { rutinaActualizada ->
                        if (validarRutina(rutinaActualizada)) {  // Validar la rutina
                            try {
                                conexionRutina.actualizarRutina(
                                    idPersona = usuarioActual!!.idPersona,
                                    rutina = rutinaActualizada
                                ) { exito ->
                                    errorMensaje = if (exito) {
                                        "Rutina actualizada con éxito."
                                    } else {
                                        "Error al actualizar la rutina."
                                    }
                                }
                            } catch (e: Exception) {
                                errorMensaje = "Error inesperado: ${e.localizedMessage}"  // Manejar excepciones
                            }
                        } else {
                            errorMensaje = "La rutina debe tener al menos un ejercicio."  // Validación de al menos un ejercicio
                        }
                    },
                    onClickBorrar = {
                        try {
                            usuarioLogeado.usuarioActual?.let { usuario ->
                                conexionRutina.borrarRutina(
                                    idPersona = usuario.idPersona,
                                    rutinaId = rutina.idRutina.toString()
                                ) { exito ->
                                    if (exito) {
                                        navController.navigate("VentanaInicio")  // Navegar a la ventana de inicio
                                    } else {
                                        errorMensaje =
                                            "Error al borrar la rutina. Intenta de nuevo."  // Manejar error al borrar
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            errorMensaje = "Error inesperado: ${e.localizedMessage}"  // Manejar excepciones
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.size(25.dp))
        }

        // Botón de cancelar fijo en la parte inferior
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Boton(
                text = "Cancelar",
                onClick = {
                    navController.navigate("VentanaInicio")  // Navegar al inicio si se cancela
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

fun validarRutina(rutina: Rutina): Boolean {
    return rutina.nombreRutina.isNotEmpty() && rutina.ejercicios.isNotEmpty()  // Verificar si tiene nombre y ejercicios
}
