package com.example.fitcraft.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fitcraft.data.model.Ejercicio

// ViewModel para manejar los datos relacionados con las rutinas de ejercicio
class DatosRutina : ViewModel() {
    var idRutina = mutableStateOf(0)             // ID único de la rutina
    var nombreRutina = mutableStateOf("")       // Nombre de la rutina
    var descripcionRutina = mutableStateOf("")  // Descripción de la rutina
    var horaInicio = mutableStateOf("")         // Hora de inicio de la rutina
    var horaFin = mutableStateOf("")            // Hora de fin de la rutina
    var dias = mutableStateListOf<String>()     // Días en los que se realiza la rutina
    val ejercicios = mutableStateListOf<Ejercicio>() // Lista de ejercicios en la rutina

    val datosEjercicio = mutableStateMapOf<Ejercicio, EjercicioValores>() // Datos específicos por ejercicio

    // Función para resetear todos los datos relacionados con la rutina
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

// Clase para definir los valores asociados a un ejercicio en la rutina
data class EjercicioValores(
    val series: String = "",       // Series del ejercicio
    val repeticiones: String = "", // Repeticiones del ejercicio
    val rir: String = ""           // RIR (Repeticiones en reserva)
)
