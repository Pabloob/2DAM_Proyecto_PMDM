package com.example.fitcraft.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fitcraft.data.firebase.ConexionPersona
import com.example.fitcraft.ui.screen.Ajustes
import com.example.fitcraft.ui.screen.CrearRutina
import com.example.fitcraft.ui.screen.IniciarSesion
import com.example.fitcraft.ui.screen.Inicio
import com.example.fitcraft.ui.screen.Misrutinas
import com.example.fitcraft.ui.screen.MostrarEjercicios
import com.example.fitcraft.ui.screen.Registrar
import com.example.fitcraft.ui.theme.modifierBox
import com.example.fitcraft.viewmodel.DatosRutina
import com.example.fitcraft.viewmodel.UsuarioLogeado
import com.google.firebase.auth.FirebaseAuth

object Routes {
    const val IniciarSesion = "VentanaIniciarSesion"
    const val Registrar = "VentanaRegistrar"
    const val Inicio = "VentanaInicio"
    const val Ejercicios = "VentanaEjercicios"
    const val CrearRutina = "VentanaCrearRutina"
    const val MisRutinas = "VentanaMisRutinas"
    const val Ajustes = "VentanaAjustes"
}

@Composable
fun NavegadorVentanas(
    navHostController: NavHostController,
    usuarioLogeado: UsuarioLogeado,
    datosRutina: DatosRutina,
) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val conexionPersona = ConexionPersona()
    val isLoggedIn = remember { mutableStateOf(firebaseAuth.currentUser != null) }
    val cargando = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        firebaseAuth.addAuthStateListener { auth ->
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid
                conexionPersona.obtenerUsuarioPorUID(userId) { usuario ->
                    if (usuario != null) {
                        usuarioLogeado.usuarioActual = usuario
                    } else {
                        firebaseAuth.signOut()
                        isLoggedIn.value = false
                    }
                    cargando.value = false
                }
            } else {
                cargando.value = false
            }
            isLoggedIn.value = currentUser != null
        }
    }

    if (cargando.value) {
        Box(
            modifierBox,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val startDestination = if (isLoggedIn.value) Routes.Inicio else Routes.IniciarSesion

        NavHost(
            navController = navHostController,
            startDestination = startDestination
        ) {
            addAuthScreens(navHostController, usuarioLogeado)
            addMainScreens(navHostController, usuarioLogeado, datosRutina)
        }
    }
}

private fun NavGraphBuilder.addAuthScreens(
    navController: NavHostController,
    usuarioLogeado: UsuarioLogeado
) {
    composable(Routes.IniciarSesion) { IniciarSesion(navController, usuarioLogeado) }
    composable(Routes.Registrar) { Registrar(navController) }
}

private fun NavGraphBuilder.addMainScreens(
    navController: NavHostController,
    usuarioLogeado: UsuarioLogeado,
    datosRutina: DatosRutina
) {
    composable(Routes.Inicio) { Inicio(navController, usuarioLogeado) }
    composable(Routes.Ejercicios) { MostrarEjercicios(navController, datosRutina) }
    composable(Routes.CrearRutina) { CrearRutina(navController, usuarioLogeado, datosRutina) }
    composable(Routes.MisRutinas) { Misrutinas(navController, usuarioLogeado) }
    composable(Routes.Ajustes) { Ajustes(navController, usuarioLogeado) }
}
