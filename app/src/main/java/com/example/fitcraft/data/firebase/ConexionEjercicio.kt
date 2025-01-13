package com.example.fitcraft.data.firebase

import com.example.fitcraft.data.model.Ejercicio
import com.google.firebase.database.FirebaseDatabase

class ConexionEjercicio {  // Clase para gestionar la conexión con los ejercicios en Firebase
    private val ejerciciosRef = FirebaseDatabase.getInstance().getReference("ejercicios")  // Referencia a la base de datos de ejercicios

    // Método para cargar todos los ejercicios desde Firebase
    fun cargarEjercicios(callback: (List<Ejercicio>) -> Unit) {
        ejerciciosRef.get()  // Obtiene los datos de Firebase
            .addOnSuccessListener { snapshot ->  // Éxito en la obtención de los datos
                if (snapshot.exists()) {  // Si hay datos disponibles
                    val ejercicios =
                        snapshot.children.mapNotNull { it.getValue(Ejercicio::class.java) }  // Mapea los datos a objetos Ejercicio
                    callback(ejercicios)  // Devuelve los ejercicios al callback
                } else {
                    callback(emptyList())  // Si no hay datos, devuelve una lista vacía
                }
            }.addOnFailureListener {  // Manejo de errores
                it.printStackTrace()  // Imprime el error
                callback(emptyList())  // Devuelve una lista vacía en caso de error
            }
    }
}
