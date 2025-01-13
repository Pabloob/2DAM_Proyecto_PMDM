package com.example.fitcraft.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fitcraft.data.firebase.ConexionRutina
import com.example.fitcraft.data.model.Rutina
import com.example.fitcraft.data.model.Usuario
import com.example.fitcraft.ui.components.createLineData
import com.github.mikephil.charting.data.LineData

// ViewModel para manejar el estado del usuario logueado y sus rutinas
class UsuarioLogeado : ViewModel() {
    var usuarioActual by mutableStateOf<Usuario?>(null)             // Usuario actualmente autenticado
    var rutinaDelDia = mutableStateOf<Rutina?>(null)               // Rutina del día
    var rutinasUsuario = mutableStateOf<List<Rutina>>(emptyList()) // Lista de todas las rutinas del usuario
    var lineData = mutableStateOf<LineData?>(null)                 // Datos para gráfico de líneas
    var cargando = mutableStateOf(true)                           // Estado de carga

    private val conexionRutina = ConexionRutina()                  // Conexión a Firebase para rutinas

    // Inicializa los datos del usuario y carga rutinas y gráficos
    fun inicializarUsuario(idPersona: Int, onError: () -> Unit) {
        cargando.value = true

        // Cargar rutinas del día
        conexionRutina.cargarRutinasPorDia(idPersona) { rutinas ->
            rutinaDelDia.value = rutinas.firstOrNull()
            cargando.value = false
        }

        // Cargar todas las rutinas y preparar datos para el gráfico
        conexionRutina.cargarTodasLasRutinas(idPersona) { rutinas ->
            rutinasUsuario.value = rutinas
            lineData.value = createLineData(rutinas)
        }
    }

    // Función para cerrar sesión y limpiar datos
    fun cerrarSesion() {
        usuarioActual = null
        rutinaDelDia.value = null
        rutinasUsuario.value = emptyList()
        lineData.value = null
    }
}
