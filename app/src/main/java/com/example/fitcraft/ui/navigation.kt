package com.example.fitcraft.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fitcraft.ui.screen.Ajustes
import com.example.fitcraft.ui.screen.CrearRutina
import com.example.fitcraft.ui.screen.IniciarSesion
import com.example.fitcraft.ui.screen.Inicio
import com.example.fitcraft.ui.screen.Misrutinas
import com.example.fitcraft.ui.screen.MostrarEjercicios
import com.example.fitcraft.ui.screen.Registrar
import com.example.fitcraft.viewmodel.DatosRutina
import com.example.fitcraft.viewmodel.UsuarioLogeado
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavegadorVentanas(
    navHostController: NavHostController,
    usuarioLogeado: UsuarioLogeado,
    datosRutina: DatosRutina,
) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    // Verificar si hay un usuario autenticado
    val startDestination = if (currentUser != null) {
        // Si hay un usuario autenticado, ir a la pantalla de inicio
        "VentanaInicio"
    } else {
        // Si no hay un usuario autenticado, ir a la pantalla de inicio de sesión
        "VentanaIniciarSesion"
    }

    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable("VentanaIniciarSesion") {
            IniciarSesion(navHostController, usuarioLogeado)
        }
        composable("VentanaRegistrar") {
            Registrar(navHostController)
        }
        composable("VentanaInicio") {
            Inicio(navHostController, usuarioLogeado)
        }
        composable("VentanaEjercicios") {
            MostrarEjercicios(navHostController, datosRutina)
        }
        composable("VentanaCrearRutina") {
            CrearRutina(navHostController, usuarioLogeado, datosRutina)
        }
        composable("VentanaMisRutinas") {
            Misrutinas(navHostController, usuarioLogeado)
        }
        composable("VentanaAjustes") {
            Ajustes(navHostController, usuarioLogeado)
        }
    }
}
