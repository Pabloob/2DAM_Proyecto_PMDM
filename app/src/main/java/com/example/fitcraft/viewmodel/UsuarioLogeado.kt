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

class UsuarioLogeado : ViewModel() {
    var usuarioActual by mutableStateOf<Usuario?>(null)
    var rutinaDelDia = mutableStateOf<Rutina?>(null)
    var rutinasUsuario = mutableStateOf<List<Rutina>>(emptyList())
    var lineData = mutableStateOf<LineData?>(null)
    var cargando = mutableStateOf(true)

    private val conexionRutina = ConexionRutina()

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

    fun cerrarSesion() {
        usuarioActual = null
        rutinaDelDia.value = null
        rutinasUsuario.value = emptyList()
        lineData.value = null
    }
}
