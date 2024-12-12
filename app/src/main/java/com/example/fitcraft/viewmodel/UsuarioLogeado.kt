package com.example.fitcraft.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fitcraft.data.model.Usuario

class UsuarioLogeado : ViewModel() {
    var usuarioActual by mutableStateOf<Usuario?>(null)

    fun cerrarSesion() {
        usuarioActual = null
    }

}
