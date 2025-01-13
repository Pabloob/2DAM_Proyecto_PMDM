package com.example.fitcraft.data.model

data class Ejercicio(  // Clase que representa un ejercicio en una rutina
    val descripcion: String = "",  // Descripción del ejercicio
    val nombreEjercicio: String = "",  // Nombre del ejercicio
    var repeticiones: Int = 0,  // Número de repeticiones
    var series: Int = 0,  // Número de series
    var rir: Int = 0,  // Repeticiones en reserva (RIR)
    val tipoEjercicio: String = ""  // Tipo de ejercicio (ej. fuerza, cardio, etc.)
)
