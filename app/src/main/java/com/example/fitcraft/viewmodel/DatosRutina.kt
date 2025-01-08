package com.example.fitcraft.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fitcraft.data.model.Ejercicio

class DatosRutina : ViewModel() {
    var idRutina = mutableStateOf(0)
    var nombreRutina = mutableStateOf("")
    var descripcionRutina = mutableStateOf("")
    var horaInicio = mutableStateOf("")
    var horaFin = mutableStateOf("")
    var dias = mutableStateListOf<String>()
    val ejercicios = mutableStateListOf<Ejercicio>()

    val datosEjercicio = mutableStateMapOf<Ejercicio, EjercicioValores>()

    fun resetearDatos() {
        idRutina.value = 0
        nombreRutina.value = ""
        descripcionRutina.value = ""
        horaInicio.value = ""
        horaFin.value = ""
        ejercicios.clear()
        dias.clear()
        datosEjercicio.clear()
    }
}

data class EjercicioValores(
    val series: String = "",
    val repeticiones: String = "",
    val rir: String = ""
)
