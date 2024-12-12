package com.example.fitcraft.data.firebase

import com.example.fitcraft.data.model.Ejercicio
import com.google.firebase.database.FirebaseDatabase

class ConexionEjercicio {
    private val ejerciciosRef = FirebaseDatabase.getInstance().getReference("ejercicios")

    // Cargar todos los ejercicios
    fun cargarEjercicios(callback: (List<Ejercicio>) -> Unit) {
        ejerciciosRef.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val ejercicios =
                        snapshot.children.mapNotNull { it.getValue(Ejercicio::class.java) }
                    callback(ejercicios)
                } else {
                    callback(emptyList())
                }
            }.addOnFailureListener {
                it.printStackTrace()
                callback(emptyList())
            }
    }
}

