package com.example.fitcraft.data.model

data class Rutina(
    val idPersona: Int = 0,
    val idRutina: Int = 0,
    val nombreRutina: String = "",
    val descripcion: String = "",
    val horaInicio: String = "",
    val horaFin: String = "",
    val dias: List<String> = emptyList(),
    val ejercicios: List<Ejercicio> = emptyList()
)