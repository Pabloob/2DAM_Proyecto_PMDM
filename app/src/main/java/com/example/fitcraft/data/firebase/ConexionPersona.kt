package com.example.fitcraft.data.firebase

import android.util.Log
import com.example.fitcraft.data.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ConexionPersona {

    private val database = FirebaseDatabase.getInstance()
    private val usuariosRef = database.getReference("usuarios")
    private val firebaseAuth = FirebaseAuth.getInstance()

    companion object {
        private const val TAG = "ConexionPersona"
    }

    // Borrar un usuario por idPersona
    fun borrarUsuarioPorId(usuario: Usuario, callback: (Boolean) -> Unit) {
        val usuarioId = usuario.idPersona.toString()

        usuariosRef.child(usuarioId).removeValue().addOnCompleteListener { dbTask ->
            if (dbTask.isSuccessful) {
                firebaseAuth.signInWithEmailAndPassword(usuario.email, usuario.contrasena)
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            firebaseAuth.currentUser?.delete()
                                ?.addOnCompleteListener { deleteTask ->
                                    callback(deleteTask.isSuccessful)
                                } ?: callback(false)
                        } else {
                            callback(false)
                        }
                    }.addOnFailureListener {
                        callback(false)
                    }
            } else {
                callback(false)
            }
        }.addOnFailureListener {
            callback(false)
        }
    }

    // Registrar usuario
    fun registrarUsuario(usuario: Usuario, callback: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(usuario.email, usuario.contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener
                    usuariosRef.child(uid).setValue(usuario).addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            callback(true, "Usuario registrado correctamente")
                        } else {
                            callback(false, "Error al guardar usuario en la base de datos")
                        }
                    }
                } else {
                    callback(false, task.exception?.localizedMessage ?: "Error al crear cuenta")
                }
            }.addOnFailureListener {
                callback(false, it.localizedMessage)
            }
    }

    fun obtenerUsuarioPorUID(uid: String, callback: (Usuario?) -> Unit) {
        usuariosRef.child(uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot.exists()) {
                    val usuario = snapshot.getValue(Usuario::class.java)
                    callback(usuario)
                } else {
                    callback(null)
                }
            } else {
                callback(null)
            }
        }
    }

    fun actualizarUsuario(usuario: Usuario, callback: (Boolean) -> Unit) {
        val usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios")

        usuariosRef.child(usuario.idPersona.toString()).setValue(usuario)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true) // Actualización exitosa
                } else {
                    callback(false) // Error durante la actualización
                }
            }
    }


    fun validarRegistro(
        nombre: String,
        apellidos: String,
        email: String,
        fechaNacimiento: String,
        altura: Float,
        peso: Float,
        nombreUsuario: String,
        contrasena: String
    ): Boolean {
        return nombre.isNotEmpty() && apellidos.isNotEmpty() &&
                fechaNacimiento.isNotEmpty() && email.isNotEmpty() && altura > 0 &&
                peso > 0 && nombreUsuario.isNotEmpty() &&
                contrasena.isNotEmpty()
    }
}