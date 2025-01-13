package com.example.fitcraft.data.model

data class Usuario(  // Clase que representa un usuario
    var idPersona: Int = 0,  // ID único del usuario
    var nombre: String = "",  // Nombre del usuario
    var apellidos: String = "",  // Apellidos del usuario
    var nombreUsuario: String = "",  // Nombre de usuario
    var email: String = "",  // Correo electrónico del usuario
    var contrasena: String = "",  // Contraseña del usuario
    var altura: Float = 0f,  // Altura del usuario en flotante
    var peso: Float = 0f,  // Peso del usuario en flotante
    var fechaNacimiento: String = ""  // Fecha de nacimiento del usuario
)
