package com.example.fitcraft.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fitcraft.data.firebase.ConexionPersona
import com.example.fitcraft.ui.components.*
import com.example.fitcraft.ui.theme.*
import com.example.fitcraft.viewmodel.UsuarioLogeado
import com.google.firebase.auth.FirebaseAuth

// Composición principal de la pantalla Ajustes
@Composable
fun Ajustes(navController: NavController, usuarioLogeado: UsuarioLogeado) {
    // Creación de instancia de ConexionPersona para operaciones de Firebase
    val conexionPersona = remember { ConexionPersona() }
    val context = LocalContext.current

    // Estado para manejar cambios en los datos del usuario
    val usuarioActual = usuarioLogeado.usuarioActual
    var auxNombreUsuario by rememberSaveable { mutableStateOf(usuarioActual?.nombreUsuario ?: "") }
    var auxAltura by rememberSaveable { mutableStateOf(usuarioActual?.altura?.toString() ?: "") }
    var auxPeso by rememberSaveable { mutableStateOf(usuarioActual?.peso?.toString() ?: "") }

    // Caja principal que ocupa toda la pantalla y tiene fondo
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondo)
    ) {
        // Contenedor principal con scroll vertical
        Column(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
        ) {
            // Título centrado "Ajustes"
            TextoCentrado("Ajustes", color = ColorTitulo)

            // Contenedor secundario con forma redondeada y fondo secundario
            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(ColorFondoSecundario)
                    .padding(20.dp)
            ) {
                // Fila para editar nombre de usuario
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    CampoTexto(
                        value = auxNombreUsuario,
                        onValueChange = { auxNombreUsuario = it },
                        placeholder = "Nombre de usuario",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Fila para editar peso
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    CampoTexto(
                        value = auxPeso,
                        onValueChange = { auxPeso = it },
                        placeholder = "Peso (kg)",
                        modifier = Modifier.fillMaxWidth(),
                        numerico = true
                    )
                }

                // Fila para editar altura
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    CampoTexto(
                        value = auxAltura,
                        onValueChange = { auxAltura = it },
                        placeholder = "Altura (cm)",
                        modifier = Modifier.fillMaxWidth(),
                        numerico = true
                    )
                }

                // Espaciado entre filas
                Spacer(modifier = Modifier.height(10.dp))

                // Fila para guardar cambios
                ClickableRow(
                    text = "Guardar cambios",
                    color = ColorTitulo,
                    onClick = {
                        if (auxAltura.toFloatOrNull() != null && auxPeso.toFloatOrNull() != null && auxAltura.toFloat() > 0 && auxPeso.toFloat() > 0) {
                            usuarioActual?.let { usuario ->
                                usuario.nombreUsuario = auxNombreUsuario
                                usuario.altura = auxAltura.toFloat()
                                usuario.peso = auxPeso.toFloat()

                                // Actualizar usuario en Firebase
                                conexionPersona.actualizarUsuario(usuario) { exito ->
                                    if (exito) {
                                        Toast.makeText(
                                            context,
                                            "Cambios guardados",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Error al guardar los cambios",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        } else {
                            // Mostrar mensaje si la validación falla
                            Toast.makeText(
                                context,
                                "Error al guardar los cambios",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )

                // Fila para cerrar sesión
                ClickableRow(
                    text = "Cerrar sesión",
                    color = ColorError,
                    onClick = {
                        // Cerrar sesión en Firebase
                        FirebaseAuth.getInstance().signOut()
                        usuarioLogeado.cerrarSesion()

                        // Limpiar sesión local
                        val sharedPreferences =
                            context.getSharedPreferences("FitCraftPrefs", Context.MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            remove("uid")
                            apply()
                        }

                        // Redirigir a la pantalla de inicio de sesión
                        navController.navigate("VentanaIniciarSesion") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )

                // Fila para borrar cuenta
                ClickableRow(
                    text = "Borrar cuenta",
                    color = ColorError,
                    onClick = {
                        usuarioActual?.let { usuario ->
                            conexionPersona.borrarUsuarioPorId(usuario) { exito ->
                                if (exito) {
                                    usuarioLogeado.cerrarSesion()
                                    val sharedPreferences = context.getSharedPreferences(
                                        "FitCraftPrefs",
                                        Context.MODE_PRIVATE
                                    )
                                    with(sharedPreferences.edit()) {
                                        clear()
                                        apply()
                                    }

                                    Toast.makeText(context, "Cuenta eliminada", Toast.LENGTH_SHORT)
                                        .show()
                                    navController.navigate("VentanaIniciarSesion") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error al borrar la cuenta. Intenta nuevamente.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                )
            }
        }

        // Panel inferior para navegación
        PanelNavegacionInferior(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}
