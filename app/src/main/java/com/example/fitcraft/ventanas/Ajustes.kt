package com.example.fitcraft.ventanas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.conexiones.ConexionPersona
import com.example.fitcraft.*
import com.example.fitcraft.clases.Usuario
import com.example.fitcraft.ui.theme.ColorError
import com.example.fitcraft.ui.theme.ColorFondo
import com.example.fitcraft.ui.theme.ColorFondoSecundario
import com.example.fitcraft.ui.theme.ColorTitulo

@Composable
fun Ajustes(navController: NavController, datosPersistentes: DatosPersistentes) {
    val conexionPersona = remember { ConexionPersona() } // Instancia de conexión para manejar usuarios
    val context = LocalContext.current

    // Inicializamos las variables con el usuario actual
    val usuarioActual = datosPersistentes.usuarioActual
    var nombreUsuario by remember { mutableStateOf(usuarioActual?.nombreUsuario ?: "") }
    var auxAltura by remember { mutableStateOf(usuarioActual?.altura?.toString() ?: "") }
    var auxPeso by remember { mutableStateOf(usuarioActual?.peso?.toString() ?: "") }

    LaunchedEffect(usuarioActual) {
        // Si hay un usuario logueado, cargar sus datos
        usuarioActual?.let { usuario ->
            conexionPersona.obtenerUsuarioPorNombre(usuario.nombreUsuario) { persona ->
                persona?.let {
                    auxAltura = it.altura.toString()
                    auxPeso = it.peso.toString()
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
            TextoCentrado("Ajustes", color = ColorTitulo)

            Column(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(ColorFondoSecundario)
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    CampoTexto(
                        value = nombreUsuario,
                        onValueChange = { nombreUsuario = it },
                        placeholder = "Nombre",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    CampoTexto(
                        value = auxPeso,
                        onValueChange = { auxPeso = it },
                        placeholder = "Peso",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    CampoTexto(
                        value = auxAltura,
                        onValueChange = { auxAltura = it },
                        placeholder = "Altura",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ClickableRow(
                    text = "Cerrar sesión",
                    color = ColorError,
                    onClick = {
                        datosPersistentes.usuarioActual = null // Limpiar usuario actual
                        navController.navigate("VentanaIniciarSesion")
                    }
                )

                ClickableRow(
                    text = "Borrar cuenta",
                    color = ColorError,
                    onClick = {
                        usuarioActual?.let { usuario ->
                            conexionPersona.borrarUsuarioPorNombre(usuario.nombreUsuario) { exito ->
                                if (exito) {
                                    Toast.makeText(context, "Cuenta eliminada", Toast.LENGTH_SHORT)
                                        .show()
                                    datosPersistentes.usuarioActual = null
                                    navController.navigate("VentanaIniciarSesion")
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

                ClickableRow(
                    text = "Borrar otra cuenta",
                    color = ColorError,
                    onClick = {
                        navController.navigate("VentanaBorrarCuentas")
                    }
                )
            }
        }

        PanelNavegacionInferior(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}

@Preview
@Composable
private fun AjustesPreview() {
    val navController = rememberNavController()
    val datosPersistentes = DatosPersistentes().apply {
        usuarioActual = Usuario(
            nombreUsuario = "usuarioPrueba",
            altura = 170.0,
            peso = 70.0
        )
    }
    Ajustes(navController = navController, datosPersistentes = datosPersistentes)
}
