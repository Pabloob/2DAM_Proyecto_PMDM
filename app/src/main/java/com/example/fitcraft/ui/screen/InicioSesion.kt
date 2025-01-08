package com.example.fitcraft.ui.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fitcraft.R
import com.example.fitcraft.data.firebase.ConexionPersona
import com.example.fitcraft.data.firebase.InicioSesionFirebase
import com.example.fitcraft.ui.NavegadorVentanas
import com.example.fitcraft.ui.components.Boton
import com.example.fitcraft.ui.components.CampoTexto
import com.example.fitcraft.ui.components.CampoTextoConContrasena
import com.example.fitcraft.ui.components.TextoCentrado
import com.example.fitcraft.ui.components.TextoInteractivo
import com.example.fitcraft.ui.theme.ColorError
import com.example.fitcraft.ui.theme.ColorTitulo
import com.example.fitcraft.ui.theme.modifierBox
import com.example.fitcraft.ui.theme.modifierColumna
import com.example.fitcraft.viewmodel.DatosRutina
import com.example.fitcraft.viewmodel.UsuarioLogeado
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class VentanaPrincipal : ComponentActivity() {
    private val usuarioLogeado: UsuarioLogeado by viewModels()
    private val datosRutina: DatosRutina by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavegadorVentanas(
                navController,
                usuarioLogeado,
                datosRutina
            )
        }
    }
}

@Composable
fun IniciarSesion(navController: NavController, usuarioLogeado: UsuarioLogeado) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var errorMensaje by rememberSaveable { mutableStateOf("") }

    val inicioSesionFirebase = remember { InicioSesionFirebase() }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifierBox,
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifierColumna
                .verticalScroll(rememberScrollState()),
        ) {
            TextoCentrado(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(
                    fontSize = 75.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = ColorTitulo
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para el email
            CampoTexto(
                value = email,
                onValueChange = {
                    email = it
                    errorMensaje = ""
                },
                placeholder = "E-mail",
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Email, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para la contraseña
            CampoTextoConContrasena(
                value = password,
                onValueChange = {
                    password = it
                    errorMensaje = ""
                },
                placeholder = "Contraseña",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mensaje de error
            if (errorMensaje.isNotEmpty()) {
                Text(
                    text = errorMensaje,
                    color = ColorError,
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de inicio de sesión
            Boton(
                text = "Iniciar Sesión",
                onClick = {
                    inicioSesionFirebase.loginWithEmail(email, password)
                        .onEach { response ->
                            when (response) {
                                is InicioSesionFirebase.AuthResponse.Success -> {
                                    val uid = response.uid
                                    ConexionPersona().obtenerUsuarioPorUID(uid) { usuario ->
                                        if (usuario != null) {
                                            usuarioLogeado.usuarioActual = usuario
                                            navController.navigate("VentanaInicio") {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        } else {
                                            errorMensaje = "Error al cargar datos del usuario."
                                        }
                                    }
                                }

                                is InicioSesionFirebase.AuthResponse.Error -> {
                                    errorMensaje = response.messaje
                                }
                            }
                        }
                        .launchIn(coroutineScope)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texto interactivo para registrarse
            TextoInteractivo(
                texto = "Crear cuenta",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { navController.navigate("VentanaRegistrar") }
            )
        }
    }
}
