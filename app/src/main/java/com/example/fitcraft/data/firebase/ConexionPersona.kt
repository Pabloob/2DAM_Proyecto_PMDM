package com.example.fitcraft.data.firebase

import android.util.Log
import com.example.fitcraft.data.model.Usuario
import com.example.fitcraft.viewmodel.UsuarioLogeado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.content.Context

class ConexionPersona {

    private val database = FirebaseDatabase.getInstance()
    private val usuariosRef = database.getReference("usuarios")
    private val firebaseAuth = FirebaseAuth.getInstance()

    companion object {
        private const val TAG = "ConexionPersona"
    }

    // Obtener usuario por nombre
    fun obtenerUsuarioPorNombre(nombre: String, callback: (Usuario?) -> Unit) {
        usuariosRef.orderByChild("nombreUsuario").equalTo(nombre).get()
            .addOnSuccessListener { snapshot ->
                val usuario =
                    snapshot.children.mapNotNull { it.getValue(Usuario::class.java) }.firstOrNull()
                callback(usuario)
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Error al obtener usuario por nombre: ${exception.message}", exception)
                callback(null)
            }
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

    fun verificarSesionAutomatica(
        context: Context,
        usuarioLogeado: UsuarioLogeado,
        callback: (Boolean, String?) -> Unit
    ) {
        val sharedPreferences = context.getSharedPreferences("FitCraftPrefs", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getString("UID", null)

        if (uid != null) {
            // Cargar datos del usuario desde Firebase Realtime Database
            usuariosRef.child(uid).get()
                .addOnSuccessListener { snapshot ->
                    val usuario = snapshot.getValue(Usuario::class.java)
                    if (usuario != null) {
                        usuarioLogeado.usuarioActual = usuario
                        callback(true, null) // Éxito
                    } else {
                        callback(false, "No se encontró información del usuario.")
                    }
                }
                .addOnFailureListener {
                    callback(false, it.localizedMessage)
                }
        } else {
            callback(false, "")
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

}