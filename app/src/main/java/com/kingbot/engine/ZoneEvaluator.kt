package com.kingbot.engine

import android.util.Log

class ZoneEvaluator {

    companion object {
        const val TAG = "KingBotZone"
    }

    private val blockedZones = mutableSetOf<String>()
    private val preferredZones = mutableSetOf<String>()

    /**
     * Agrega o actualiza las listas de zonas bloqueadas y preferidas.
     */
    fun updateZoneLists(blocked: List<String>, preferred: List<String>) {
        blockedZones.clear()
        blockedZones.addAll(blocked.map { it.lowercase().trim() })

        preferredZones.clear()
        preferredZones.addAll(preferred.map { it.lowercase().trim() })
    }

    /**
     * Evalúa si una zona de destino está permitida.
     * Retorna false si la zona está en la lista negra (bloqueada).
     */
    fun isZoneAllowed(destinationName: String): Boolean {
        val destination = destinationName.lowercase().trim()

        // Si la zona está explícitamente bloqueada, se rechaza de inmediato
        for (blocked in blockedZones) {
            if (destination.contains(blocked)) {
                Log.w(TAG, "Zona BLOQUEADA detectada: '$destinationName'. Viaje rechazado.")
                return false
            }
        }

        Log.d(TAG, "Zona aprobada para el destino: '$destinationName'.")
        return true
    }

    /**
     * Verifica si una zona es considerada preferida (para darles prioridad o bonificación).
     */
    fun isZonePreferred(destinationName: String): Boolean {
        val destination = destinationName.lowercase().trim()
        for (preferred in preferredZones) {
            if (destination.contains(preferred)) {
                return true
            }
        }
        return false
    }
}
