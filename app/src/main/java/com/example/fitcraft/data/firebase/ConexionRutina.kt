package com.example.fitcraft.data.firebase

import android.annotation.SuppressLint
import com.example.fitcraft.data.model.Rutina
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class ConexionRutina {
    private val rutinasRef = FirebaseDatabase.getInstance().getReference("rutinas")

    // Cargar todas las rutinas de un usuario
    fun cargarTodasLasRutinas(idPersona: Int, callback: (List<Rutina>) -> Unit) {
        rutinasRef.child(idPersona.toString()).get().addOnSuccessListener { snapshot ->
            val rutinas = snapshot.children.mapNotNull { child ->
                child.getValue(Rutina::class.java)
            }
            callback(rutinas)
        }.addOnFailureListener {
            it.printStackTrace()
            callback(emptyList())
        }
    }

    // Cargar rutina por día
    @SuppressLint("NewApi")
    fun cargarRutinasPorDia(idPersona: Int, callback: (List<Rutina>) -> Unit) {
        val diaActual = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es")).lowercase()
        rutinasRef.child(idPersona.toString()).get()
            .addOnSuccessListener { snapshot ->
                val rutinas = snapshot.children.mapNotNull { child ->
                    val rutina = child.getValue(Rutina::class.java)
                    rutina?.takeIf { it.dias.any { dia -> dia.lowercase() == diaActual } }
                }
                callback(rutinas)
            }
            .addOnFailureListener {
                it.printStackTrace()
                callback(emptyList())
            }
    }

    // Agregar una rutina para un usuario
    fun agregarRutina(idPersona: Int, rutina: Rutina, callback: (Boolean) -> Unit) {
        val rutinaId = rutina.idRutina.toString() // Usar idRutina como clave
        rutinasRef.child(idPersona.toString()).child(rutinaId).setValue(rutina)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }.addOnFailureListener {
                it.printStackTrace()
                callback(false)
            }
    }

    // Borrar una rutina por ID
    fun borrarRutina(idPersona: Int, rutinaId: String, callback: (Boolean) -> Unit) {
        rutinasRef.child(idPersona.toString()).child(rutinaId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true) // Rutina eliminada con éxito
                } else {
                    callback(false) // Error durante la eliminación
                }
            }.addOnFailureListener {
                it.printStackTrace()
                callback(false) // Error durante la eliminación
            }
    }



    // Actualizar una rutina existente
    fun actualizarRutina(idPersona: Int, rutina: Rutina, callback: (Boolean) -> Unit) {
        val rutinaId = rutina.idRutina.toString()
        rutinasRef.child(idPersona.toString()).child(rutinaId).setValue(rutina)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }.addOnFailureListener {
                it.printStackTrace()
                callback(false)
            }
    }

}
