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

// Objetivo de la clase Routes: Definir las rutas/nombres de las pantallas
object Routes {
    const val IniciarSesion = "VentanaIniciarSesion" // Ruta para iniciar sesión
    const val Registrar = "VentanaRegistrar"       // Ruta para registro de usuarios
    const val Inicio = "VentanaInicio"             // Ruta para la pantalla principal
    const val Ejercicios = "VentanaEjercicios"     // Ruta para mostrar ejercicios
    const val CrearRutina = "VentanaCrearRutina"   // Ruta para crear rutinas
    const val MisRutinas = "VentanaMisRutinas"     // Ruta para gestionar rutinas del usuario
    const val Ajustes =
        "VentanaAjustes"           // Ruta para la configuración o ajustes del usuario
}

// Componente principal que gestiona la navegación entre pantallas
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

    // Escucha los cambios en el estado de autenticación de Firebase
    LaunchedEffect(Unit) {
        firebaseAuth.addAuthStateListener { auth ->
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val userId = currentUser.uid
                conexionPersona.obtenerUsuarioPorUID(userId) { usuario ->
                    if (usuario != null) {
                        usuarioLogeado.usuarioActual = usuario
                    } else {
                        firebaseAuth.signOut() // Si usuario no encontrado, cerrar sesión
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

    // Mostrar indicador de carga mientras se procesa la autenticación
    if (cargando.value) {
        Box(
            modifierBox,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Determinar la pantalla inicial basándose en si está o no autenticado
        val startDestination = if (isLoggedIn.value) Routes.Inicio else Routes.IniciarSesion

        NavHost(
            navController = navHostController,
            startDestination = startDestination
        ) {
            addAuthScreens(navHostController, usuarioLogeado) // Agregar pantallas de autenticación
            addMainScreens(
                navHostController,
                usuarioLogeado,
                datosRutina
            ) // Agregar pantallas principales
        }
    }
}

// Agregar pantallas relacionadas con autenticación (Iniciar sesión y Registro)
private fun NavGraphBuilder.addAuthScreens(
    navController: NavHostController,
    usuarioLogeado: UsuarioLogeado
) {
    composable(Routes.IniciarSesion) { IniciarSesion(navController, usuarioLogeado) }
    composable(Routes.Registrar) { Registrar(navController) }
}

// Agregar pantallas principales como Inicio, Ejercicios, Crear Rutina, etc.
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
