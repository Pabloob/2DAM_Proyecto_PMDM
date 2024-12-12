package com.example.fitcraft.data.model

data class Usuario(
    var idPersona: Int = 0,
    var nombre: String = "",
    var apellidos: String = "",
    var nombreUsuario: String = "",
    var email: String = "",
    var contrasena: String = "",
    var altura: Float = 0f,
    var peso: Float = 0f,
    var fechaNacimiento: String = ""
)
