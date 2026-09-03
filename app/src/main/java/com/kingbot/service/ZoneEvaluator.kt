package com.kingbot.service

import android.util.Log

class ZoneEvaluator {

    companion object {
        const val TAG = "KingBotZones"

        // Lista negra de zonas restringidas, peligrosas o poco rentables (configurables)
        private val blockedZones = listOf(
            "zona peligrosa", 
            "sector rojo", 
            "afueras"
        )

        // Lista blanca opcional de zonas preferidas (si se requiere exclusividad)
        private val preferredZones = listOf(
            "centro", 
            "zona norte", 
            "comercial"
        )
    }

    /**
     * Valida si el destino del viaje está permitido según las reglas de zonas.
     * @param destinationText El texto extraído de la tarjeta que indica el destino o ruta.
     * @return true si el viaje pasa el filtro de zonas, false si debe ser rechazado.
     */
    fun isZoneAllowed(destinationText: String): Boolean {
        val cleanDest = destinationText.lowercase().trim()

        // 1. Verificar si contiene alguna zona prohibida explícita
        for (zone in blockedZones) {
            if (cleanDest.contains(zone)) {
                Log.w(TAG, "Zona bloqueada detectada: '$zone' en destino: [$destinationText]. Viaje rechazado.")
                return false
            }
        }

        // 2. Si se requiere validación de zonas preferidas y hay una lista, se puede verificar aquí.
        // Por defecto, si no está bloqueada y no viola las reglas, se acepta.
        Log.d(TAG, "Zona de destino aprobada para el texto: [$destinationText]")
        return true
    }
}
