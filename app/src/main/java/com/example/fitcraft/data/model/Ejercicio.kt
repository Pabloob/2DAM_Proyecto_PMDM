package com.example.fitcraft.data.model

data class Ejercicio(
    val descripcion: String = "",
    val nombreEjercicio: String = "",
    var repeticiones: Int = 0,
    var series: Int = 0,
    var rir: Int = 0,
    val tipoEjercicio: String = ""
)


