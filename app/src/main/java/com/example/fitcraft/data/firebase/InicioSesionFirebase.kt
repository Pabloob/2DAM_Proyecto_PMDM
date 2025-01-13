package com.example.fitcraft.data.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InicioSesionFirebase {
    private val auth = Firebase.auth  // Inicialización del objeto FirebaseAuth

    // Función para iniciar sesión con correo y contraseña
    fun loginWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->  // Escucha al completar el inicio de sesión
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        trySend(AuthResponse.Success(currentUser.uid))  // Enviar la respuesta exitosa con UID
                    } else {
                        trySend(AuthResponse.Error(messaje = "No se pudo autenticar al usuario."))  // Enviar error si no se pudo autenticar
                    }
                    close()  // Cerrar el flujo
                } else {
                    trySend(AuthResponse.Error(messaje = task.exception?.message ?: "Error desconocido"))  // Enviar error en caso de fallo
                    close()  // Cerrar el flujo
                }
            }
        awaitClose()  // Cerrar flujo si se cancela
    }

    interface AuthResponse {  // Respuesta del inicio de sesión
        data class Success(val uid: String) : AuthResponse  // Respuesta exitosa
        data class Error(val messaje: String) : AuthResponse  // Respuesta de error
    }
}
