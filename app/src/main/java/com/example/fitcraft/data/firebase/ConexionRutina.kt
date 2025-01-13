package com.example.fitcraft.data.firebase

import android.annotation.SuppressLint
import com.example.fitcraft.data.model.Rutina
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class ConexionRutina {  // Clase para gestionar las rutinas en Firebase
    private val rutinasRef = FirebaseDatabase.getInstance().getReference("rutinas")  // Referencia a la sección de rutinas en Firebase

    // Cargar todas las rutinas de un usuario específico
    fun cargarTodasLasRutinas(idPersona: Int, callback: (List<Rutina>) -> Unit) {
        rutinasRef.child(idPersona.toString()).get().addOnSuccessListener { snapshot ->  // Obtiene las rutinas del usuario especificado
            val rutinas = snapshot.children.mapNotNull { child ->
                child.getValue(Rutina::class.java)
            }
            callback(rutinas)  // Devuelve las rutinas
        }.addOnFailureListener {
            it.printStackTrace()
            callback(emptyList())  // Maneja errores y devuelve una lista vacía
        }
    }

    // Cargar rutinas por día actual
    @SuppressLint("NewApi")
    fun cargarRutinasPorDia(idPersona: Int, callback: (List<Rutina>) -> Unit) {
        val diaActual = LocalDate.now().dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es")).lowercase()  // Obtiene el día actual en español
        rutinasRef.child(idPersona.toString()).get()
            .addOnSuccessListener { snapshot ->
                val rutinas = snapshot.children.mapNotNull { child ->
                    val rutina = child.getValue(Rutina::class.java)
                    rutina?.takeIf { it.dias.any { dia -> dia.lowercase() == diaActual } }
                }
                callback(rutinas)  // Devuelve las rutinas que coinciden con el día actual
            }
            .addOnFailureListener {
                it.printStackTrace()
                callback(emptyList())  // Maneja errores y devuelve una lista vacía
            }
    }

    // Agregar una rutina para un usuario específico
    fun agregarRutina(idPersona: Int, rutina: Rutina, callback: (Boolean) -> Unit) {
        val rutinaId = rutina.idRutina.toString()  // Usa idRutina como clave para almacenar la rutina
        rutinasRef.child(idPersona.toString()).child(rutinaId).setValue(rutina)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)  // Devuelve si la operación fue exitosa
            }.addOnFailureListener {
                it.printStackTrace()
                callback(false)  // Maneja errores y devuelve falso si falla
            }
    }

    // Borrar una rutina por su ID
    fun borrarRutina(idPersona: Int, rutinaId: String, callback: (Boolean) -> Unit) {
        rutinasRef.child(idPersona.toString()).child(rutinaId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)  // Rutina eliminada con éxito
                } else {
                    callback(false)  // Error durante la eliminación
                }
            }.addOnFailureListener {
                it.printStackTrace()
                callback(false)  // Maneja errores y devuelve falso si falla
            }
    }

    // Actualizar una rutina existente
    fun actualizarRutina(idPersona: Int, rutina: Rutina, callback: (Boolean) -> Unit) {
        val rutinaId = rutina.idRutina.toString()
        rutinasRef.child(idPersona.toString()).child(rutinaId).setValue(rutina)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)  // Devuelve si la operación fue exitosa
            }.addOnFailureListener {
                it.printStackTrace()
                callback(false)  // Maneja errores y devuelve falso si falla
            }
    }
}
