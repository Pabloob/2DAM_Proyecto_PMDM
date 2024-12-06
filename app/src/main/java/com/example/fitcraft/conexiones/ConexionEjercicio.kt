package com.example.fitcraft.conexiones

import com.example.fitcraft.clases.Ejercicio
import com.google.firebase.database.FirebaseDatabase

class ConexionEjercicio {
    private val ejerciciosRef = FirebaseDatabase.getInstance().getReference("ejercicios")

    // Cargar todos los ejercicios
    fun cargarEjercicios(callback: (List<Ejercicio>) -> Unit) {
        ejerciciosRef.get()
            .addOnSuccessListener { snapshot ->
                val ejercicios = snapshot.children.mapNotNull { it.getValue(Ejercicio::class.java) }
                callback(ejercicios)
            }.addOnFailureListener {
                it.printStackTrace()
                callback(emptyList())
            }
    }

    // Agregar un ejercicio a la raíz (opcional)
    fun agregarEjercicio(ejercicio: Ejercicio, callback: (Boolean) -> Unit) {
        val ejercicioId = ejerciciosRef.push().key
        if (ejercicioId != null) {
            ejerciciosRef.child(ejercicioId).setValue(ejercicio)
                .addOnCompleteListener { task ->
                    callback(task.isSuccessful)
                }.addOnFailureListener {
                    it.printStackTrace()
                    callback(false)
                }
        } else {
            callback(false)
        }
    }

    // Borrar un ejercicio de la raíz (opcional)
    fun borrarEjercicio(ejercicioId: String, callback: (Boolean) -> Unit) {
        ejerciciosRef.child(ejercicioId).removeValue()
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }.addOnFailureListener {
                it.printStackTrace()
                callback(false)
            }
    }
}

