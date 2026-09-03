package com.kingbot.engine

import android.util.Log

class DropoffEvaluator {

    companion object {
        const val TAG = "KingBotDropoff"
    }

    /**
     * Evalúa la distancia total del viaje (Punto B / Destino).
     */
    fun isDropoffValid(totalTripKm: Double, maxAllowedTripKm: Double): Boolean {
        if (totalTripKm <= 0.0) return false

        val isValid = totalTripKm <= maxAllowedTripKm

        Log.d(TAG, "Punto B (Destino Total): ${totalTripKm}km (Límite máximo: ${maxAllowedTripKm}km) -> Aprobado: $isValid")
        return isValid
    }
}
