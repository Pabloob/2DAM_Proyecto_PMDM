package com.example.fitcraft.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fitcraft.data.model.Ejercicio

class DatosRutina : ViewModel() {
    var nombreRutina = mutableStateOf("")
    var descripcionRutina = mutableStateOf("")
    var horaInicio = mutableStateOf("")
    var horaFin = mutableStateOf("")
    val ejerciciosSeleccionados = mutableListOf<Ejercicio>()
    val seriesYRepeticiones = mutableStateMapOf<Ejercicio, Pair<String, String>>()
    var diaRutina = mutableStateListOf<String>()

    fun resetearDatos() {
        nombreRutina.value = ""
        descripcionRutina.value = ""
        horaInicio.value = ""
        horaFin.value = ""
        ejerciciosSeleccionados.clear()
        seriesYRepeticiones.clear()
    }
}
