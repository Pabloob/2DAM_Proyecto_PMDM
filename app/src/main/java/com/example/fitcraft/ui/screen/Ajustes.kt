package com.example.fitcraft.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.data.firebase.ConexionPersona
import com.example.fitcraft.data.model.Usuario
import com.example.fitcraft.ui.components.CampoTexto
import com.example.fitcraft.ui.components.ClickableRow
import com.example.fitcraft.ui.components.PanelNavegacionInferior
import com.example.fitcraft.ui.components.TextoCentrado
import com.example.fitcraft.ui.theme.ColorError
import com.example.fitcraft.ui.theme.ColorFondo
import com.example.fitcraft.ui.theme.ColorFondoSecundario
import com.example.fitcraft.ui.theme.ColorTitulo
import com.example.fitcraft.viewmodel.UsuarioLogeado
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Ajustes(navController: NavController, usuarioLogeado: UsuarioLogeado) {
    val conexionPersona =
        remember { ConexionPersona() }
    val context = LocalContext.current

    // Inicializamos las variables con el usuario actual
    val usuarioActual = usuarioLogeado.usuarioActual
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
                        modifier = Modifier.fillMaxWidth()
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
                        modifier = Modifier.fillMaxWidth()
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ClickableRow(
                    text = "Cerrar sesión",
                    color = ColorError,
                    onClick = {
                        FirebaseAuth.getInstance().signOut() // Cierra sesión en Firebase
                        usuarioLogeado.cerrarSesion() // Limpia los datos persistentes

                        // Limpia la sesión local
                        val sharedPreferences =
                            context.getSharedPreferences("FitCraftPrefs", Context.MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            remove("uid")
                            apply()
                        }

                        // Redirige a la pantalla de inicio de sesión
                        navController.navigate("VentanaIniciarSesion") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )

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

                                    // Redirige a la pantalla de inicio de sesión
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

        PanelNavegacionInferior(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}