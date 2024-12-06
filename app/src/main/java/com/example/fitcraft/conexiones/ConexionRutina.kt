package com.example.fitcraft.conexiones

import android.annotation.SuppressLint
import com.example.fitcraft.clases.Rutina
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale


class ConexionRutina {
    private val rutinasRef = FirebaseDatabase.getInstance().getReference("rutinas")

    // Cargar todas las rutinas de un usuario
    fun cargarTodasLasRutinas(usuarioId: String, callback: (List<Rutina>) -> Unit) {
        rutinasRef.child(usuarioId).get().addOnSuccessListener { snapshot ->
            val rutinas = snapshot.children.mapNotNull { it.getValue(Rutina::class.java) }
            callback(rutinas)
        }.addOnFailureListener {
            it.printStackTrace()
            callback(emptyList())
        }
    }

    // Cargar rutina por día
    @SuppressLint("NewApi")
    fun cargarRutinasPorDia(usuarioId: String, callback: (List<Rutina>) -> Unit) {
        val diaActual = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es", "ES")).lowercase()
        rutinasRef.child(usuarioId).get().addOnSuccessListener { snapshot ->
            val rutinas = snapshot.children.mapNotNull { it.getValue(Rutina::class.java) }
                .filter { rutina -> rutina.dias.contains(diaActual) }
            callback(rutinas)
        }.addOnFailureListener {
            it.printStackTrace()
            callback(emptyList())
        }
    }

    // Agregar una rutina para un usuario
    fun agregarRutina(usuarioId: String, rutina: Rutina, callback: (Boolean) -> Unit) {
        val rutinaId = rutinasRef.child(usuarioId).push().key
        if (rutinaId != null) {
            rutinasRef.child(usuarioId).child(rutinaId).setValue(rutina).addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }.addOnFailureListener {
                it.printStackTrace()
                callback(false)
            }
        } else {
            callback(false)
        }
    }

    // Borrar una rutina por ID
    fun borrarRutina(usuarioId: String, rutinaId: String, callback: (Boolean) -> Unit) {
        rutinasRef.child(usuarioId).child(rutinaId).removeValue().addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }.addOnFailureListener {
            it.printStackTrace()
            callback(false)
        }
    }
}
