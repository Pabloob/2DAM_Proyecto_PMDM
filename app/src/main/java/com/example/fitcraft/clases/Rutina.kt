package com.example.fitcraft.clases

data class Rutina(
    val idPersona: Int = 0,
    val idRutina: Int = 0,
    val descripcion: String = "",
    val dias: List<String> = emptyList(),
    val ejercicios: List<Ejercicio> = emptyList(),
    val horaInicio: String = "",
    val horaFin: String = "",
    val nombreRutina: String = ""
)