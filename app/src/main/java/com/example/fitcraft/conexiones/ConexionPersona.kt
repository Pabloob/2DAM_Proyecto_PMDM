package com.example.fitcraft.conexiones

import com.example.fitcraft.clases.Usuario
import com.google.firebase.database.FirebaseDatabase

class ConexionPersona {

    private val database = FirebaseDatabase.getInstance()
    private val personasRef = database.getReference("personas")

    fun obtenerTodosLosUsuarios(callback: (List<Usuario>) -> Unit) {
        val usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios")
        usuariosRef.get().addOnSuccessListener { snapshot ->
            val usuarios = snapshot.children.mapNotNull { it.getValue(Usuario::class.java) }
            callback(usuarios)
        }.addOnFailureListener {
            it.printStackTrace()
            callback(emptyList())
        }
    }

    fun obtenerUsuarioPorNombre(nombre: String, callback: (Usuario?) -> Unit) {
        val usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios")
        usuariosRef.get().addOnSuccessListener { snapshot ->
            val usuario = snapshot.children.mapNotNull { it.getValue(Usuario::class.java) }
                .firstOrNull { it.nombreUsuario == nombre }
            callback(usuario)
        }.addOnFailureListener {
            it.printStackTrace()
            callback(null)
        }
    }

    fun agregarUsuario(usuario: Usuario, callback: (Boolean) -> Unit) {
        val usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios")
        val usuarioId = usuariosRef.push().key
        if (usuarioId != null) {
            usuariosRef.child(usuarioId).setValue(usuario).addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }.addOnFailureListener {
                it.printStackTrace()
                callback(false)
            }
        } else {
            callback(false)
        }
    }


    fun borrarUsuarioPorNombre(nombre: String, callback: (Boolean) -> Unit) {
        val usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios")
        usuariosRef.get().addOnSuccessListener { snapshot ->
            val usuarioSnapshot = snapshot.children.firstOrNull {
                it.getValue(Usuario::class.java)?.nombre == nombre
            }
            if (usuarioSnapshot != null) {
                usuarioSnapshot.ref.removeValue().addOnCompleteListener { task ->
                    callback(task.isSuccessful)
                }.addOnFailureListener {
                    it.printStackTrace()
                    callback(false)
                }
            } else {
                callback(false) // Usuario no encontrado
            }
        }.addOnFailureListener {
            it.printStackTrace()
            callback(false)
        }
    }

}
