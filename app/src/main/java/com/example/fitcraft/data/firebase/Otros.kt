package com.example.fitcraft.data.firebase

import com.example.fitcraft.data.model.Rutina

fun procesarRutinasSemanal(
    rutinas: List<Rutina>
): List<Float> {
    // Inicializar tiempos por día de la semana
    val tiemposPorDia = mutableMapOf(
        "lunes" to 0f, "martes" to 0f, "miércoles" to 0f,
        "jueves" to 0f, "viernes" to 0f, "sábado" to 0f, "domingo" to 0f
    )

    for (rutina in rutinas) {
        rutina.dias.forEach { dia ->
            val inicio = convertirHoraAFloat(rutina.horaInicio)
            val fin = convertirHoraAFloat(rutina.horaFin)
            val tiempo = fin - inicio
            tiemposPorDia[dia.lowercase()] = tiemposPorDia.getOrDefault(dia.lowercase(), 0f) + tiempo
        }
    }

    // Retornar en orden de lunes a domingo
    return listOf(
        tiemposPorDia["lunes"] ?: 0f,
        tiemposPorDia["martes"] ?: 0f,
        tiemposPorDia["miércoles"] ?: 0f,
        tiemposPorDia["jueves"] ?: 0f,
        tiemposPorDia["viernes"] ?: 0f,
        tiemposPorDia["sábado"] ?: 0f,
        tiemposPorDia["domingo"] ?: 0f
    )
}

fun convertirHoraAFloat(hora: String): Float {
    val partes = hora.split(":")
    return if (partes.size == 2) {
        partes[0].toFloat() + partes[1].toFloat() / 60
    } else {
        0f
    }
}
