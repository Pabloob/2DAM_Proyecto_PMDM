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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.fitcraft.ui.theme.ColorFondo
import com.example.fitcraft.ui.theme.ColorFondoSecundario
import com.example.fitcraft.ui.theme.ColorTitulo
import com.example.fitcraft.ui.theme.esquina50
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val inicioSesionFirebase = remember { InicioSesionFirebase(context) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        inicioSesionFirebase.verificarSesionActiva()
            .onEach { response ->
                when (response) {
                    is InicioSesionFirebase.AuthResponse.Success -> {
                        ConexionPersona().obtenerUsuarioPorUID(response.uid) { usuario ->
                            if (usuario != null) {
                                usuarioLogeado.usuarioActual = usuario
                                navController.navigate("VentanaInicio") {
                                    popUpTo(0) { inclusive = true }
                                }
                            } else {
                                cargando = false
                            }
                        }
                    }

                    is InicioSesionFirebase.AuthResponse.Error -> {
                        cargando = false
                    }
                }
            }
            .launchIn(coroutineScope)
    }


    if (cargando) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorFondo),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(esquina50)
                    .background(ColorFondoSecundario)
                    .padding(30.dp)
            ) {
                TextoCentrado(
                    text = stringResource(id = R.string.app_name),
                    style = TextStyle(
                        fontSize = 75.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier,
                    color = ColorTitulo
                )

                Spacer(modifier = Modifier.height(16.dp))

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

                if (errorMensaje.isNotEmpty()) {
                    Text(
                        text = errorMensaje,
                        color = ColorError,
                        style = TextStyle(fontSize = 14.sp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Boton(
                    text = "Iniciar Sesión",
                    onClick = {
                        cargando = true
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
                                                cargando = false
                                            }
                                        }
                                    }

                                    is InicioSesionFirebase.AuthResponse.Error -> {
                                        errorMensaje = response.messaje
                                        cargando = false
                                    }
                                }
                            }
                            .launchIn(coroutineScope)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextoInteractivo(
                    texto = "Crear cuenta",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = { navController.navigate("VentanaRegistrar") }
                )
            }
        }
    }
}
