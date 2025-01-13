package com.example.fitcraft.data.firebase

import com.example.fitcraft.data.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ConexionPersona {  // Clase para gestionar la conexión con los usuarios en Firebase
    private val database = FirebaseDatabase.getInstance()  // Instancia de la base de datos de Firebase
    private val usuariosRef = database.getReference("usuarios")  // Referencia a la sección de usuarios en Firebase
    private val firebaseAuth = FirebaseAuth.getInstance()  // Instancia de autenticación de Firebase

    companion object {
        private const val TAG = "ConexionPersona"  // Etiqueta para el log de la clase
    }

    // Borrar un usuario por su idPersona
    fun borrarUsuarioPorId(usuario: Usuario, callback: (Boolean) -> Unit) {
        val usuarioId = usuario.idPersona.toString()

        usuariosRef.child(usuarioId).removeValue().addOnCompleteListener { dbTask ->  // Elimina el usuario de la base de datos
            if (dbTask.isSuccessful) {
                firebaseAuth.signInWithEmailAndPassword(usuario.email, usuario.contrasena)  // Inicia sesión con los datos de usuario
                    .addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            firebaseAuth.currentUser?.delete()  // Elimina el usuario autenticado
                                ?.addOnCompleteListener { deleteTask ->
                                    callback(deleteTask.isSuccessful)  // Devuelve el resultado del borrado
                                } ?: callback(false)  // Si no se puede eliminar, devuelve falso
                        } else {
                            callback(false)  // Si no se pudo iniciar sesión, devuelve falso
                        }
                    }.addOnFailureListener {
                        callback(false)  // Maneja posibles errores en la autenticación
                    }
            } else {
                callback(false)  // Si la eliminación falló, devuelve falso
            }
        }.addOnFailureListener {
            callback(false)  // Maneja errores en la eliminación del usuario
        }
    }

    // Registrar un nuevo usuario
    fun registrarUsuario(usuario: Usuario, callback: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(usuario.email, usuario.contrasena)  // Crea un nuevo usuario en Firebase
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = firebaseAuth.currentUser?.uid ?: return@addOnCompleteListener  // Obtiene el UID del usuario
                    usuariosRef.child(uid).setValue(usuario).addOnCompleteListener { dbTask ->  // Guarda el usuario en la base de datos
                        if (dbTask.isSuccessful) {
                            callback(true, "Usuario registrado correctamente")  // Devuelve éxito y mensaje
                        } else {
                            callback(false, "Error al guardar usuario en la base de datos")  // Si falla, mensaje de error
                        }
                    }
                } else {
                    callback(false, task.exception?.localizedMessage ?: "Error al crear cuenta")  // Si falla, mensaje de error
                }
            }.addOnFailureListener {
                callback(false, it.localizedMessage)  // Maneja errores en el proceso de registro
            }
    }

    // Obtener un usuario por UID
    fun obtenerUsuarioPorUID(uid: String, callback: (Usuario?) -> Unit) {
        usuariosRef.child(uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val snapshot = task.result
                if (snapshot.exists()) {
                    val usuario = snapshot.getValue(Usuario::class.java)
                    callback(usuario)  // Devuelve el usuario obtenido
                } else {
                    callback(null)  // Si no existe el usuario, devuelve null
                }
            } else {
                callback(null)  // Maneja errores en la obtención del usuario
            }
        }
    }

    // Actualizar datos de un usuario
    fun actualizarUsuario(usuario: Usuario, callback: (Boolean) -> Unit) {
        val usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios")  // Referencia para actualizar usuarios

        usuariosRef.child(usuario.idPersona.toString()).setValue(usuario)  // Actualiza la información del usuario en la base de datos
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)  // Si la actualización es exitosa, devuelve verdadero
                } else {
                    callback(false)  // Si falla la actualización, devuelve falso
                }
            }
    }

    // Validar registro de un usuario
    fun validarRegistro(  // Verifica que todos los campos del registro sean válidos
        nombre: String,
        apellidos: String,
        email: String,
        fechaNacimiento: String,
        altura: Float,
        peso: Float,
        nombreUsuario: String,
        contrasena: String
    ): Boolean {
        return nombre.isNotEmpty() && apellidos.isNotEmpty() &&  // Verifica campos no vacíos
                fechaNacimiento.isNotEmpty() && email.isNotEmpty() && altura > 0 &&  // Verifica fechas válidas
                peso > 0 && nombreUsuario.isNotEmpty() && contrasena.isNotEmpty()  // Verifica datos válidos
    }
}
