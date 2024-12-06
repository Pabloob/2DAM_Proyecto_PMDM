package com.example.fitcraft.clases

data class Usuario(
    val idPersona:Int=0,
    val nombre: String = "",
    val apellidos: String = "",
    val fechaNacimiento: String = "",
    val altura: Double = 0.0,
    val peso: Double = 0.0,
    val nombreUsuario: String = "",
    val contrasena: String = ""
)