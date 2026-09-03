package com.kingbot.engine

import android.util.Log

class PickupEvaluator {

    companion object {
        const val TAG = "KingBotPickup"
    }

    /**
     * Evalúa la distancia al punto de recogida (Punto A).
     * @param pickupKm Distancia actual al pasajero.
     * @param maxStandardPickupKm Límite estricto configurado por el usuario.
     * @param allowDynamicExtension Si está activo, permite ampliar un 30% adicional si el viaje es muy rentable.
     */
    fun isPickupValid(pickupKm: Double, maxStandardPickupKm: Double, allowDynamicExtension: Boolean): Boolean {
        if (pickupKm < 0.0) return false

        val effectiveMax = if (allowDynamicExtension) {
            maxStandardPickupKm * 1.3 // Ampliación dinámica del 30% si el switch está encendido
        } else {
            maxStandardPickupKm
        }

        val isValid = pickupKm <= effectiveMax

        Log.d(TAG, "Punto A (Recogida): ${pickupKm}km (Límite efectivo: ${effectiveMax}km) -> Aprobado: $isValid")
        return isValid
    }
}
