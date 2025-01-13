package com.example.fitcraft.data.model

data class Rutina(  // Clase que representa una rutina de usuario
    val idPersona: Int = 0,  // ID único de la persona asociada a la rutina
    val idRutina: Int = 0,  // ID único de la rutina
    val nombreRutina: String = "",  // Nombre de la rutina
    val descripcion: String = "",  // Descripción de la rutina
    val horaInicio: String = "",  // Hora de inicio de la rutina
    val horaFin: String = "",  // Hora de finalización de la rutina
    val dias: List<String> = emptyList(),  // Días en los que se realiza la rutina
    val ejercicios: List<Ejercicio> = emptyList()  // Lista de ejercicios asociados a la rutina
)
