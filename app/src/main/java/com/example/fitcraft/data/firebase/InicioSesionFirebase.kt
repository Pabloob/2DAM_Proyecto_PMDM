package com.example.fitcraft.data.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InicioSesionFirebase{
    private val auth = Firebase.auth

    fun loginWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    if (currentUser != null) {
                        trySend(AuthResponse.Success(currentUser.uid))
                    } else {
                        trySend(AuthResponse.Error(messaje = "No se pudo autenticar al usuario."))
                    }
                    close()
                } else {
                    trySend(
                        AuthResponse.Error(
                            messaje = task.exception?.message ?: "Error desconocido"
                        )
                    )
                    close()
                }
            }
        awaitClose()
    }

    interface AuthResponse {
        data class Success(val uid: String) : AuthResponse
        data class Error(val messaje: String) : AuthResponse
    }

}