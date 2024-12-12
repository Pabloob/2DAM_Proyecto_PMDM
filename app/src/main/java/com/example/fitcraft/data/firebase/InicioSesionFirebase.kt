package com.example.fitcraft.data.firebase

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.fitcraft.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID

class InicioSesionFirebase(val context: Context) {
    private val auth = Firebase.auth

//    fun createAccountWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
//
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    trySend(AuthResponse.Success)
//                    close()
//                } else {
//                    trySend(AuthResponse.Error(messaje = task.exception?.message ?: ""))
//                    close()
//                }
//            }
//        awaitClose()
//    }

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it ->
            str + "%02x".format(it)
        }
    }

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

    fun signInWithGoogle(): Flow<AuthResponse> = callbackFlow {
        try {
            val credentialManager = CredentialManager.create(context)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.web_client_id))
                .setAutoSelectEnabled(false)
                .setNonce(createNonce())
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseCredential =
                    GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                auth.signInWithCredential(firebaseCredential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        if (currentUser != null) {
                            trySend(AuthResponse.Success(currentUser.uid))
                        } else {
                            trySend(AuthResponse.Error(messaje = "No se pudo autenticar al usuario."))
                        }
                    } else {
                        trySend(
                            AuthResponse.Error(
                                messaje = task.exception?.message
                                    ?: "Error al autenticar con Google"
                            )
                        )
                    }
                    close()
                }
            }
        } catch (e: Exception) {
            trySend(AuthResponse.Error(messaje = e.message ?: "Excepción desconocida"))
            close()
        }
        awaitClose()
    }

    fun verificarSesionActiva(): Flow<AuthResponse> = callbackFlow {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Si hay un usuario autenticado, enviamos el éxito con su UID
            trySend(AuthResponse.Success(currentUser.uid))
        } else {
            // Si no hay usuario autenticado, enviamos un error
            trySend(AuthResponse.Error(messaje = "No hay sesión activa"))
        }
        close()
    }


    interface AuthResponse {
        data class Success(val uid: String) : AuthResponse
        data class Error(val messaje: String) : AuthResponse
    }


}